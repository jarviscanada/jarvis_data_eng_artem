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
- Draw a component diagram which contains controllers, services, DAOs, psql, IEX Cloud, WebServlet/Tomcat, HTTP client, and SpringBoot. 
- briefly explain the following components and services (3-5 sentences for each)
  - Controller layer (e.g. handles user requests....)
  - Service layer
  - DAO layer
  - SpringBoot: webservlet/TomCat and IoC
  - PSQL and IEX

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
