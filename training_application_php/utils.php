<?php



function html_escape($value){
    return htmlentities($value, ENT_QUOTES, 'UTF-8');
}


function load_form($name){

    return new Form(APP_ROOT . 'templates/forms/' . $name . '.html');

}
