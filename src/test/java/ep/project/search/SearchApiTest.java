package ep.project.search;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author hwang-yunho on 2019. 9. 19.
 * @project search
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SearchApiTest {

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
    // 아이디
    private String ID = "1";

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
    public void search_테스트1_match_all_쿼리 () throws IOException {

        SearchRequest request = new SearchRequest(INDEX_NAME)
                .types(TYPE_NAME)
                .source(SearchSourceBuilder.searchSource()
                        .query(QueryBuilders.matchAllQuery())
                        .from(0)
                        .size(5)
                        .sort(new FieldSortBuilder("movieCd").order(SortOrder.DESC)));

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);;
        SearchHits searchHits = response.getHits();;

        checkData(searchHits);
    }

    @Test
    public void search_테스트2_match_쿼리 () throws IOException {

        SearchRequest request = new SearchRequest(INDEX_NAME)
                .types(TYPE_NAME)
                .source(SearchSourceBuilder.searchSource()
                        .query(QueryBuilders.matchQuery("movieNm", "아이"))
                        .from(0)
                        .size(5)
                        .sort(new FieldSortBuilder("movieCd").order(SortOrder.DESC)));

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);;
        SearchHits searchHits = response.getHits();;

        checkData(searchHits);
    }

    @Test
    public void search_테스트3_common_terms_쿼리 () throws IOException {

        SearchRequest request = new SearchRequest(INDEX_NAME)
                .types(TYPE_NAME)
                .source(SearchSourceBuilder.searchSource()
                        .query(QueryBuilders.commonTermsQuery("movieNm", "아이"))
                        .from(0)
                        .size(5)
                        .sort(new FieldSortBuilder("movieCd").order(SortOrder.DESC)));

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);;
        SearchHits searchHits = response.getHits();;

        checkData(searchHits);
    }

    @Test
    public void search_테스트4_term_쿼리 () throws IOException {

        SearchRequest request = new SearchRequest(INDEX_NAME)
                .types(TYPE_NAME)
                .source(SearchSourceBuilder.searchSource()
                        .query(QueryBuilders.termQuery("movieCd", "20173732"))
                        .from(0)
                        .size(5)
                        .sort(new FieldSortBuilder("movieCd").order(SortOrder.DESC)));

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);;
        SearchHits searchHits = response.getHits();;

        checkData(searchHits);
    }

    @Test
    public void search_테스트5_querystring_쿼리 () throws IOException {

        SearchRequest request = new SearchRequest(INDEX_NAME)
                .types(TYPE_NAME)
                .source(SearchSourceBuilder.searchSource()
                        .query(QueryBuilders.queryStringQuery("+아이 -어른").field("movieNm"))
                        .from(0)
                        .size(5)
                        .sort(new FieldSortBuilder("movieCd").order(SortOrder.DESC)));

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);;
        SearchHits searchHits = response.getHits();;

        checkData(searchHits);
    }

    @Test
    public void search_테스트6_bool_쿼리 () throws IOException {

        SearchRequest request = new SearchRequest(INDEX_NAME)
                .types(TYPE_NAME)
                .source(SearchSourceBuilder.searchSource()
                        .query(QueryBuilders.boolQuery()
                                //.must(QueryBuilders.termQuery("movieCd", "value"))
                                //.mustNot(QueryBuilders.termQuery("keywordField2", "value2"))
                                .should(QueryBuilders.termQuery("movieNm", "아이")))
                                //.filter(QueryBuilders.termQuery("keywordField4", "value4")))
                        .from(0)
                        .size(5)
                        .sort(new FieldSortBuilder("movieCd").order(SortOrder.DESC)));

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);;
        SearchHits searchHits = response.getHits();;

        checkData(searchHits);
    }

    public void checkData(SearchHits hits) {
        SearchHit hit = hits.getAt(0);
        Map<String, Object> sourceAsMap = hit.getSourceAsMap();

        String movieCd = (String) sourceAsMap.get("movieCd");
        String movieNm = (String) sourceAsMap.get("movieNm");
        String movieNmEn = (String) sourceAsMap.get("movieNmEn");

        assertThat(movieCd,   is("20173732"));
        assertThat(movieNm,   is("살아남은 아이"));
        assertThat(movieNmEn, is("Last Child"));
    }
}
