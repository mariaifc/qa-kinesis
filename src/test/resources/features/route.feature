Feature: Route endpoint
@Sanity
  Scenario: When routing odd numbers a message is sent to li-stream-odd stream
    When I send a GET request to "route/1"
    Then The response status code should be 200
    And The response header should contain "X-Transaction-Id"
    And a message is sent to "li-stream-odd" stream
    But a message is not sent to "li-stream-even" stream

  Scenario: When routing seed as zero a message is sent to li-stream-odd stream
    When I send a GET request to "route/0"
    Then The response status code should be 200
    And The response header should contain "X-Transaction-Id"
    And a message is sent to "li-stream-even" stream
    But a message is not sent to "li-stream-odd" stream

  Scenario: When routing odd numbers a message is sent to li-stream-even stream
    When I send a GET request to "route/2"
    Then The response status code should be 200
    And The response header should contain "X-Transaction-Id"
    And a message is sent to "li-stream-even" stream
    But a message is not sent to "li-stream-odd" stream

  Scenario: When routing empty seed service must return a not found request
    When I send a GET request to "route/"
    Then The response status code should be 404

  Scenario: When routing non numbers service must return a bad request
    When I send a GET request to "route/null"
    Then The response status code should be 400
