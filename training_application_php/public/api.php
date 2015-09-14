<?php

require './init.php';


$app->get('/hello/:name', function ($name) use($app) {
    $app->render(200,[
        'msg' => 'welcome to my API! ' . $name
    ]);
});


$app->post('/auth/login', function () use($app, $ds) {

    $email = $app->request->poset('email');
    $password = $app->request->post('password');

    $customer = $ds->getCustomerByEmail($email);

    // FIXME: Just for testing. Need to add hashing
    if($customer && $customer->pw == $password){
        $app->render(200,[
            'token' => '<generated auth token>'
        ]);
    }else{
        $app->render(401,[
            'msg' => 'wrong username or password'
        ]);
    }

});



$app->run();

