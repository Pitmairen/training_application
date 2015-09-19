<?php
/*
 * Define some constants and set up the data source
 */

define('IS_DEBUGGING', true);
define('COOKIE_SECRET', '<super-secret>');

define('TPL_INC', APP_ROOT . 'templates/inc/');

date_default_timezone_set('Europe/Oslo');


$ds = new DataSourceSqlite('sqlite::memory:');
$ds->loadTestData(APP_ROOT . 'database_sqlite.sql');

