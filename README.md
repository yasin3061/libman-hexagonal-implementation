# Libman - Library Management application
This application demonstrates the application of *Hexagonal (or Ports & Adapters) architecture*.

[![Build Status](https://travis-ci.com/yasin3061/libman-hexagonal-implementation.svg?branch=master)](https://travis-ci.com/yasin3061/libman-hexagonal-implementation) ![Jave CI with Maven](https://github.com/yasin3061/libman-hexagonal-implementation/workflows/Java%20CI%20with%20Maven/badge.svg?branch=master) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=yasin3061_libman-hexagonal-implementation&metric=alert_status)](https://sonarcloud.io/dashboard?id=yasin3061_libman-hexagonal-implementation) [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

This application provides basic REST endpoints for managing library (add a new book, reserve
, borrow it). 

The technology behind it: 
* Java 11
* Postgres
* Spring Boot 
* Maven
* Sonar integration

Concepts:
* SOLID principles
* Hexagonal architecture
* Domain Driven Design
* TDD

## Getting started
### Using docker-compose
From the project root directory, run the following command:
```console
$ docker-compose up
```

The `Dockerfile` is optimised in such a way that it will only download the dependencies only if
 the `pom.xml` is changed.

#### Using Maven (with H2 or local Postgres database)

Compile the application:

```console
$ mvn clean package
```

Option 1 - Run it with in-memory database H2 (default profile)

```console
$ mvn spring-boot:run 
```

Option 2 - Use Postgres database instance

Update the configuration file - `src/main/resources/application
.yml` for
 profile *local-postgres* and enter the database details.
 
Update the DB details and run:
```console
$ mvn spring-boot:run -P local-postgres
```
Run tests:
```console
$ mvn test
```

Run integration tests:
```console
$ mvn test-integration
```

## Details of the hexagonal architecture implementation
In a traditional application architecture, we normally have a `web` layer where we usually have
 all the controllers. This `web` layer is then dependent on `service` layer where we have all
  the business logic. The `service` layer then uses the `dao` layer for persistence.
  
### Structure of the application
Hexagonal architecture puts the core application in the center and exposes several `ports`. These
 ports provide interfaces to connect to the application. The external infrastructure code like
  database, web controllers implements the `adapters` in order to connect to these `ports`.
  
Here is how a typical application would look like.

![Ports & Adapters](https://yasinbhojawala.com/wp-content/uploads/Ports-adapters.png)

In terms of our application, here is how it looks like.

![Project structure](https://yasinbhojawala.com/wp-content/uploads/Project-structure-PnA.png)

Instead of `web`, `service`, and `dao` layers, we have `application`, `domain`, and `infrastructure` packages.

The `application` package is responsible for the delivery mechanism of the application. It could be via REST APIs, console-based, or desktop application. In our case, it is REST APIs via `Spring` controllers.

The `infrastructure` is responsible to provide necessary gluing code for the application to run
. In our case, it's the `Spring` bean configuration.

Finally, there is the `domain` which is the core of our application. The code in `domain` is the business code. It does not have any dependency on frameworks or fancy libraries. In fact, just by looking at the `domain` code, no one would be able to tell which framework is used or how the application is delivered.

*Uncle Bob* endorses this idea in his book **Clean Architecture**.

This logic safeguards our business logic from the external world. 

### Dependency inversion
The **D** in **SOLID** talks about *Dependency Inversion*. The Hexagonal architecture promotes that. Let's have a look at the dependency diagram for this application.
![Ports and Adapters - dependency](https://yasinbhojawala.com/wp-content/uploads/Ports-and-Adapters-dependency.png)

The core or the domain of our application does not depend on anything. It's a standalone entity. All the dependencies are inward and none is outward. Here the dependency is *inverted*. This meas that our application need not respond to the external changes.

### The User domain
Let's have a look at an example here. We will begin with the *User* domain as it is the simplest.
![User domain overview](https://yasinbhojawala.com/wp-content/uploads/User-domain-overview.png)

As we can see, the *User* domain consists of three parts `application`, `core`, and
 `infrastructure`.

The `application` contains `UserCommandController` which registers `Spring` controller. The controller uses `AddNewUser` **port** in order to serve the incoming requests.
```java
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCommandController {

    private final AddNewUser addNewUser;

    @PostMapping("")
    public ResponseEntity<String> addNewUser(@RequestBody AddUserCommand addUserCommand) {
        addNewUser.handle(addUserCommand);
        return new ResponseEntity<>("New user was added to library", HttpStatus.CREATED);
    }
}
```
The `AddNewUser` is a simple interface. The `UserDatabaseAdapter` **adapter** implements this interface.
```java
public interface AddNewUser {
    UserIdentifier handle(AddUserCommand addUserCommand);
}
```
The `UserDatabaseAdapter` further uses the `UserRepository` for persistence.
```java
@RequiredArgsConstructor
public class UserDatabaseAdapter implements UserDatabase {

    private final UserRepository userRepository;

    @Override
    public UserIdentifier save(User user) {
        User savedUser = userRepository.save(user);
        return new UserIdentifier(savedUser.getIdentifierAsLong());
    }
}
```
As we can figure out, the `core` does not need to know whether the requests are coming from an API or console. Similarly, it doesn't care about whether the data is saved to RDBMS or file storage.

In fact, just to demonstrate, I have written the database adapter for `borrowing` domain completely differently. Go check it out!

Basically, these `adapters` are interchangeable as long as they *fit in* to the `port`.
![Multiple Adapters](https://yasinbhojawala.com/wp-content/uploads/Multiple-Adapters.png)

You will not find any references to Spring framework inside core. This is not just a recommendation but a requirement.

This can be asserted by unit tests:
```java
@AnalyzeClasses(packages = {"com.yasinbee.libman.hex.domain.user"},
        importOptions = {ImportOption.DoNotIncludeTests.class})
public class UserArchitectureTest {

    @ArchTest
    public static final ArchRule hexagonalArchInUserDomain = onionArchitecture()
            .domainModels("com.yasinbee.libman.hex.domain.user.core.model..")
            .domainServices("com.yasinbee.libman.hex.domain.user..")
            .applicationServices("com.yasinbee.libman.hex.domain.user.application..")
            .adapter("infrastructure", "com.yasinbee.libman.hex.domain.user.infrastructure..");

    @ArchTest
    public static final ArchRule noSpringDependenciesInUserFacade =
            noClass(UserFacade.class)
                    .should()
                    .dependOnClassesThat()
                    .resideInAPackage("org.springframework..");
}
```

The above test asserts that anything in core must not be dependent on the Spring framework.
Source code credits: @wkrzywiec
### Different ports
As we can see, we have two different ports, `incoming` and `outgoing`. The difference is that the requests coming in to the application are served by `incoming` ports. The requests going out of the application are served by the `outgoing` ports.

### CI CD Infrastructure
The CI CD pipeline utilizes automation tools and **infrastructure-as-code** for continuous
 deployment.
 
Here is the [link to the video](https://yasinbhojawala.com/wp-content/uploads/Infrastructure-demo
.webm) explaining the build and deployment pipeline. 


Basically, we push the code to master, Travis will start performing these steps.
* Build & test
* Create docker images
* Push docker images to docker hub

![CI CD Pipeline](https://yasinbhojawala.com/wp-content/uploads/Libman-AWS-Beanstalk-infra-CD-Pipeline.png)

Once Travis pushes the images, it notifies Elasticbeanstalk and the deployment begins.

Here is how the overall setup for the application looks like.
![App infrastructure](https://yasinbhojawala.com/wp-content/uploads/Libman-AWS-Beanstalk-infra-Libman-infrastructure.png)

Nginx is our proxy, and the App is our `Spring` application running on port `8080`. The application
 talks to the RDS instance running Postgres on port `5432`. The port `80` is exposed to the outside
 world. No other ports are exposed.

### Conclusion
I hope this sample gave you rough idea about the practical implementation of the hexagonal
 architecture using `Spring` and `Java`. Finally, we saw how to create an end-to-end CD pipeline
  using Travis, Docker and AWS.
