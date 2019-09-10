package ep.project.search.repository;

import ep.project.search.model.SearchRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Repository
public class SearchRepository {
    private RestHighLevelClient restHighLevelClient;

    public SearchRepository(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public GetResponse read(String id) throws IOException {
        GetRequest getRequest = new GetRequest("movie","_doc", id);
        GetResponse documentFields = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        return documentFields;
    }

    public RestStatus write(SearchRequest map, String id) throws IOException {
        IndexRequest indexRequest = new IndexRequest("movie","_doc", id);
        Map<String, Map<String, String>> body = new HashMap<>();
        body.put("doc", map.getDoc());
        indexRequest.source(map, XContentType.JSON);
        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        return index.status();
    }
}
