package api;

public class EndpointBuilder {

    public static String getServiceUrl(String endpoint) {
        return String.format("http://%s:%s/%s",
                PropertiesUtils.getPropertyFromConfigFile("environment"),
                PropertiesUtils.getPropertyFromConfigFile("service_port"),
                endpoint);
    }

    public static String getKinesisUrl() {
        return String.format("http://%s:%s",
                PropertiesUtils.getPropertyFromConfigFile("environment"),
                PropertiesUtils.getPropertyFromConfigFile("kinesis_port"));
    }
}
