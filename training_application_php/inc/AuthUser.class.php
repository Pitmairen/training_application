<?php

/**
 * Represents an authenticated user.
 */
class AuthUser implements User
{

    private $session;

    public function __construct($session){
        $this->session = $session;
    }

    public function getID(){
        return $this->session['user_id'];
    }

    public function getName(){
        return $this->session['user_name'];
    }

    public function isAuthenticated(){
        return true;
    }

    public function rank(){
        return $this->session['user_rank'];
    }

}
