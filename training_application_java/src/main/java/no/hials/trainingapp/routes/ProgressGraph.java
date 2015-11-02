package no.hials.trainingapp.routes;

import java.sql.SQLException;
import java.util.List;
import no.hials.trainingapp.datasource.DataItem;
import no.hials.trainingapp.datasource.DataSource;
import no.hials.trainingapp.routing.TemplateRoute;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Displays a graph of the customers progress
 */
public class ProgressGraph extends TemplateRoute {

    public ProgressGraph(DataSource datasource, Request req, Response resp) {
        super(datasource, req, resp);
    }

    @Override
    public ModelAndView handle() throws Exception {
        
        DataItem exercise = getExercise();
       
        List<DataItem> progress = getDataSource().getProgressForExercise(
                getCurrentUser().getId(),
                exercise.getInteger("exercise_id"));
        

        setData("exercise", exercise);
        setData("progress", progress);
        setData("showGraph", progress.size() >= 2);
        
        
        return renderTemplate("progress-graph");
    }
    
    
    private DataItem getExercise() throws SQLException{
        try{
           int id = Integer.parseInt(getRequest().params("id"));
           return getDataSource().getExerciseById(id);
        }catch(NumberFormatException e){
            return null;
        }
    }

}
