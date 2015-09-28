<?php
/*
 * Define some constants and set up the data source
 *
 * Copy this file and rename it to config.php
 */

define('IS_DEBUGGING', true);
define('COOKIE_SECRET', '<super-secret>');
date_default_timezone_set('Europe/Oslo');


// Create data source object here and store it in a 
// variable called $ds.
//
// Example:
$ds = new DataSourceSqlite('sqlite::memory:'); // Replace this with the real datasource


