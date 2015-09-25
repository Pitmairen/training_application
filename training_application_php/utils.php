<?php



function html_escape($value){
    return htmlentities($value, ENT_QUOTES, 'UTF-8');
}


function load_form($name){

    return new Form(APP_ROOT . 'templates/forms/' . $name . '.html');

}


function hash_password($plaintext_password){

    return password_hash($plaintext_password, PASSWORD_DEFAULT);

}

function verify_password($plaintext_password, $password_hash){

    return password_verify($plaintext_password, $password_hash);

}

