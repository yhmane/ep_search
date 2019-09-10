package ep.project.search.service;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @author hwang-yunho on 2019. 9. 5.
 * @project search
 */
@Service
public class SearchService {

    @Value("${elasticsearch.index}")
    private String INDEX_NAME;
    @Value("${elasticsearch.type}")
    private String TYPE_NAME;
    @Value("${elasticsearch.hostname}")
    private String HOST_NAME;
    @Value("${elasticsearch.port}")
    private int PORT;
    @Value("${elasticsearch.scheme}")
    private String SCHEME;

    public RestHighLevelClient createConnection() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(HOST_NAME,PORT,SCHEME)
                )
        );
    }

    public Map<String, Object> getOne(String id) throws IOException{

        RestHighLevelClient client = createConnection();

        GetRequest request = new GetRequest( INDEX_NAME, TYPE_NAME, id);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        client.close();

        Map<String, Object> sourceAsMap = response.getSourceAsMap();
        return sourceAsMap;
    }
}
