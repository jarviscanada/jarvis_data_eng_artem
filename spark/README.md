Table of contents
* [Introduction](#Introduction)
* [Spark Architecture](#Spark-Architecture)
* [Spark RDD Project 1](#Spark-RDD-Project-1)
* [Spark RDD Project 2](#Spark-RDD-Project-2)

# Introduction
- Purpose: Help the data analytics team to process data using Apache Spark and evaluate different tools. 
- Learned: Apache Spark - open-source distributed cluster-computing framework. It has different tools to process the data, which include: lower level Spark RDD, higher-level Spark Data Frames, DataSets, and SQL. Depending on the task the data can be processed using one of them. 

# Spark Architecture
- Spark Architecture diagram
	- driver & executors
  - YARN
  - SparkSession

# Spark RDD Project 1
- Spark RDD (Resilient Distributed Dataset) - a fault-tolerant distributed collection of records spread over one or many partitions. RDDs are considered low level and advised to be used in the situations when higher-level structures like DataFrames or DataSets cannot be used instead. RDDs involve two main types of operations: transformations and actions. Transformations are lazy operations that return other RDD, while actions are operations that involve some computations on the data and return of a value. 
- In this project, RDDs were used to store and process the data in different ways (selecting, joining, casting, etc.). In particular, the CSV file was parsed and transformed to RDD. Then other types of manipulation were applied. 
- Notebook screen shot
	- use `Full Page Screen Capture` chrome extention to capture a webpage as a picture

# Spark RDD Project 2
- what's Spark Structured API?
	- SQL, DF, Dataset
  - Why structured API is preferred over RDDs?
- the purpose of your project
- Notebook screen shot
	- use `Full Page Screen Capture` chrome extention to capture a webpage as a picture
