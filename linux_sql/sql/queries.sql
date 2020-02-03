--GROUP HOSTS BY HARDWARE INFO
SELECT cpu_number, id, total_mem FROM host_info, total_mem DESC;

--AVERAGE MEMORY USAGE
SELECT host_id,hostname, date_trunc('hour', A.timestamp)+ interval '5 minute'*round(date_part('minute', A.timestamp)/5.0) AS time, ROUND(AVG((B.total_mem-memory_free*1024)*100/B.total_mem)) AS avg_used_mem_percentage FROM host_usage A, host_info B WHERE B.id=A.host_id GROUP BY time,host_id,hostname;

