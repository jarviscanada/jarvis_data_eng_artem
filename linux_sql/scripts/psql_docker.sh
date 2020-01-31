#!/bin/bash

commands=$1
password=$2

#start docker
systemctl status docker > /dev/null || systemctl start docker

#if command is stop, stop jrvs-psql container  if it is not already stoped
if [ "$commands" == "stop" ]
then
  if docker ps -f name=jrvs-psql | egrep "jrvs-psql"
  then
    docker container stop jrvs-psql
    exit 0
  else
    echo "jrvs-psql is already stoped!"
    exit 0
  fi
fi

#if command is start

#check if psql container is running, if so exit
if docker ps -f name=jrvs-psql | egrep "jrvs-psql" > /dev/null
then
  exit 0
fi

#check if the psql volume is created, if not create one
if [ "$(docker volume ls | egrep "pgdata")" == "" ]
then
  docker volume create pgdata
fi

#check if the psql container is created, if not create one
if [ "$(docker container ps -a -f name=jrvs-psql | egrep "jrvs-psql")" == "" ]
then
  docker run --name jrvs-psql -e POSTGRES_PASSWORD=$password -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres
fi

docker container start jrvs-psql
