# Prerequisites

In order to be able to submit your assignement, you should have the following installed:
* JDK >= 17
* Maven
* Postman / any other tool that allows you to hit the application's endpoints
* Any versioning tool
* Any IDE that allows you to run the application


# How to

#### Run the application
The application should be run as a SpringBootApplication. Below is a quick guide on how to do that via IntelliJ:
* Edit Configuration 
   * Add New Configuration (Spring Boot)
     * Change the **Main class** to **ing.assessment.INGAssessment**
       * Run the app.

#### Connect to the H2 database
Access the following url: **http://localhost:8080/h2-console/**
 * **Driver Class**: _**org.h2.Driver**_
 * **JDBC URL**: _**jdbc:h2:mem:testdb**_
 * **User Name**: _**sa**_
 * **Password**: **_leave empty_**


#### Implemented Features and Functionalities

1) Manage orders (id, timestamp, list of product ids, cost of order, delivery time, delivery cost)
I have implemented three APIs for order management:
- Place an Order: Allows users to create a new order by providing a list of products. I implemented methods to compute 
the total cost, delivery time, and delivery cost.
- Get Order Details: Retrieves detailed information about a specific order based on its ID
- Delete an Order: Allows users to cancel an order within 2 minutes of placement.

2) Add validations as you see fit
I have implemented several validation rules to prevent incorrect actions. I will list some of them:
- Order must contain at least one product.
- Product quantity must be greater than zero.
- Order cost must be greater than zero.
- Duplicate productId found
- Insufficient stock for product ID 
- Orders can only be canceled within the first 2 minutes after placement.

3) Design error mechanism and handling (e.g. Out of stock, Invalid order)
- I have implemented a Validator that checks for invalid order conditions. 
- If an issue is detected, the validator throws a ValidatorException, a custom class which extends RuntimeException.
- I have also created a dedicated class, ValidatorErrorCodes, to store error messages.

4) Write unit tests, at least for one class 
I have implemented unit tests to prevent regressions as new features are added. The tests cover:
- Verifying that order cost, delivery time, and delivery cost are correctly determined.
- Ensuring that fetching an order by ID returns the expected details.
- API testing – Validating the behavior of the Get Order Details, Delete an order,
and Place an Order endpoints to confirm that they handle requests correctly and return the appropriate responses.

5) Use Java 17+ features
- I have leveraged Java 17 features to enhance the error handling mechanism.
- The ValidatorException class is now a sealed class, with two permitted subtypes:
- OutOfStockError: Thrown when a product is out of stock.
- InvalidOrderError: Thrown when an order fails validation. 


#### Additional features I would consider
- Stock Restoration After Order Deletion: After an order is deleted, the stock for the ordered products should be 
restored to its original quantity to ensure accurate inventory tracking.
- Concurrency Handling for Order Placement: This would ensure that if two users attempt to place the same order at the 
same time and the stock is insufficient, only one of them would succeed.


#### Note regarding commented Lombok annotations (@NoArgsConstructor, @AllArgsConstructor)
I encountered issues with object creation, so I commented out these annotations and manually handled the constructor logic.
