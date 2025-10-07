# Lab 2 Web Server -- Project Report

## Description of Changes

### Customize the whitelabel Error Page

The first step was to create a custom error page (error.html) to improve the user experience when an error occurs.
This template is located in `src/main/resources/templates` and its content is automatically displayed by SpringBoot when an error occurs. Moreover, A test was conducted to verify functionality.

### Add a New Endpoint

A Rest endpoint was integrated into the application that returns the current server time in JSON format. The endpoint was already implemented in memory and was used directly in the project. In addition, an automated test was implemented, simulating a request from an external client.

### Enable HTTP/2 and SSL Support

HTTPS was enabled using a self-signed certificate, and HTTP/2 was activated on the Spring Boot server, following the steps outlined in the laboratory report.

1. A self-signed certificate was created and combined into a PKCS#12 file (localhost.p12) according to the lab guide. The file was moved to src/main/resources so that Spring Boot could use it.

2. application.yml was modified with the provided options: enabling SSL, specifying the keystore, setting the password, and activating HTTP/2.

3. The application was manually verified to respond correctly over HTTPS using the following command:
   `curl -k -H "Accept: text/html,*/*;q=0.9" -i https://127.0.0.1:8443/`
4. The /time endpoint was also verified to work correctly:
   `curl -k -i https://127.0.0.1:8443/time`

## Technical Decisions

### Customize the whitelabel Error Page

An automated test was implemented to verify that the error page (error.html) is returned correctly.
In detail:

-   `TestRestTemplate` was used to make an HTTP request.

-   The test checks that the response is 404 Not Found, as well as verifying that the body contains the text ‘!Oops! Page not found’.

-   It is important to note that the header `Accept: text/html `had to be explicitly added to the request to ensure that the server returned the HTML, as its default response is in JSON format.

```kotlin
@Test
    fun `should return error page `() {
        // se ha tenido que pedir en html explicitamente porque sino lo devolvia en Json
        val headers = HttpHeaders()
        headers.accept = listOf(MediaType.TEXT_HTML)

        val entity = HttpEntity<String>(headers)

        val response = restTemplate.exchange(
            "http://localhost:$port",
            HttpMethod.GET,
            entity,
            String::class.java
        )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.body).contains("¡Oops! Página no encontrada")

    }
```

### Add a New Endpoint

This section describes the technical decisions for validating the /time endpoint.
In details:

-   `TestRestTemplate` was used to make an HTTP request.

-   The test explicitly checks that the HTTP response status is 200 OK and that the time variable is not null.

-   Additionally, it verifies that the returned time is earlier than or equal to the current time. Exact equality is not checked due to decimal precision in the time comparison.

```kotlin
fun ` URI time return localtime` (){
        val response = restTemplate.getForEntity("http://localhost:$port/time", TimeDTO::class.java)

        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertNotNull(response.body?.time)
        assertThat(response.body!!.time).isBeforeOrEqualTo(LocalDateTime.now())

    }
```

### Enable HTTP/2 and SSL Support

Regarding technical decisions, the steps outlined in the laboratory report were followed to prevent SSL and HTTP/2 configuration errors.

## Learning Outcomes

Upon completing this exercise, the following knowledge and skills were acquired:

-   Creation of a custom error page and execution of automated tests to verify its functionality.

-   Understanding and configuration of HTTP/2 and SSL, as these technologies had not been used previously.

-   Development of skills in using TestRestTemplate to perform automated testing of REST endpoints.

## AI Disclosure

### AI Tools Used

-   ChatGPT

### AI-Assisted Work

-   ChatGPT was used to write parts of REPORT.md, including the description of changes and technical decisions.
-   ChatGPT was used for guidance on how to implement the error page test, specifically to configure the HTTP request to return the response in HTML using the Accept: text/html header, as Spring Boot returns JSON by default.

-   Approximately 20-25% of the report text was generated with AI assistance, while the rest was written and adapted by the author.

-   All code provided by ChatGPT was manually reviewed and adapted to the project context, ensuring consistency with the lab practice and guidance.

### Original Work

Everything else was developed by the developer following the practical guide:

-   Implementation and validation of the /time endpoint.

-   Creation and customisation of the error page (error.html).

-   Configuration of SSL and HTTP/2 following the steps in the lab report.
