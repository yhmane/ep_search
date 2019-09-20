package ep.project.search.model;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hwang-yunho on 2019. 9. 20.
 * @project search
 */
@Repository
public class ProductRepository {

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

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    public ProductRepository(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    public RestHighLevelClient createConnection() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(HOST_NAME,PORT,SCHEME)
                )
        );
    }

    /**
     * 검색 결과를 return
     * 정렬은 우선 낮은가격순으로 함 (추후 수정 예정)
     * @param type (검색 타입)
     * @param search (검색어)
     * @return
     * @throws Exception
     */
    public List<Product> searchProduct(String type, String search) throws Exception {

        RestHighLevelClient client = createConnection();
        SearchRequest request = new SearchRequest(INDEX_NAME)
                .types(TYPE_NAME)
                .source(SearchSourceBuilder.searchSource()
                        .query(QueryBuilders.matchQuery(type, search))
                        .from(0)
                        .size(5)
                        .sort(new FieldSortBuilder("lprice").order(SortOrder.DESC)));


        SearchResponse response = client.search(request, RequestOptions.DEFAULT);;
        SearchHits searchHits = response.getHits();
        client.close();

        return getData(searchHits);
    }

    public List<Product> getData(SearchHits searchHits) {

        List<Product> products = new ArrayList<Product>();
        for( SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Product product = new Product();
            product.setProductId((Integer) sourceAsMap.get("product_id"));
            product.setTitle((String) sourceAsMap.get("title"));
//            product.setLastBuildDate((LocalDateTime) sourceAsMap.get("last_build_date"));
            product.setTotal((Integer) sourceAsMap.get("total"));
            product.setStart((Integer) sourceAsMap.get("start"));
            product.setDisplay((Integer) sourceAsMap.get("display"));
            product.setLink((String) sourceAsMap.get("link"));
            product.setImage((String) sourceAsMap.get("image"));
            product.setLprice((Integer) sourceAsMap.get("lprice"));
            product.setHprice((Integer) sourceAsMap.get("hprice"));
            product.setPmNo((Integer) sourceAsMap.get("pm_no"));
            product.setMallName((String) sourceAsMap.get("mall_name"));
            product.setProductType((Integer) sourceAsMap.get("product_type"));
            products.add(product);
        }

        return products;
    }
}
