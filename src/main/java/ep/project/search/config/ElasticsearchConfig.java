package ep.project.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hwang-yunho on 2019. 9. 23.
 * @project search
 */
@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.hostname}")
    private String HOST_NAME;
    @Value("${elasticsearch.port}")
    private int PORT;
    @Value("${elasticsearch.scheme}")
    private String SCHEME;

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder( new HttpHost(HOST_NAME,PORT,SCHEME)));
        return client;
    }
}
