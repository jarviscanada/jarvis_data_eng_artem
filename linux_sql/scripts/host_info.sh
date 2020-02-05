#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

#validate arguments
if [ "$#" -ne 5 ]; then
    echo "Illegal number of parameters"
    exit 1
fi

lscpu_out=`lscpu`
memory_info=`cat /proc/meminfo`

#parse the usage info
hostname=$(hostname -f)
cpu_number=$(echo "$lscpu_out" | egrep "^CPU\(s\):" | awk '{print $2}' | xargs)
cpu_architecture=$(echo "$lscpu_out" | egrep "^Architecture:" | awk '{print $2}' | xargs)
cpu_model=$(echo "$lscpu_out" | egrep "^Model name:" | awk '{print $3,$4,$5,$6,$7}' | xargs)
cpu_mhz=$(echo "$lscpu_out" | egrep "^CPU MHz:" | awk '{print $3}')
l2_cache=$(echo "$lscpu_out" | egrep "^L2 cache:" | awk '{print $3}' | xargs)
total_mem=$(echo "$memory_info" | egrep "^MemTotal:" | awk '{print $2}' | xargs)
timestamp=`date +"%Y-%m-%d %H:%M:%S"`

insertHostInfoQuery="INSERT INTO host_info (hostname,cpu_number,cpu_architecture,cpu_model,cpu_mhz,L2_cache,total_mem,timestamp) VALUES ('"$hostname"','"$cpu_number"','"$cpu_architecture"','"$cpu_model"','"$cpu_mhz"','"${l2_cache%?}"','"$total_mem"','"$timestamp"');"

#connect to a database and execute the query
export PGPASSWORD=$psql_password
psql -h $psql_host -p $psql_port  -d $db_name -U $psql_user -c "$insertHostInfoQuery"

exit 0  

