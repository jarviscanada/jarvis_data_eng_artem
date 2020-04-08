Table of contents
* [Introduction](#Introduction)
* [Hadoop Cluster](#Hadoop-Cluster)
* [Hive Project](#Hive-Project)
* [Improvements](#Improvements)


# Introduction
- Purpose of this project: help data analytics team to process the data using Apache Hadoop  
- Evaluated Core Hadoop components, including MapReduce, HDFS, and YARN
- Provisioned a Hadoop Cluster using GCP
- Solved business problem using Apache Hive and Zeppelin Notebook

# Hadoop Cluster
![image](/hadoop/ClusterDiagram.png)
The cluster consists of one Master Node and two Worker Nodes. Each node has 2 CPUs, 12 GB RAM, 100 GB of storage memory. This cluster uses Hadoop ecosystem to store and process the data as well as some other APIs that simplify the use of Hadoop. Cluster consists consist of multiple components: HDFS, YARN, Hive, Zeppelin.

### HDFS
Hadoop Distributed File System (HDFS) - a storage system that is used by Hadoop to store large data sets. It is highly scalable and can be run on commodity hardware. HDFS splits the file into equal-sized chunks (by default 128 MB) and stores them among the nodes. Two additional copies of each chunk are created and stored among other nodes to maintain fault tolerance. There are two types of nodes in HDFS: Name Node and Data Node. Name node contains the metadata (location of the file chunks, file permissions, number of alive nodes, etc.). Data Nodes contain the actual data chunks. Data Node failure does not have a big impact on HDFS since other nodes contain the copies of the data that is lost with the failed Data Node. In the case of a Name Node failure, there are two additional Name Nodes that are in standby mode available to replace a failed Named Node. Thus, the system is highly available as well. In the created cluster, Master Node contains the Name Node, while Worker Nodes are used as Data Nodes.

### YARN
Yet Another Resource Negotiator (YARN) - a resource manager and a job scheduler. It consists of three components: Resource Manager, Node Managers, and Application Masters. Resource Manager is used to allocating resources for the jobs, keep track of application progress and alive nodes. It communicates with Node Managers and Application Masters. Node Manager is run on each node and is used to create containers for the jobs and Application Masters. It provides the allocated resources to the jobs in containers. Application Master is run in containers and is used to ask Resource Manager for resources and coordinate the execution of all tasks for a job. In the cluster, the Resource Manager is run inside the Master Node, each Worker Node contains a Node Manager. Application Master is created when a job is executed.

### Hive
Apache Hive - a warehousing tool that is used on top of Hadoop. Basically, it allows executing SQL-like queries aginst the data in HDFS. Using Hive it is possible to create a table with defined schemas on top of the data in HDFS. It uses a schema-on-write mechanism and different execution engines such as MapReduce, Spark, and Tez. To execute commands in the Hive, there is a built-in CLI tool called Beeline. Hive submits a job to YARN which starts the execution.

### Zeppelin
Zeppelin - another external API that is basically a web notebook that gives you an easy, straightforward way to execute arbitrary code. It allows executing SQL, Scala and even scheduling a job to run at some interval. Its easy to use UI is preferred over the CLI that is provided by Hive. However, when a query is executed in the Zeppelin notebook, it calls Hive through JDBC drivers. 


# Hive Project
- The purpose of this project was to practice processing big data using Apache Hive. Create different types of tables and store the data inside them. Then, see the effects of each table on speed of the queries. In addition, try out different SerDe (lazySerDe and OpenCSVSerDe).  
![image2](/hadoop/notebook.png)

# Improvements
If you have more time, what would you improve?
- Try out bucketing and see it's influence on query performance
- Try out all queries used in the project using Scala to increase the performance of queries
- Try to combine partitioning and bucketing to increase the performance of queries
