<?php



class DataSourceBase
{

    protected $db;

    public function __construct($db){
        $this->db = $db;
    }


    public function lastInsertId(){
        return $this->db->lastInsertId();

    }

    public function commit(){
        $this->db->commit();
    }

    public function runTransaction($callback){

        $this->db->beginTransaction();

        try{
            $callback($this);
        }
        catch(Exception $e){
            $this->db->rollback();
            throw $e;
        }


    }


}


