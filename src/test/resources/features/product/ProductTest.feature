Feature: Create Products

  Scenario: Create 2 products
    Given the following products exists
      | id | name            | quantity | price |
      | 1  | TV              | 70       | 50000 |
      | 2  | Fridge          | 90       | 25000 |
      | 3  | Washing Machine | 70       | 50000 |
      | 4  | AC              | 48       | 35000 |
    When the client calls product endpoint "/products/4"
    Then the response status code is 200
    And returned product-name should be "AC"

