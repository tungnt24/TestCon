package viettel.vcs.testcontainer.containers;


import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;

import org.elasticsearch.client.RestClient;
import org.junit.After;

import org.junit.Test;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import org.testcontainers.utility.DockerImageName;


import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestElasticsearchContainer {

    /**
     * Elasticsearch version which should be used for the Tests
     */
    private static final String ELASTICSEARCH_VERSION = "7.9.2";

    private static final DockerImageName ELASTICSEARCH_IMAGE = DockerImageName
        .parse("docker.elastic.co/elasticsearch/elasticsearch")
        .withTag(ELASTICSEARCH_VERSION);

    /**
     * Elasticsearch default username, when secured
     */
    private static final String ELASTICSEARCH_USERNAME = "elastic";

    /**
     * From 6.8, we can optionally activate security with a default password.
     */
    private static final String ELASTICSEARCH_PASSWORD = "changeme";

    private RestClient client = null;

    private RestClient anonymousClient = null;

    @After
    public void stopRestClient() throws IOException {
        if (client != null) {
            client.close();
            client = null;
        }
        if (anonymousClient != null) {
            anonymousClient.close();
            anonymousClient = null;
        }
    }

    @Test
    public void testStart() throws IOException {
        try (ElasticsearchContainer container = new ElasticsearchContainer(ELASTICSEARCH_IMAGE)) {
            // Start the container. This step might take some time...
            container.start();
            assertTrue(container.isRunning());
            // Do whatever you want with the rest client ...
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(ELASTICSEARCH_USERNAME, ELASTICSEARCH_PASSWORD)
            );

            client =
                RestClient
                    .builder(HttpHost.create(container.getHttpHostAddress()))
                    .setHttpClientConfigCallback(httpClientBuilder -> {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    })
                    .build();

            Response response = client.performRequest(new Request("GET", "/_cluster/health"));
            assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
        }
    }
}