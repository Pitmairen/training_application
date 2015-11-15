package no.hials.trainingapp.routes.admin;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.datasource.Transaction;
import no.hials.trainingapp.routing.FormInput;
import no.hials.trainingapp.routing.FormRoute;
import no.hials.trainingapp.routing.Validators;
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

        FormInput form = setupFormValidation();

        if (getRequest().requestMethod().equals("POST")) {

            List<DataItem> sets = validateSets(form);

            if (form.postedAndValid()) {

                final DataItem workout = createWorkoutForInsert(form, prog);

                getDataSource().runTransaction((Transaction tx, DataSource ds) -> {

                    DataItem storedWorkout = ds.storeNewWorkout(workout);

                    prepareSetsForInsert(storedWorkout, sets);

                    ds.storeNewWorkoutSets(sets);

                    tx.commit();

                    getResponse().redirect("/admin/");

                    flashMessage("The workout has been created");

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

    private DataItem createWorkoutForInsert(FormInput form, DataItem program) {

        DataItem workout = new DataItem();
        workout.put("workout_name", form.getValue("workoutName"));
        workout.put("workout_description", form.getValue("workoutDesc"));
        workout.put("workout_date", form.getValue("workoutDate"));
        workout.put("workout_program_id", program.get("program_id"));

        return workout;

    }

    private void prepareSetsForInsert(DataItem workout, List<DataItem> sets) {
        for (DataItem set : sets) {
            set.put("set_workout_id", workout.get("workout_id"));
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

    private String formatToday() {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private FormInput setupFormValidation() {

        FormInput form = getFormInput();

        form.addRequiredInputs("workoutDesc", "workoutName", "workoutDate");

        form.addValidator("workoutDate", new Validators.DateValidator(false));

        return form;
    }

    private List<DataItem> validateSets(FormInput form) {

        List<DataItem> sets = new ArrayList<>();

        // Just set maximum supported set to 100 for now
        for (int i = 0; i < 100; i++) {

            // End on first non existing set
            if (form.getValue("set-" + i + "-weight") == null) {
                break;
            }

            DataItem set = getSubmittedSetData(form, i);

            if (!mValidExerciseIds.contains(set.getInteger("set_exercise_id"))) {

                form.addValidationError("Set nr" + (i + 1) + " has an invalid exercise");

                set.put("set_exercise_id", 0); // Reset id to default
            }

            sets.add(set);
        }

        if (sets.isEmpty()) {
            form.addValidationError("The workout must define at least one set");
        }

        return sets;

    }

    private DataItem getSubmittedSetData(FormInput form, int setId) {
        DataItem set = new DataItem();
        set.put("set_weight_planned", "");
        set.put("set_reps_planned", "");
        set.put("set_exercise_id", "");

        try {
            String value = form.getValue("set-" + setId + "-weight", "");
            if (value.isEmpty()) {
                form.addValidationError("Set nr " + (setId + 1) + " is missing weight ");
            } else {
                set.put("set_weight_planned",
                        Integer.parseInt(form.getValue("set-" + setId + "-weight")));
            }
        } catch (NumberFormatException e) {
            form.addValidationError("Set nr " + (setId + 1) + ": weight must be a number");
        }

        try {

            String value = form.getValue("set-" + setId + "-reps", "");
            if (value.isEmpty()) {
                form.addValidationError("Set nr " + (setId + 1) + " is missing reps");
            } else {
                set.put("set_reps_planned",
                        Integer.parseInt(form.getValue("set-" + setId + "-reps")));
            }
        } catch (NumberFormatException e) {
            form.addValidationError("Set nr " + (setId + 1) + ": reps must be a number");
        }

        try {
            set.put("set_exercise_id",
                    Integer.parseInt(form.getValue("set-" + setId + "-exercise", "")));

        } catch (NumberFormatException e) {
            form.addValidationError("Set nr " + (setId + 1) + " has an invalid exercise");
        }

        return set;

    }

}
