<?php



/**
 * Defines some constands related to authentication
 */
class Auth
{
    const RANK_GUEST = 0;
    const RANK_USER = 1;
    const RANK_ADMIN = 3;



    private static $user = null;

    public static function getCurrentUser(){

        if(self::$user == null){
            if(!isset($_SESSION['user_name'])){
                self::$user = new GuestUser();
            }else{
                self::$user = new AuthUser($_SESSION);
            }
        }

        return self::$user;

    }


}



