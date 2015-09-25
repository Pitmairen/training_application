<?php


class Form extends Gregwar\Formidable\Form
{

    private $errors;

    function getErrors(){
        return $this->errors;
    }


    function hasErrors(){
        return count($this->errors) > 0;
    }

    function renderErrors($templateFile){
        $form = $this;
        require $templateFile;
    }

    function postedAndValid(){

        if(!$this->posted())
            return false;

        $this->errors = $this->check();

        return !$this->hasErrors();
    }

}


