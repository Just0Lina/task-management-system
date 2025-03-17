#!/bin/bash
echo "Creating or replacing database: ${POSTGRES_DB}"
psql -U ${POSTGRES_USER} -c "DROP DATABASE IF EXISTS ${POSTGRES_DB};"
psql -U ${POSTGRES_USER} -c "CREATE DATABASE ${POSTGRES_DB};"