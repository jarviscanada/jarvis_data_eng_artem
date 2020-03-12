Table of contents
* [Introduction](#Introduction)
* [Quick Start](#Quick-Start)
* [Architecture](#Architecture)
* [REST API Usage](#REST-API-Usage)
* [Docker Deployment](#Docker-Deployment)
* [Improvements](#Improvements)

# Introduction
The application is a trading platform that allows users to get different information about the stock market as well as account and trader details. The source of the stock market data is IEX Cloud. The application makes it possible to get the latest standing details of every stock on the market in real-time. In addition, new traders can be registered using the app, which will automatically put the details into the database and create a new account for the trader. Traders can perform the buy/sell operations using the application and each operation will be added to the log. This allows trader to keep track of his orders on the market. The app designed not just for traders, anyone that wants to get latest information on the stock market is able to do so very easily. 
The app was build using Spring Boot framework. To handle dependencies Maven was used. Tested with Mockito and JUnit. PostgreSQL is a database.

# Quick Start
- Prequiresites: Docker, CentOS 7
- Docker scritps: 
- build psql image
```
cd ./springboot/psql
docker build -t trading-psql .  #docker builds ./Dokcerfile by default
docker image ls -f reference=trading-psql
```
- build app image 
```
cd ./springboot/
docker build -t trading-app . #docker builds ./Dokcerfile by default
docker image ls -f reference=trading-app
```
 - create docker network
 ```
 docker network create --driver bridge trading-net
 ```
 
  - start containers
  ```
 #Set your environmental variables
 export PSQL_URL="your psql URL"
 export PSQL_USER="your username"
 export PSQL_PASSWORD="your password"
 export IEX_PUB_TOKEN="your_token"
  
 #start psql container
 docker run --name trading-psql-dev \
-e POSTGRES_PASSWORD='$PSQL_PASSWORD' \
-e POSTGRES_DB=jrvstrading \
-e POSTGRES_USER='$PSQL_USERNAME' \
--network trading-net \
-d -p 5432:5432 trading-psql
  
 #start app container
 docker run --name trading-app-dev \
-e "PSQL_URL=${PSQL_URL}" \
-e "PSQL_USER=${PSQL_USER}" \
-e "PSQL_PASSWORD=${PSQL_PASSWORD}" \
-e "IEX_PUB_TOKEN=${IEX_PUB_TOKEN}" \
--network trading-net \
-p 5000:5000 -t trading-app

#list running containers
# you should see two running docker containers
docker container ls
  ```
- Try trading-app with SwaggerUI
![image](/springboot/Swagger.png)

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
Quote controller is used for getting the latest quotes from the IEX Cloud and storing them in the database. There are five services that this controller provides that can be accessed using HTTP requests: 
* GET `/quote/dailyList` - show the daily list of quotes from the Quotes database. This means that all the quotes that are stored in the database will be returned to the user. 
* GET `/quote/iex/ticker/{ticker}` - show a quote from IEX Cloud based on the ticker. The app will make its own request to the IEX API and then return the latest quote to the user. 
* POST `/quote/tickerId/{tickerId}` - add a ticker to the daily list. The quote for the given ticker will be searched using IEX API. If found, the quote with the same ticker in the Quotes database will be updated or saved if no record of this quote exists in the database. 
* PUT `/quote/` - update a quote in the Quotes database. If the record of the quote exists in the database, it will be updated. Otherwise, the quote will be saved. 
* PUT `/quote/iexMarketData` - update quotes in the database with the latest data from IEX Cloud. 
## Trader Controller
Trader controller is used for managing records of traders and their accounts. In addition, using the trader controller, a trader can deposit and withdraw funds from his account. There are five services that this controller provides that can be accessed using HTTP requests:
* POST `/trader/firstname/{firstname}/lastname/{lastname}/dob/{dob}/country/{country}/email/{email}` - used to create a trader record and trader account. Firstly, the trader record is created and stored in the Trader database, it includes personal information of the trader provided in the URL. Then a new zero balance account is created for this trader and stored in the Accounts database. The user gets back a TraderAccountView which contains both trader record and account record for the newly created trader. 
* POST `/trader/` - used to create a trader record and trader account. The information about a trader is taken from the body of the request, unlike in previous POST request. The mechanism of trader account creation and return data is identical to the POST request explained above.
* DELETE `/traderId/{traderId}` - used to delete trader by the given id. This operation involves deleting trader records from the Trader database, associated account record from the Account database and all orders performed by the trader from Orders database. This involves inspection of trader balance. If the balance equals zero, the trader can be successfully removed from the database as well as its account and orders. Otherwise, the error code will be returned. 
*  PUT `/deposit/traderId/{traderId}/amount/{amount}` - used to deposit funds to the trader account with provided id. There is no limit on deposits, however, the trader account must exist in the Account database. 
* PUT `/withdraw/traderId/{traderId}/amount/{amount}` - used to withdraw funds from the trader's account by trader id that is provided. To successfully withdraw money from the account, the user with the given id must exist and the amount to be withdrawn must not be greater than the funds on the trader account balance.
##Order Controller
Order controller is used for performing buy/sell orders. There is one service that is provided by this controller that can be accessed through HTTP request:
* POST `/order/marketOrder` - is used to perform buy or sell operation. The details of an order are coming from the body of the request and include account id, size, and ticker. If the size is positive, that means that the trader is buying stock with the given ticker, if it is negative, then it is a sell order. To sell stocks, the account must have enough stocks of a given type available. To buy stocks, the account must have enough money available. The request to be successful must provide account id that exists in the database, the provided size of the order must be valid and ticker must exist in the database.  

# Docker Deployment
![image](/springboot/DockerDiagram.png)
This diagram shows how the app is deployed using docker. There is a number of steps that must be performed in order to run the app using docker:
1. Run docker daemon
2. The application contains two Dockerfiles that are used to create `trading-psql` and `trading-app` images. For the first image *postgres:9.6-alpine* used as a base image and an SQL script is run that creates four PostgreSQL tables and one view (described in architecture section). The app image is based on *openjdk:8-alpine*. 
3. The network that will be used by future created containers is created next. It is a bridge with a name `trading-net`.
4. From images described above, two containers are created: `trading-psql-dev` and `trading-app-dev`. Connected using a `trading-net` they are used for deploying the application. 

# Improvements
- create an automatic update of tickers at the end of each workday of the stock market
- create a new controller that will provide statistics for a given quote for the past month, week, day. 
- add dashboard controller 
- added more order types
- extended the quote. Now  quote provides limited information (done for training purposes)
