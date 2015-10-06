<?php



/**
 * Defines the structure of a user
 */
interface User
{

    /**
     * Returns the id of the current user
     */
    public function getID();

    /**
     * Returns the name of the current user
     */
    public function getName();

    /**
     * Returns true if the user is authenticated
     */
    public function isAuthenticated();

    /**
     * Returns the rank level of the curent user.
     * The rank levels are defined in Auth class.
     */
    public function rank();

    /**
     * Returns true if user is admin
     */
    public function isAdmin();
}



