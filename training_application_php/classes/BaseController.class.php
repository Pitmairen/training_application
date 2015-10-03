<?php

abstract class BaseController {
    
    private $dataSource;
    private $app;
    
    public function __construct(\Slim\Slim $app, DataSource $datasource)
    {
        $this->app = $app;
        $this->dataSource = $datasource;
    }
    
    protected function getCurrentUser()
    {
        return Auth::getCurrentUser();
    }
    
    protected function getDataSource()
    {
        return $this->dataSource;
    }
    
    protected function getApp()
    {
        return $this->app;
    }
    
    protected function renderTemplate($name, $data=[])
    {
        $this->app->render($name, $data);
    }
    
    
}
