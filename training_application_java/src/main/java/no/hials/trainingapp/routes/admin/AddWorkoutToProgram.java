package no.hials.trainingapp.routes.admin;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.Transaction;
import no.hials.trainingapp.routing.FormRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Adds a new workout to a program
 */
public class AddWorkoutToProgram extends FormRoute {

    private final HashSet<Integer> mValidExerciseIds;

    public AddWorkoutToProgram(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);

        mValidExerciseIds = new HashSet<>();
    }

    @Override
    public ModelAndView handle() throws SQLException {

        DataItem prog = getProgram();

        if (prog == null) {
            Spark.halt(404);
        }

        List<DataItem> exercises = getDataSource().getAllExercises();
        setValidExerciseIds(exercises);

        if (getRequest().requestMethod().equals("POST")) {

            List<DataItem> sets = validateFormInput();

            if (!hasValidationErrors()) {

                final DataItem workout = createWorkoutForInsert(prog);

                getDataSource().runTransaction((Transaction tx, DataSource ds) -> {

                    DataItem storedWorkout = ds.storeNewWorkout(workout);

                    prepareSetsForInsert(storedWorkout, sets);

                    ds.storeNewWorkoutSets(sets);

                    tx.commit();

                    getResponse().redirect("/admin");
                    Spark.halt();
                });

            }

            setData("submittedSets", sets);

        }

        setData("program", prog);
        setData("toDay", formatToday());
        setData("exercises", exercises);

        return renderTemplate("admin/new-workout");
    }

    private DataItem createWorkoutForInsert(DataItem program) {

        Request req = getRequest();
        DataItem workout = new DataItem();
        workout.put("workout_name", req.queryParams("workoutName"));
        workout.put("workout_description", req.queryParams("workoutDesc"));
        workout.put("workout_date", req.queryParams("workoutDate"));
        workout.put("workout_program_id", program.get("program_id"));

        return workout;

    }

    private void prepareSetsForInsert(DataItem workout, List<DataItem> sets) {

        int prevExId = -1;
        int currentSetNr = 0;
        for (DataItem set : sets) {

            if (set.getInteger("set_exercise_id") != prevExId) {
                currentSetNr = 0;
            }
            set.put("set_nr", ++currentSetNr);
            set.put("set_duration_planned", 0);
            set.put("set_workout_id", workout.get("workout_id"));
            prevExId = set.getInteger("set_exercise_id");
        }
    }

    private DataItem getProgram() throws SQLException {

        try {
            int id = Integer.parseInt(getRequest().params("prog_id"));
            return getDataSource().getProgramById(id);
        } catch (NumberFormatException e) {
            Spark.halt(404);
        }

        return null;
    }

    private void setValidExerciseIds(List<DataItem> exercises) {

        for (DataItem it : exercises) {
            mValidExerciseIds.add(it.getInteger("exercise_id"));
        }

    }

    private List<DataItem> validateFormInput() {

        checkRequiredValues();
        validateDate(getRequest().queryParams("workoutDate"));

        return validateSets();
    }

    private void checkRequiredValues() {
        String[] requiredParams = new String[]{
            "workoutDesc", "workoutName", "workoutDate"};

        Request req = getRequest();

        for (String param : requiredParams) {
            if (req.queryParams(param).isEmpty()) {
                addValidationError(param + " is required");
            }
        }
    }

    private String formatToday() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void validateDate(String value) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = dateFormat.parse(value);

            if (dateFormat.format(date).equals(value)) {
                return;
            }

        } catch (ParseException e) {
            ;
        }

        addValidationError("Date should be in the format yyyy/mm/dd");

    }

    private List<DataItem> validateSets() {

        List<DataItem> sets = new ArrayList<>();

        // Just set maximum supported set to 100 for now
        for (int i = 0; i < 100; i++) {

            // End on first non existing set
            if (getRequest().queryParams("set-" + i + "-weight") == null) {
                break;
            }

            DataItem set = getSubmittedSetData(i);

            if (!mValidExerciseIds.contains(set.getInteger("set_exercise_id"))) {

                addValidationError("Set nr" + (i + 1) + " has an invalid exercise");

                set.put("set_exercise_id", 0); // Reset id to default
            }

            sets.add(set);
        }

        if (sets.isEmpty()) {
            addValidationError("The workout must define at least one set");
        }

        return sets;

    }

    private DataItem getSubmittedSetData(int setId) {

        Request req = getRequest();
        DataItem set = new DataItem();

        try {
            set.put("set_weight_planned",
                    Integer.parseInt(req.queryParams("set-" + setId + "-weight")));
        } catch (NumberFormatException e) {
            addValidationError("Set nr " + (setId + 1) + " is missing weight");
            set.put("set_weight_planned", "");
        }

        try {
            set.put("set_reps_planned",
                    Integer.parseInt(req.queryParams("set-" + setId + "-reps")));
        } catch (NumberFormatException e) {
            addValidationError("Set nr " + (setId + 1) + " is missing reps");
            set.put("set_reps_planned", "");
        }

        try {
            set.put("set_exercise_id",
                    Integer.parseInt(req.queryParams("set-" + setId + "-exercise")));

        } catch (NumberFormatException e) {
            addValidationError("Set nr " + (setId + 1) + " has an invalid exercise");
            set.put("set_exercise_id", 0);
        }

        return set;

    }

}
