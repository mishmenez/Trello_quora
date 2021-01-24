Thisproject is similar to Quora. Follow the directory structure given in the project stub file. The main module is divided into three sub-modules —  quora-api, quora-db, and quora-service.

 

1. quora-api

config - This directory must consist of all the required configuration files of the project (if any). We have already provided swagger config file in the stub.
controller - This directory must consist of all the controller classes required for the project (the list of required controllers along with the API endpoints are listed in the next segment).

exception - This directory must consist of the exception handlers for all the exceptions. You have to implement the code for exception handler for all the exceptions to be implemented in the project.

endpoints - This directory consists of the JSON files which are used to generate the Request and Response models.

test - This directory consists of tests for all the controller classes. You need to uncomment all the given test cases to run these test cases after implementing the project.

 

2. quora-db

config - This directory consists of the database properties and environment properties for local development.
sql - This directory consists of all the SQL queries to create database schema tables.
 

3. quora-service

business - This directory must consist of all the implementations of the business logic of the application.
dao - This directory allows us to isolate the application/business layer from the persistence layer and must consist of the implementation of all the data access object classes.
entity - This directory must consist of all the entity classes related to the project to map these class objects with the database. You need to observe the database schema and all the constraints given in SQL files carefully to map Java objects with the database.
exception - This directory consists of all the exceptions related to the project. All the exceptions required for the project have been implemented in the stub file.
 

Authentication and Authorization
The authentication functionality should be implemented in such a way that the API endpoints are accessible only when a user successfully logs in to the endpoint '/user/signin'. In such a case, the user should be able to subsequently access the endpoints by providing only the access token to each endpoint. Also, the user will have access to the endpoints defined in the 'AdminController' class based on the role information provided in the database, as defined below:

If the role of a user is 'admin', the user will be able to access all the API endpoints in the web application.

If the role of a user is 'nonadmin', the user cannot access the API endpoints defined in the AdminController class; he/she can, however, access the rest of the API endpoints defined in the other controller classes.

 

Spring, Unit Testing, and Mocking
In the stub file provided, we have written the unit tests for the controllers of this project. You can read the tests and understand what those tests are trying to accomplish. You need to have all the records stored in the database given in "quora_test.sql" file before running the test cases. These records will be stored in the database by activating the profile setup using the command "mvn clean install -Psetup" in the quora-db folder of the project. You also need to uncomment the code written for all the test classes before running all the test cases in the test directory.

 

HTTP status
As you have already learnt about different HTTP response status codes, implement the same when creating the API endpoints and return the corresponding HTTP status code based on the functionality or message. The most commonly used response codes in this project are as follows:

HttpStatus.OK

HttpStatus.CREATED

HttpStatus.UNAUTHORIZED

HttpStatus.FORBIDDEN

HttpStatus.NOT_FOUND