<?php

class WorkoutController extends BaseController
{
    
    public function get($id)
    {
        $workout = $this->getWorkoutOr404($id);
        $sets = $this->getDataSource()->getExerciseSetsForWorkoutById($id);

        if($workout->workout_done) {
            $this->renderTemplate("workout-done.php", 
                    ['sets' => $sets, 'workout' => $workout]);

        }else{

            $form = load_form('workout', ['sets' => $sets]);

            $this->renderTemplate("workout.php", 
                ['workout_form' => $form, 'workout' => $workout]);

        }
        
    }
    
    
    public function post($id)
    {
        $workout = $this->getWorkoutOr404($id);
         
        $sets = $this->getDataSource()->getExerciseSetsForWorkoutById($id);

        $form = load_form('workout', ['sets' => $sets]);

        if($form->postedAndValid()){

            $set_vals = $this->createSetValuesForUpdate($form, $sets);
            $desc = $form->getValue('workout_description');

            $this->getDataSource()->runTransaction(function($ds) use($id, $desc, $set_vals){

                $ds->updateWorkoutCompleted($id, $desc);
                $ds->updateExerciseSetValues($set_vals);

                $ds->commit();
            });


            $this->getApp()->redirect('/workout');
        }

        $this->renderTemplate("workout.php", ['workout' => $workout,
                                    'workout_form' => $form]);
        
    }
    
    
    private function createSetValuesForUpdate($form, $sets){
        
        $set_vals = [];
        foreach ($sets as $set) {
            $set_vals[$set->set_id] = [
                'weight' => $form->getValue('weight-' . $set->set_id),
                'reps' => $form->getValue('reps-' . $set->set_id),
            ];
        }
        return $set_vals;
    }
    
    private function getWorkoutOr404($id)
    {
        $workout = $this->getDataSource()->getWorkoutById($id);
        
        if(!$workout){
            $this->getApp()->notFound();
        }
        return $workout;
    }
    
}

