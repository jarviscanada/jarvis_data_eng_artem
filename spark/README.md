Table of contents
* [Introduction](#Introduction)
* [Spark Architecture](#Spark-Architecture)
* [Spark RDD Project](#Spark-RDD-Project)
* [Spark DataFrame Project](#Spark-DataFrame-Project)

# Introduction
- Purpose: Help the data analytics team to process data using Apache Spark and evaluate different tools. 
- Learned: Apache Spark - open-source distributed cluster-computing framework. It has different tools to process the data, which include: lower level Spark RDD, higher-level Spark Data Frames, DataSets, and SQL. Depending on the task the data can be processed using one of them. 

# Spark Architecture
- Spark Architecture diagram
	- driver & executors
  - YARN
  - SparkSession

# Spark RDD Project
- Spark RDD (Resilient Distributed Dataset) - a fault-tolerant distributed collection of records spread over one or many partitions. RDDs are considered low level and advised to be used in the situations when higher-level structures like DataFrames or DataSets cannot be used instead. It is advised that RDDs should be used instead of higher-level APIs in one of three cases: you need some functionality that is not available in higher-level APIs, you need to maintain a legacy codebase written using RDDs or you need to do some custom shared variable (variables that are used in custom user-defined functions that have special properties when running on a cluster) manipulation. RDDs involve two main types of operations: transformations and actions. Transformations are lazy operations that return other RDD, while actions are operations that involve some computations on the data and return of a value. 
- In this project, I learned how to create RDDs from different sources, manipulate the data that is inside, use actions and transformations to resemble SQL queries. 
![image](/spark/Notebook2.png)

# Spark DataFrame Project
- Spark Structured APIs - table-like collections with well-defined rows and columns. They include Spark DataFrame, Spark DataSet, and SQL. DataFrames and DataSets represent immutable, lazily evaluated plans that specify what operations to apply to data residing at a location to generate some output. Spark SQL allows running SQL against views and tables organized into databases. These APIs considered a higher level and are preferred over RDDs in most of the tasks. In cases other than the ones that are described in the previous section, higher-level APIs are used. 
- The purpose of this project was to learn different types of manipulation on the data using Spark DataFrames. It involved: creating DataFrames from different sources and executing operations on them (select, aggregations, window functions, filtering, etc.)
![image](/spark/Notebook.png)
