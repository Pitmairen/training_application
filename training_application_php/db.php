<?php



class DB
{

    private $pdo;

    function __construct($pdo){

        $this->pdo = $pdo;


    }


    /**
     * Used for statements that do not return a result set. (INSERT, UPDATE, DELETE ...)
     */
    public function execute($query, $params=[]){

        $sth = $this->prepareExecute($query, $params);

        $res = $sth->execute();
        $sth->closeCursor();

        return $res;

    }



    public function fetchOneObject($query, $params=[]){

        $sth = $this->prepareQuery($query, $params);
        $sth->execute();
        $res = $sth->fetchObject();
        $sth->closeCursor();

        return $res;

    }


    public function fetchOneAssoc($query, $params=[]){

        $sth = $this->prepareQuery($query, $params);
        $sth->execute();
        $res = $sth->fetch(PDO::FETCH_ASSOC);
        $sth->closeCursor();

        return $res;


    }

    public function fetchAllAssoc($query, $params=[]){

        $sth = $this->prepareQuery($query, $params);
        $sth->execute();


        return $sth->fetchAll(PDO::FETCH_ASSOC);


    }

    public function fetchAllObject($query, $params=[], $class=null){

        $sth = $this->prepareQuery($query, $params);

        $sth->execute();

        return $sth->fetchAll(PDO::FETCH_CLASS);

    }


    public function lastInsertId(){
        return $this->pdo->lastInsertId();
    }


    public function beginTransaction(){

        $this->pdo->beginTransaction();

    }

    public function commit(){

        $this->pdo->commit();

    }

    public function rollback(){

        $this->pdo->rollback();

    }



    private function prepareQuery($query, $params){

        $sth = $this->pdo->prepare($query);

        return $this->bindParams($sth, $params);
    }

    private function prepareExecute($query, $params){

        $sth = $this->pdo->prepare($query);

        return $this->bindParams($sth, $params);
    }


    private function bindParams($sth, $params){

        foreach($params as $key => $value){
            $sth->bindValue($key, $value, $this->getParamType($value));
        }

        return $sth;
    }

    private function getParamType($value){

        if(is_int($value))
            return PDO::PARAM_INT;
        else if(is_bool($value))
            return PDO::PARAM_BOOL;
        else if(is_null($value))
            return PDO::PARAM_NULL;
        else if(is_string($value))
            return PDO::PARAM_STR;
        else
            throw new Exception("Unknown param type");
    }



}









