<?php

namespace admin{

class IndexController extends \BaseController
{
    
    public function get()
    {
        
        return $this->renderTemplate("admin/index.php");
        
    }
    
    
}


}