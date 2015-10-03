<?php


/*
 * Adds the current logged in user to the view data and 
 * makes sure the user is logged in.
 *
 */
class AuthMiddleware extends \Slim\Middleware
{

    public function call()
    {
        $app = $this->app;

        $current_user = Auth::getCurrentUser();

        // Force login by redirecting unauthenticated users to the login page.
        if(!$current_user->isAuthenticated()){

            if($app->request->getResourceUri() !== '/login'){
                return $app->response()->redirect('/login');
            }

        }

        // Add the current user to the template data
        $app->view->appendData(array(
            'current_user' => $current_user
        ));


        $this->next->call();

    }
}



