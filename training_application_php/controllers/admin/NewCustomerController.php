<?php

namespace admin{


class NewCustomerController extends \BaseController
{
    
    public function get()
    {
        
        return $this->renderTemplate("admin/newcustomer.php");
        
    }
    
    
}


}