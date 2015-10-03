<?php

class WorkoutLogController extends BaseController
{
    
    public function get()
    {
        

        $user = $this->getCurrentUser();

        $workouts = $this->getDataSource()->getWorkoutLogForUser($user->getID());

        $this->renderTemplate("stats.php", ['workout_log' => $workouts]);
        
    }
    
    
}


return 'WorkoutLogController';