<?php

class WorkoutScheduleController extends BaseController
{
    
    public function get()
    {
        
        $user = $this->getCurrentUser();

        $workouts = $this->getDataSource()
                         ->getNextWorkoutsForCustomer($user->getID());
        
        return $this->renderTemplate("workout-schedule.php", [
            'next_workouts' => $workouts
        ]);
        
    }
    
    
}

