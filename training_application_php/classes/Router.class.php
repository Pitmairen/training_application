<?php

class Router{
    
    private $app;
    private $ds;
    
    public function __construct(\Slim\Slim $app, DataSource $ds)
    {
        $this->app = $app;
        $this->ds = $ds;
    }
    
    public function get($route, $controller, ...$extra)
    {
        $this->app->get($route, function(...$params) use($controller, $extra){
            
            $controller = new $controller($this->app, $this->ds, ...$extra);
            $controller->get(...$params);
        });
    }
    
    
    public function post($route, $controller, ...$extra)
    {
        $this->app->post($route, function(...$params) use($controller, $extra){
            
            $controller = new $controller($this->app, $this->ds, ...$extra);
            $controller->post(...$params);
        });
    }
    
}