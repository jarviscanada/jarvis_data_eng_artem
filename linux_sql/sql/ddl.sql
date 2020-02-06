--create a database
CREATE DATABASE host_agent;

--switch to a database
\c host_agent;

--create a table host_info if not exists
CREATE TABLE IF NOT EXISTS PUBLIC.host_info (
  id SERIAL NOT NULL,
  hostname VARCHAR(100) NOT NULL,
  cpu_number INT NOT NULL,
  cpu_architecture VARCHAR(100) NOT NULL,
  cpu_model VARCHAR(100) NOT NULL,
  cpu_mhz FLOAT NOT NULL,
  L2_cache INT NOT NULL,
  total_mem INT NOT NULL,
  timestamp TIMESTAMP NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (hostname)
);

--create a table host_usage if not exists
CREATE TABLE IF NOT EXISTS PUBLIC.host_usage (
  timestamp TIMESTAMP NOT NULL,
  host_id SERIAL NOT NULL,
  memory_free INT NOT NULL,
  cpu_idel INT NOT NULL,
  cpu_kernel INT NOT NULL,
  disk_io INT NOT NULL,
  disk_available INT NOT NULL,
  FOREIGN KEY (host_id) REFERENCES host_info(id)
); 
