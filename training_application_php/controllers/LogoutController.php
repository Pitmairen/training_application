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
        
        Auth::logoutCurrentUser();

        $this->getApp()->redirect('/');
        
    }

    
}
