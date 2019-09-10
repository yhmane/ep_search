package ep.project.search;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.close.CloseIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.open.OpenIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IndexApiTest {

    @Autowired
    private RestHighLevelClient client;
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
    @Value("${elasticsearch.aliasname}")
    private String ALIAS_NAME = "moive_auto_alias";

    @Before
    public void connection_생성() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(HOST_NAME, PORT, SCHEME)));
    }

    @After
    public void connection_종료() throws IOException {
        client.close();
    }

    @Test
    public void index_테스트1_생성() throws IOException {

        // 매핑 설정
        CreateIndexRequest request = new CreateIndexRequest()
                .index(INDEX_NAME)
                .mapping(TYPE_NAME, jsonBuilder()
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
                        .endObject())
                .settings(
                        Settings.builder()
                                .put("index.number_of_shards", 5)
                                .put("index.number_of_replicas", 2))
                .alias(new Alias(ALIAS_NAME));

        boolean acknowledged = client.indices()
                .create(request, RequestOptions.DEFAULT)
                .isAcknowledged();

        assertThat(acknowledged, is(true));
    }

    @Test
    public void index_테스트2_닫기() throws IOException{

        CloseIndexRequest requestClose = new CloseIndexRequest(INDEX_NAME);

        boolean acknowledged  = client.indices().close(requestClose, RequestOptions.DEFAULT).isAcknowledged();

        assertThat(acknowledged, is(true));
    }

    @Test
    public void index_테스트3_오픈() throws IOException{

        OpenIndexRequest requestOpen = new OpenIndexRequest(INDEX_NAME);

        boolean acknowledged  = client.indices().open(requestOpen, RequestOptions.DEFAULT).isAcknowledged();

        assertThat(acknowledged, is(true));
    }

    @Test
    public void index_테스트4_삭제() throws IOException {

        DeleteIndexRequest request = new DeleteIndexRequest(INDEX_NAME);

        boolean acknowledged = client.indices()
                .delete(request, RequestOptions.DEFAULT)
                .isAcknowledged();

        assertThat(acknowledged, is(true));
    }
}
