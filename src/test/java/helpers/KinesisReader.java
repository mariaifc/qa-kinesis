package helpers;

import api.EndpointBuilder;
import api.PropertiesUtils;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KinesisReader {

    public static List<Record> getRecords(String stream) {

        System.setProperty("com.amazonaws.sdk.disableCbor", "true");

        String kinesisEndpoint = EndpointBuilder.getKinesisUrl();

        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new
                AwsClientBuilder.EndpointConfiguration(kinesisEndpoint, Regions.US_EAST_1.getName());

        AWSCredentials awsCreds = new BasicAWSCredentials(PropertiesUtils.getPropertyFromConfigFile("access_id"),
                PropertiesUtils.getPropertyFromConfigFile("secret_key"));

        AmazonKinesisClientBuilder clientBuilder = AmazonKinesisClientBuilder.standard();
        clientBuilder.withCredentials(new AWSStaticCredentialsProvider(awsCreds));
        clientBuilder.setEndpointConfiguration(endpointConfiguration);

        AmazonKinesis client = clientBuilder.build();

        ListShardsRequest listShardsRequest = new ListShardsRequest();
        listShardsRequest.setStreamName(stream);
        Shard shard = client.listShards(listShardsRequest).getShards().get(0);

        GetShardIteratorResult shardIterator = client.getShardIterator(stream, shard.getShardId(),
                "TRIM_HORIZON");
        String myShardIterator = shardIterator.getShardIterator();

        return readRecords(client, myShardIterator);
    }

    private static List<Record> readRecords(AmazonKinesis client, String shardIterator) {
        List<Record> records = new ArrayList<>();

        while (records.isEmpty()) {

            GetRecordsRequest getRecordsRequest = new GetRecordsRequest();
            getRecordsRequest.setShardIterator(shardIterator);
            getRecordsRequest.setLimit(25);

            GetRecordsResult result = client.getRecords(getRecordsRequest);

            records = result.getRecords();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                throw new RuntimeException(exception);
            }

            shardIterator = result.getNextShardIterator();
        }

        return records;
    }

    public static List<String> getRecordsPartitionsKeys(String stream) {
        return getRecords(stream).stream().map(Record::getPartitionKey).collect(Collectors.toList());
    }

}
