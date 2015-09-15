<?php



function html_escape($value){
    return htmlentities($value, ENT_QUOTES, 'UTF-8');
}
