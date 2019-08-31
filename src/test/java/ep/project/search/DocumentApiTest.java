package ep.project.search;

import org.apache.http.HttpHost;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.RestStatus;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author hwang-yunho on 2019. 8. 31.
 * @project search
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentApiTest {

    @Autowired
    private RestHighLevelClient client;
    // Index명
    private final String INDEX_NAME = "movie_rest";
    // 타입명
    private final String TYPE_NAME = "_doc";
    // 문서 키값
    private final String ID = "1";


    @Before
    public void connection_생성() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("127.0.0.1", 9200, "http")));
    }

    @After
    public void connection_종료() throws IOException {
        client.close();
    }

    @Test
    public void index_테스트1_insert() throws IOException {

        IndexRequest request = new IndexRequest(INDEX_NAME,TYPE_NAME, ID);

        request.source(jsonBuilder()
                .startObject()
                .field("movieCd", "20173732")
                .field("movieNm", "살아남은 아이")
                .field("movieNmEn", "Last Child")
                .endObject()
        );


        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        String id = response.getId();
        RestStatus status = response.status();

        assertThat(id, is("1"));
        assertThat(status, is(RestStatus.CREATED));

    }

    @Test
    public void index_테스트2_get() throws IOException {

        GetRequest request = new GetRequest( INDEX_NAME, TYPE_NAME,ID);

        GetResponse response = client.get(request, RequestOptions.DEFAULT);

        Map<String, Object> sourceAsMap = response.getSourceAsMap();
        String movieCd = (String) sourceAsMap.get("movieCd");
        String movieNm = (String) sourceAsMap.get("movieNm");
        String movieNmEn = (String) sourceAsMap.get("movieNmEn");

        assertThat(movieCd,   is("20173732"));
        assertThat(movieNm,   is("살아남은 아이"));
        assertThat(movieNmEn, is("Last Child"));
    }

    @Test
    public void index_테스트3_update() throws IOException {

        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("createdAt", new Date())
                .field("prdtYear", "2019")
                .field("typeNm", "장편")
                .endObject();

        UpdateRequest request = new UpdateRequest(INDEX_NAME, TYPE_NAME, ID).doc(builder);

        UpdateResponse updateResponse = client.update(request, RequestOptions.DEFAULT);
        RestStatus status = updateResponse.status();

        assertThat(status, is(RestStatus.OK));
    }

    @Test
    public void index_테스트4_delete() throws IOException {

        DeleteRequest request = new DeleteRequest(INDEX_NAME, TYPE_NAME, ID);
        DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);

        RestStatus status = deleteResponse.status();
        assertThat(status, is(RestStatus.OK));
    }
}