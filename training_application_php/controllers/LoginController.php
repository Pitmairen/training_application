<?php

class LoginController extends BaseController
{
    
    // Used to cache the user object in the password check function
    // so we don't need to fetch it from the database twich
    private $userCache;
    
    public function get()
    {
        if ($this->getCurrentUser()->isAuthenticated()) {

            $this->getApp()->redirect('/');
            return;
        }

        $form = load_form('login');

        $this->renderTemplate("login.php", ['login_form' => $form]);

    }
    
    
    public function post()
    {
        
        $form = load_form('login');

        $form->addConstraint(function($form){
            return $this->checkPassword($form);
        });
            

        if ($form->postedAndValid()) {

            $user = $this->userCache;

            Auth::loginUser($user);

            $this->getApp()->redirect('/');
        } else {
            $this->renderTemplate("login.php", ['login_form' => $form]);
        }
        
    }
    
    
    private function checkPassword($form){
        $pass = $form->getValue('password');
        $user = $this->getDataSource()->getUserByEmail($form->getValue('username'));
        if ($user && verify_password($pass, $user->user_pw)) {
            $this->userCache = $user;
            return;
        }
        return 'Wrong username or password';
    }
    
}
