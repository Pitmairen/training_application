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

        

        // Force login by redirecting unauthenticated users to the login page.
        if(!isset($_SESSION['user'])){

            if($app->request->getResourceUri() !== '/login'){

                return $app->response()->redirect('/login');

            }
        }

        if(isset($_SESSION['user'])){

            $app->view->appendData(array(
                'current_user' => $_SESSION['user']
            ));

        }



        $this->next->call();

    }
}



