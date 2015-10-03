 <?php

class LoadTemplateController extends BaseController
{
    
    private $templateName;
    
    public function __construct(\Slim\Slim $app, DataSource $datasource, $templateName)
    {
        parent::__construct($app, $datasource);
        $this->templateName = $templateName;
    }
    
    public function get()
    {
        return $this->renderTemplate($this->templateName);
    }
    
    
}
