# Linux Cluster Monitoring Agent

## Introduction
Cluster Monitor Agent is an internal tool that collects hardware specifications of each server and monitors usage of resources by each of the servers. All of the information is stored in the RDBMS (PostgreSQL). It helps the infrastructure team to generate reports for future resource planning.

## Architecture and Design
1. ![image](/linux_sql/diagram.png)
2. There are two tables: host_info and host_usage. host_info stores information about the server hardware specifications which include: information about CPU, cache and total memory. This information is stored once for each server. host_usage is another table that stores information about the current use of resources by the server. This includes memory usage, CPU usage. This information is stored every minute.

3. There are three scripts: psql_docker.sh, host_info.sh, host_usage.sh. psql_docker.sh is a script that starts/stops the Docker PostgreSQL container. This includes creating containers (including the creation of a volume) if it does not exist yet. host_info.sh is a script that creates a query that inserts hardware specifications into the host_info table of a database, then it executes the query. Connection to database details is provided as arguments to the script. host_usage.sh is another script that creates the query that inserts information about the resource usage by the server to the database. The query is executed then. The details of the connection to the database are provided as arguments to the script. 

## Usage
1) To initialize the database psql_docker.sh, must be run with parameter "start". To stop the database container run with argument "stop"
2) host_info.sh usage: run with details of connection to the database as arguments (psql_host psql_port db_name psql_user psql_password). This will store the information about the hardware specifications to the table. 
3) host_usage.sh usage: run with details of connection to the database as arguments (psql_host psql_port db_name psql_user psql_password). This will store information about the current usage of the system's resources.
4) crontab setup: run bash command: crontab -e, then put four asteriscs with one whitespace between them and provide the command that will run the host_usage.sh script (include full path) with output redirection to the file of your choice (will be used as a log). This will make the system run the script every minute.
## Improvements 
1) handle hardware update
2) improve the performance of the queries 
3) improve user interface  of the program
