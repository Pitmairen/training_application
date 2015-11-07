#!/usr/bin/env bash

# Default db file
DB_FILE="/tmp/trainingappjava.db"

# Check if an alternate db file is specified as a command line argument
if [ -n "$1" ]; then
    DB_FILE="$1"
fi

# Remove existing file
if [ -e "$DB_FILE" ]; then 
    printf "$DB_FILE exists. Remove? (y/n) "
    read -n 1 do_delete 
    echo 
    if [ "$do_delete" == "y" ]; then
        rm "$DB_FILE"
    else
        echo "Aborting"
        exit 
    fi
fi


echo "::Creating database: $DB_FILE"

echo "::Running Initial setup"
sqlite3 "$DB_FILE" < initial_setup.sql
if [ "$?" != "0" ]; then
    echo ":: An error occured, aborting."
    exit $?
fi

echo "::Inserting test data"
sqlite3 "$DB_FILE" < fill_with_testdata.sql
if [ "$?" != "0" ]; then
    echo ":: An error occured, aborting."
    exit $?
fi
echo "::Done"


