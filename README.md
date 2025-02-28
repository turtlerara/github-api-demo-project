# GitHub API Demo Project

This Gradle project contains two subprojects -- a GitHub API client and a Spring Boot demo
application. The GitHub API client is portable and can be published to a Maven repository for use in
other applications.

## demo-app

This is a Spring Boot application that serves APIs for retrieving GitHub user data.

**Spring Profiles**

- local
    - Configures the application with settings that expose all Spring Boot Actuator endpoints and
      allows the user to shutdown the app. The app's Swagger page is also available.
- preprod
    - Profile to be used in pre-production environments. Similar to settings applied when using the
      `local` Spring profile but shutting down the app using the Spring Boot Actuator shutdown
      endpoint is not enabled. The app's Swagger page is also available.
- prod
    - Profile to be used in production environments. Spring Boot Actuator endpoints are disabled
      except for the basic health check. The app's Swagger page is not made available.

**APIs**

- GitHub User Data (v1)
    - `GET /api/v1/github-users/{username}`
        - Returns information for the requested GitHub user (by their username) as well as
          repositories they own.
        - The supplied username must meet GitHub's expected username requirements (e.g. max 39
          characters).

### How to Run the Application Locally

By default, the application will start up on localhost:8080. The port can be changed by updating the
`server.port` property in the `application.yaml` file or by passing the same property in as an
argument.

#### Gradle `bootRun` task

The [Spring Boot Gradle Plugin](https://docs.spring.io/spring-boot/gradle-plugin/index.html) applied
to the project adds a `bootRun` task that provides quick access to running the application locally.

```shell
./gradlew bootRun --args='--spring.profiles.active=local'
```

For more information on using and configuring the `bootRun` Gradle
task, [click here](https://docs.spring.io/spring-boot/gradle-plugin/running.html).

#### Command-Line

Below is an example command to start the demo application up with the `local` spring profile active.
You can also use the shell scripts provided in the `bin` directory of this project.

```shell
java -jar ./demo-app/build/libs/demo-app-1.0.jar -Dspring.profiles.active=local
```

### Developer Requirements

- Gradle 8.13
    - This is automatically downloaded locally when using the Gradle Wrapper (`./gradlew`)
- Java 21
- Groovy 4 ([Spock](https://spockframework.org/) testing framework)
- Spring Boot 3.4.x

The demo application project also depends on a few libraries, such
as [Lombok](https://projectlombok.org/) and [MapStruct](https://mapstruct.org/), that help to
generate boilerplate code. Refer to the documentation provided by the developers of both of those
libraries for more information on their use and features. When developing in an IDE like IntelliJ,
you'll want to
enable [annotation processing](https://www.jetbrains.com/help/idea/annotation-processors-support.html)
in the project.

### Swagger UI

When the application is deployed using either the `local` or `preprod' Spring profiles, a **/swagger
endpoint is available that will take you to the [Swagger UI](https://swagger.io/tools/swagger-ui/)
page. This contains helpful API documentation and the ability to quickly test available API
endpoints served by the application.

### Things to Consider

#### GitHub REST API Rate Limiting

For unauthorized requests, GitHub limits the number of HTTP requests that can be made to their REST
API within a specific amount of
time. [Click here](https://docs.github.com/en/rest/using-the-rest-api/rate-limits-for-the-rest-api)
for current information on GitHub's rate limiting and the number of requests that can be made before
the limit is reached.

When the demo application encounters the rate limit, the app will attempt to pull the GitHub user's
data from an internal memory cache. The app will try to continue pulling the same data from the
cache until the rate limit reset time has been reached, and it will also try to not make additional
requests to push the rate limit reset time further into the future.

## github-api-client-spring6

This API client library uses
Spring's [RestClient](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient)
-- introduced in Spring 6.1 -- to facilitate synchronous HTTP requests.

**GitHub API Integrations Available**

- GitHub Users
    - Get information for a GitHub user by their login/username.
- GitHub Repositories
    - Get information for a GitHub user's repositories.

### Using the Client Library

Below is an example of initializing the client factory using default settings and building a
`GitHubUserClient` instance.

```
return new GitHubApiClientFactoryImpl().userClient();
```
