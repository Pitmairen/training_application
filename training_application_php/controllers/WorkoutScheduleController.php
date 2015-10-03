<?php

class WorkoutScheduleController extends BaseController
{
    
    public function get()
    {
        
        $user = $this->getCurrentUser();

        $workouts = $this->getDataSource()
                         ->getNextWorkoutsForUser($user->getID());
        
        return $this->renderTemplate("workout-schedule.php", [
            'next_workouts' => $workouts
        ]);
        
    }
    
    
}


return 'WorkoutScheduleController';