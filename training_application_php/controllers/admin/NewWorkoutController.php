<?php

namespace admin{


class NewWorkoutController extends \BaseController
{
    
    public function get()
    {
        
        return $this->renderTemplate("admin/newworkout.php");
        
    }
    
    
}


}