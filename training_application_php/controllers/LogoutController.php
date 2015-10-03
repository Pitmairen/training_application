<?php

class LogoutController extends BaseController
{
    
    public function get()
    {
        if (!$this->getCurrentUser()->isAuthenticated()) {
            $this->getApp()->redirect('/');
        }

        $this->renderTemplate("logout.php");

    }
    
    
    public function post()
    {
        if(!$this->getCurrentUser()->isAuthenticated()) {
            $this->getApp()->redirect('/');
        }
        
        unset($_SESSION['user_name']);
        unset($_SESSION['user_rank']);
        unset($_SESSION['user_id']);

        $this->getApp()->redirect('/');
        
    }

    
}
