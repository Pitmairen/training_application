<?php

class AdminMiddleware {
    
    private $app;
    
    public function __construct($app){
        
        $this->app = $app;
        
    }
    
    
    public function __invoke(){
        
        $user = Auth::getCurrentUser();
        
        if(!$user->isAdmin()){
            $this->app->halt(403, 'You are not an admin');
        }
        
    }
    
    
}
