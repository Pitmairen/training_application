<?php



/**
 * Represents an unauthenticated user.
 */
class GuestUser implements User
{
    public function getName(){
        return "Guest";
    }

    public function isAuthenticated(){
        return false;
    }

    public function rank(){
        return 0;
    }

}



