# userDataMS
This project will process the CSV Data and will save in MYSQL Database using POST API. 
Users then can use the GET API to filter the data.
This micro service will also monitor a folder for any new CSV file containg the data of user. Once a file is found then the file will be picked and its data will be validated and processed.

# Getting Started
## Deliverables:
Following will be delivered in the build folder as output of mvn build command.
* Application jar file with name userdatams-0.0.1-SNAPSHOT.jar.
* DB folder containing the configuration file to create db tables.

## Build & Setting Pre-Requisites for Application
* Create DB Schema: 
	* `CREATE SCHEMA 'user_data_db';`
* Building application from code.
	* Clone code from location https://github.com/amankachhal99/userDataMS.git
	* Go to command prompt on the checked out location.
	* If want to run the project in local system where MySQL is also installed on the same system then run following command.
		* `mvn clean install -Plocal`
	* If want to run the project in remote system where MySQL is installed on the different system then make following changes:
		* Update the DB url in file `application-prod.properties`.
		* `mvn clean install -Pprod`

## Features of MicroService
Once the application is started following features will be provided by the MS:
* Exposed End Points:
	* POST http://<IP Address or HOSTNAME>:8080/sales/record
		* User can post the CSV format raw data in the body of the request. Sample Data:
			```
			USER_NAME,AGE,HEIGHT,GENDER,SALE_AMOUNT,LAST_PURCHASE_DATE
			John Doe,29,177,M,21312,2020-11-05T13:15:30Z
			Jane Doe,11,182,F,5342,2019-12-05T13:15:30+08:00
			Jane Doe,32,187,f,5342,2019-12-05T13:15:30+08:00
			```
		* The data will be stored in MySQL DB in table USER_DATA in schema USER_DATA_DB.
		* The data will be validated against the pre-defined rules. The rules are defined in section "Data Validation Rules".
	* GET http://<IP Address or HOSTNAME>:8080/sales/record
		* User can use this endpoint to fetch the data from database.
		* This endpoint supports pagination.
		* This endpoint supports filtering of data by following filters: So following filters can be provided query parameter to fetch the data as per the need
			* `purchaseDateFrom`: all the records with last purchase date greater then provided date will be fetched.
			* `purchaseDateTo`: all the records with last purchase date less or equal to provided date will be fetched.
			* `userName`: all the records matching %provided name% will be fetched. 
			* `age`: all the records with age equal to provided age will be fetched. 
			* `saleAmount`: all the records with sale amount equal to provided sale amount will be fetched. 
			* `height`: all the records with height equal to provided height will be fetched. 
			* `gender`: all the records with gender equal to provided gender will be fetched. 
			* `page`: all the records of specified page will be fetched. 
			* `limit`: this parameter will limit the number of records per page. 
		* If no filter criteria is provided then all the records will be fetched.
* Scheduled Job to Process Data File:
	* MicroService runs a job every 2 min to monitor a folder for any new CSV file containg the data of user. Once a file is found then the file will be picked and its data will be validated and processed. 
	* Location of the monitoring folder is parallel to location of the jar file from where MS is running with name `UserDataFolder`.
	* `success` folder will be created inside `UserDataFolder`. This will contain the file is successfully processed then the file will be moved to success folder.
	* `failure` folder will be created inside `UserDataFolder`. If any error is encountered while processing the file then the file will be moved to failure folder. The error in the file will be logged in the log file. Manual intervention will be required to correct the file and move back to `UserDataFolder`.
* Logging in MS
	* Location of the log file will be parallel to location of the jar file from where MS is running with name `logs`.
	* Log have rotation policy of 10mb. A new file will be created if the size of log file becomes greater to 10mb.
	* Maximum of 10 files will be kept in the logs folder to prevent over consumption of memory in server.
## Data Validation Rules
Following are the validation rules against which the data will be validated.
* CSV data should be in correct order. No other order will be accepted.
'USER_NAME,AGE,HEIGHT,GENDER,SALE_AMOUNT,LAST_PURCHASE_DATE'
* User Name can not be blank.
* Age can not be blank.
* Age can not be negitive value.
* Height can not be blank.
* Height can not be negitive value.
* Gender can not be blank.
* Sale Amount can not be blank.
* Sale Amount can not be negitive value.
* Last Purchase Date should be in UTC format `yyyy-MM-dd'T'HH:mm:ss`

## Guidelines For Usage
Following are the step to be followed to start the application.
* Open command prompt on the jar location. 
* Execute the command to start the jar.
	`java -jar userdatams-0.0.1-SNAPSHOT.jar`
* The application should be able to start itself successfully.Look for below log to confirm that application has started successfully.
`Started UserdatamsApplication in 29.649 seconds`
* Log folder parallel to jar location should be created automatically.
* If `UserDataFolder` is not created then it will be created by MS. 

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/htmlsingle/#using-boot-devtools)
* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/htmlsingle/#boot-features-redis)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/2.3.1.RELEASE/reference/htmlsingle/#howto-execute-liquibase-database-migrations-on-startup)