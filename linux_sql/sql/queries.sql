--GROUP HOSTS BY HARDWARE INFO
SELECT cpu_number, id, total_mem FROM host_info, total_mem DESC;

--AVERAGE MEMORY USAGE
SELECT   host_id, 
         hostname, 
         Date_trunc('hour', a.timestamp)+ interval '5 minute'*round(date_part('minute', a.timestamp)/5.0) AS time,
         round(avg((b.total_mem-memory_free*1024)*100/b.total_mem)) AS avg_used_mem_percentage
FROM     host_usage a, 
         host_info b 
WHERE    b.id=a.host_id 
GROUP BY time, 
         host_id, 
         hostname;

