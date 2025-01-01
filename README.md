100ms API Recording Test Automation
This project is designed to automate the testing of the 100ms API related to live streaming, focusing on the functionality for starting and stopping recordings.

Project Structure
src/main/java: Contains the core Java classes for API interaction and test logic.
src/test/java: Contains the test cases for stopping and starting the recording.
src/test/resources/schemas: Contains the JSON schemas for validating API responses.
Requirements
Java 11+
Maven
TestNG
RestAssured
ExtentReports for reporting

Setup
1. Clone the Repository
Clone this repository to your local machine using:

bash
Copy code
git clone https://github.com/yourusername/100ms-api-recording-test.git
2. Install Dependencies
Navigate to the project directory and run the following command to install required dependencies:

bash
Copy code
mvn clean install
3. Configure TestNG
Ensure that your TestNG configuration is correctly set up. The testng.xml file located in the root folder defines the test execution configuration, including test priorities and dependencies.

Example:

xml
Copy code
<suite name="API Test Suite">
    <test name="API Tests">
        <classes>
            <class name="com.example.tests.RecordingTests" />
        </classes>
    </test>
</suite>
Running Tests
To run the tests with Maven, use the following command:

bash
Copy code
mvn test
This will trigger the TestNG tests, and you can view the reports generated in the target/test-output directory.
