### How to use this spring-boot project

- Install packages with `mvn clean package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8090/swagger-ui.html
- H2 UI : http://localhost:8090/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.

### General Informations

- Application is developed using Intellij Community version IDE

#### Restrictions
- use java 8

#### what I would have done if I had more time
- Securing endpoints using better authentication and authorization techniques like JWT based
- Adding endpoints for end user to add bulk employees, delete bulk employees and also PATCH endpoints if needed to replace PUT
- Replacing H2 database with MySQL database or other RDBMS database
- Containerization of application and run backend databases using docker
- Add spring profile for various environments
- Add logback xml config based on various environments
- Monitoring applications using Prometheus JMX exporter and Grafana
- Create CI/CD pipeline to deploy applications in cloud environment
- if Java 8 restrictions is not there change the implementation in reactive way


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues

#### Your experience in Java

Please let us know more about your Java experience in a few sentences. For example:

- I have 7 years experience in Java and I started to use Spring Boot from last three years
- I'm a beginner in Spring reactive programming but have good understanding about reactive programming
- I know Spring Boot very well and have been using it for three years
- Good at troubleshooting and fixing bugs
- Good at implementing Java 8 features
- I can handle legacy applications also and have experience in handling Struts based application
- Migrated Struts based web application into Spring application


- Passionate programmer, thirst to learn and build big things

