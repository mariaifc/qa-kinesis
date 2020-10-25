# Kinesis Stream Reader messages

The goal of this project is to check if the end point is procucing messsages for the specific Kinesis stream.

## Getting started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes

### Prerequisites
- IntelliJ IDEA
- Java 

If you don't it installed, please visit:

- IntelliJ IDEA : https://www.jetbrains.com/idea/download/#section=mac
- Java : https://www.oracle.com/java/technologies/javase-downloads.html

### Import the project
- Clone the github project
- Open the IDE
- Import the project

### Running the tests locally
- Run the Kinesis service:
You will need Kinesis running on port 4568 and the routing service running on port 9000. There is a
docker compose file that manages the start / link of these two containers. Run only:

```shell script
docker-compose up -d
```

To shutdown, run:

```shell script
docker-compose down
```

Then, you can run the tests: 

In the terminal:

- mvn test -Dcucumber.options="--tag @Sanity"

By the IDE:
- Click on route.feature
- Right button click on "Run Feature route"

### Built with
- Maven - Dependency Management
- Cucumber - BDD Testing Writting Tool
- JUnit - Testing Framework
- RestAssured - Testing REST APIs
