package com.interviewpedia.spring.cucumber.cucumberglue.product;

import com.interviewpedia.spring.cucumber.product.Product;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class CucumberProductSteps {

    @LocalServerPort
    String port;

    ResponseEntity<Product> responseEntity;

    @DataTableType
    public Product givenProducts(Map<String, String> row) {
        return Product.builder()
                .id(Integer.parseInt(row.get("id")))
                .name(row.get("name"))
                .quantity(Integer.parseInt(row.get("quantity")))
                .price(Double.parseDouble(row.get("price")))
                .build();
    }

    @Given("the following products exists")
    public void givenProducts(DataTable dataTable) {
        List<Product> productList = dataTable.asList(Product.class);
        productList.forEach(p -> new RestTemplate()
                .postForObject("http://localhost:" + port + "/products", p, Product.class));
    }

    @When("the client calls product endpoint {string}")
    public void whenClientCalls(String url) {
        try {
            responseEntity = new RestTemplate()
                    .exchange("http://localhost:" + port + url,
                            HttpMethod.GET,
                            null,
                            Product.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @Then("the response status code is {int}")
    public void thenStatusCode(int expected) {
        Assertions.assertNotNull(responseEntity);
        Assertions.assertNotNull(responseEntity.getStatusCode());
        assertThat("status code is" + expected,
                responseEntity.getStatusCodeValue() == expected);
    }

    @Then("returned product-name should be {string}")
    public void thenStringIs(String expected) {
        Assertions.assertEquals(expected, responseEntity.getBody().getName());
    }


}
