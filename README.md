# Welcome to KVK's Rest API POC Assignment!

Hi! I'm Venkat. Here is the sample Rest API POC developed for the assignment.

## Assumptions


## Tech Stack
- Java 10
- SpringBoot
- Database - H2 in memory db running in localhost
- Caching - Redis (Should be running in localhost with port 6379) 
- Security - HTTP Basic Authentication with Spring Security
- Rest - JAX-RS 
- Documentation - Swagger 2.0

## Sample Data
> ### Accounts

| ID               |AUTH_ID						 |USERNAME					   |
|------------------|-----------------------------|-----------------------------|
|1|acc1            |acc1           |
|2|acc2            |acc2           |
|3|acc3            |acc3           |
|4|acc4            |acc4           |

> ### Account Phones

| ID               |AUTH_ID						   |NUMBER                       |
|------------------|-------------------------------|-----------------------------|
|5|acc1            |9966000001|
|6|acc2            |9966000001|
|7|acc2            |9966000002|

## Live Demo

Need to update

## How to run locally

> Prerequisites
- Install Java 10
- Install Redis  - Can be downloaded from here https://github.com/dmajkic/redis/downloads
- Ports 8080, 6379 should be available free in localhost, to run the application & Redis respectively.

> Steps for Windows
1. Clone/download the code
2. Go to the project root directory
3. run the command <i>mvnw spring-boot : run</i>
4. Open the browser access the url http://localhost:8080 you will be able to can see the API documentation

> Building the source code
We need to install the maven to build the source code
Install maven and configure the bin folder to PATH environment variable
From project root run command <i>mvn clean install</i>

> H2 Database Console
http://localhost:8080/h2-console - No password required to login, just click on connect
