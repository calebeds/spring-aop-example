package me.calebeoliveira.springaopexample.controller;

import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CustomerControllerIntegrationTest extends IntegrationTestBase {
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = baseUrl;
        RestAssured.basePath = "/api/v1/customer";
    }

    @Test
    @DisplayName("Should return customer with enrichment and orders when all external APIs succeeded")
    void shouldReturnEnrichedCustomerWithOrders() {
        Long customerId = 1L;

        jsonServerMock.stubFor(get(urlEqualTo("/customers/" + customerId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                      "id": 1,
                                      "firstName": "John",
                                      "lastName": "Smith",
                                      "email": "john.smith@example.com",
                                      "phone": "+1 (555) 123-4567",
                                      "age": 32,
                                      "address": {
                                        "street": "123 Main Street",
                                        "city": "New York",
                                        "state": "NY",
                                        "zipCode": "10001",
                                        "country": "USA"
                                      },
                                      "isActive": true,
                                      "registrationDate": "2023-01-15T10:30:00Z",
                                      "loyaltyPoints": 1250
                                }
                                """)));

        jsonServerMock.stubFor(get(urlPathEqualTo("/orders"))
                .withQueryParam("customerId", WireMock.equalTo(customerId.toString()))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type","application/json")
                        .withBody("""
                                 [
                                   {
                                      "id": 1,
                                      "customerId": 1,
                                      "product": "Laptop",
                                      "amount": 1299.99,
                                      "status": "delivered",
                                      "orderDate": "2024-01-20T10:00:00Z"
                                   }
                                 ]
                                """)));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "name": "John Smith"
                        }
                        """)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.OK.value());

        given()
                .pathParam("id", customerId)
                .when()
                .get("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(1))
                .body("name", equalTo("John Smith"))
                .body("email", equalTo("john.smith@example.com"))
                .body("phone", equalTo("+1 (555) 123-4567"))
                .body("orders", notNullValue())
                .body("orders[0].id", equalTo(1))
                .body("orders[0].customerId", equalTo(1))
                .body("orders[0].product", equalTo("Laptop"))
                .body("orders[0].amount", equalTo(1299.99F))
                .body("orders[0].orderDate", equalTo("2024-01-20T10:00:00Z"));
    }
}
