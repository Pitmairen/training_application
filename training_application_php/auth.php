<?php



/*
 * Adds the current logged in user to the view data.
 *
 */
class AuthMiddleware extends \Slim\Middleware
{
    public function call()
    {
        $app = $this->app;

        

        // Force login
        // if(!isset($_SESSION['user'])){
        //
        //     if($app->request->getResourceUri() !== '/login'){
        //
        //         $app->redirect('/login');
        //         
        //         $app->stop();
        //
        //     }
        // }
        //

        if(isset($_SESSION['user'])){

            $app->view->appendData(array(
                'current_user' => $_SESSION['user']
            ));

        }



        $this->next->call();

    }
}



