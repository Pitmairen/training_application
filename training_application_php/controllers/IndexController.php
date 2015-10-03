<?php

class IndexController extends BaseController
{
    
    public function get()
    {
        
        return $this->renderTemplate("index.php");
        
    }
    
    
}
