Table of contents
* [Introduction](#Introduction)
* include all first level titles

# Introduction
The application is a trading platform that allows users to get different information about the stock market as well as account and trader details. The source of the stock market data is IEX Cloud. The application makes it possible to get the latest standing details of every stock on the market in real-time. In addition, new traders can be registered using the app, which will automatically put the details into the database and create a new account for the trader. Traders can perform the buy/sell operations using the application and each operation will be added to the log. This allows trader to keep track of his orders on the market. The app designed not just for traders, anyone that wants to get latest information on the stock market is able to do so very easily. 
The app was build using Spring Boot framework. To handle dependencies Maven was used. Tested with Mockito and JUnit. PostgreSQL is a database.

# Quick Start
- Prequiresites: Docker, CentOS 7
- Docker scritps with description
	- build images
  - create docker network
  - start containers
- Try trading-app with SwaggerUI (screenshot)

# Architecture
![image](/springboot/Diagram.png) 

### Controller layer
This layer is responsible for consuming the input from the user and pass data to the service layer. In this API there are three controllers with different mappings that serve their own roles. Trader Account Controller is used to perform operations on trader accounts, Order Controller is used to working with orders and Quote Controller used to work with quotes. When the service layer is done operating on the data, the controller layer sends a response to the user. 

### Service layer
This layer contains the business logic of the application. There are three service layers that receive data from corresponding controllers and perform operations on it. If the service layer needs to communicate to the database or other external sources of data, it uses the Data Access Layer. When operations on the data are done, some data may be passed back to the controller layer to be included in response to the client request. 

### DAO layer
This layer is used to communicate with external sources including databases or other APIs. In our API there are six Data Access Objects, five of them are connected to databases and one to external API. Each DAO is responsible for one source. DAO layer ensures that the service layer is responsible only for business logic and not for communication with external sources. This provides the separation of concerns that make API more robust and scalable.

### Web Servlet
Web servlet is provided by the Spring Boot framework and is used to receive HTTP requests from a client and send actual HTTP responses. When the client sends a request, web servlet accepts it and transfers to the controller based on the URL mapping. When web servlet sends a request to the controller, Spring Boot transforms the data from requests (body, headers, path variables) into the format that can be accepted by the controller.  

### PSQL and IEX
PSQL is used for creating databases and storing the data there. There are four databases and one view in the API that are used to store different data. Databases are communicating with the DAO layer only. IEX is an external API that is used to get recent data from the stock market. It sends it to the corresponding DAO. 

# REST API Usage
## Swagger
What's swagger (1-2 sentences, you can copy from swagger docs). Why are we using it or who will benefit from it?
## Quote Controller
- High-level description for this controller. Where is market data coming from (IEX) and how did you cache the quote data (PSQL). Briefly talk about data from within your app
- briefly explain each endpoint
  e.g.
  - GET `/quote/dailyList`: list all securities that are available to trading in this trading system blah..blah..
## Trader Controller
- High-level description for trader controller (e.g. it can manage trader and account information. it can deposit and withdraw fund from a given account)
- briefly explain each endpoint
##Order Controller
- High-level description for this controller.
- briefly explain each endpoint
## App controller
- briefly explain each endpoint
## Optional(Dashboard controller)
- High-level description for this controller.
- briefly explain each endpoint

# Docker Deployment
- docker diagram including images, containers, network, and docker hub
e.g. https://www.notion.so/jarviscanada/Dockerize-Trading-App-fc8c8f4167ad46089099fd0d31e3855d#6f8912f9438e4e61b91fe57f8ef896e0
- describe each image in details (e.g. how psql initialize tables)

# Improvements
If you have more time, what would you improve?
- at least 5 improvements
