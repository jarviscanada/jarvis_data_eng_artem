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

stats=`vmstat --unit M`
disk=`df -BM /`

#parse the usage info
memory_free=$(echo "$stats" | awk 'NR==3 {print $4}' | xargs)
cpu_idle=$(echo "$stats" | awk 'NR==3 {print $15}' | xargs)
cpu_kernel=$(echo "$stats" | awk 'NR==3 {print $14}' | xargs)
disk_io=$(echo "$stats" | awk 'NR==3 {print $10}' | xargs)
disk_available=$(echo "$disk" | awk 'NR==2 {print $4}' | xargs)
timestamp=`date +"%Y-%m-%d %H:%M:%S"`

#query to get the id associated with the hostname in host_info table
findHostNameQuery="SELECT id  FROM host_info WHERE hostname='$(hostname -f)'"

#export password for database connection
export PGPASSWORD=$psql_password

#get host id
host_id=`psql -h $psql_host -p $psql_port  -d $db_name -U $psql_user -t -c"$findHostNameQuery"`

#main query for insertion of a usage details into host_usage table
insertUsageInfoQuery="INSERT INTO host_usage (timestamp,host_id,memory_free,cpu_idel,cpu_kernel,disk_io,disk_available) VALUES ('$timestamp','"$host_id"','$memory_free','$cpu_idle','$cpu_kernel','$disk_io','"${disk_available%?}"')"

 
#execute main query
psql -h $psql_host -p $psql_port  -d $db_name -U $psql_user -c "$insertUsageInfoQuery"

exit 0

