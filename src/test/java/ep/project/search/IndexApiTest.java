package ep.project.search;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author hwang-yunho on 2019. 8. 30.
 * @project search
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexApiTest {

    @Autowired
    private RestHighLevelClient client;

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
    public void index_생성() throws IOException {

        // Index명
        String INDEX_NAME = "movie_rest";

        // 타입명
        String TYPE_NAME = "_doc";

        // 매핑정보
        XContentBuilder indexBuilder = jsonBuilder()
                .startObject()
                .startObject(TYPE_NAME)
                .startObject("properties")
                .startObject("movieCd")
                .field("type", "keyword")
                .field("store", "true")
                .field("index_options", "docs")
                .endObject()
                .startObject("movieNm")
                .field("type", "text")
                .field("store", "true")
                .field("index_options", "docs")
                .endObject()
                .startObject("movieNmEn")
                .field("type", "text")
                .field("store", "true")
                .field("index_options", "docs")
                .endObject()
                .endObject()
                .endObject()
                .endObject();

        // 매핑 설정
        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
        request.mapping(TYPE_NAME, indexBuilder);

        // Alias 설정
        String ALIAS_NAME = "moive_auto_alias";
        request.alias(new Alias(ALIAS_NAME));

        // 인덱스 생성
        boolean acknowledged = client.indices()
                .create(request, RequestOptions.DEFAULT)
                .isAcknowledged();

        assertThat(acknowledged, is(true));
    }

    @Test
    public void index_삭제() throws IOException {
        // Index명
        String INDEX_NAME = "movie_rest";


        // 인덱스 삭제
        DeleteIndexRequest request = new DeleteIndexRequest(INDEX_NAME);

        boolean acknowledged = client.indices()
                .delete(request, RequestOptions.DEFAULT)
                .isAcknowledged();
        assertThat(acknowledged, is(true));
    }
}
