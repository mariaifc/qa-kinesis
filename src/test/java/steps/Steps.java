package steps;

import api.ApiRequest;
import api.EndpointBuilder;
import helpers.KinesisReader;
import io.cucumber.java.en.And;
import io.cucumber.java.en.But;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import java.util.List;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class Steps {

    private static Logger LOGGER = Logger.getLogger(Steps.class.getName());

    private Response response;
    private String uuid;

    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String partialUrl) {
        String urlRequest = EndpointBuilder.getServiceUrl(partialUrl);
        response = ApiRequest.doGetRequest(urlRequest);
    }

    @Then("The response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedCode) {
        assertEquals(expectedCode, response.getStatusCode());
    }

    @Then("The response header should contain {string}")
    public void theResponseHeaderShouldContain(String key) {
        uuid = response.getHeader(key);
        LOGGER.info(String.format("The value for the header key %s is %s", key, uuid));
        assertNotNull("Uuid is null", uuid);
    }

    @And("a message is sent to {string} stream")
    public void aMessageIsSentTo(String stream) {
        List<String> partitionKeys = KinesisReader.getRecordsPartitionsKeys(stream);
        LOGGER.info(String.format("Records for the stream %s: \n%s", stream, String.join("\n", partitionKeys)));

        assertTrue(String.format("Uuid %s does not exist in the records of %s", uuid, stream),
                partitionKeys.stream().anyMatch(uuid::contains));
    }

    @But("a message is not sent to {string} stream")
    public void aMessageIsNotSentToStringStream(String stream) {
        List<String> partitionKeys = KinesisReader.getRecordsPartitionsKeys(stream);
        LOGGER.info(String.format("Records for the stream %s: \n%s", stream, String.join("\n", partitionKeys)));

        assertFalse(String.format("Uuid %s exists in the records of %s", uuid, stream),
                partitionKeys.stream().anyMatch(uuid::contains));
    }
}
