package ep.project.search.model;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
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

    private RestHighLevelClient restHighLevelClient;

    @Autowired
    public ProductRepository(RestHighLevelClient client) {
        this.restHighLevelClient = client;
    }

    /**
     *
     * @param type
     * @param searchValue
     * @return
     * @throws Exception
     */
    public List<Product> searchProduct(String type, String searchValue) throws Exception {
        SearchRequest request = new SearchRequest(INDEX_NAME)
                .types(TYPE_NAME)
                .source(SearchSourceBuilder.searchSource()
                        .query(QueryBuilders.matchQuery(type, searchValue))
                        .from(0)
                        .size(5)
                        .sort(new FieldSortBuilder("lprice").order(SortOrder.DESC)));

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);;
        SearchHits searchHits = response.getHits();

        return getData(searchHits);
    }

    public List<Product> getData(SearchHits searchHits) {

        List<Product> products = new ArrayList<Product>();
        for( SearchHit hit : searchHits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            Product product = new Product();
            product.setProductId((Integer) sourceAsMap.get("product_id"));
            product.setTitle((String) sourceAsMap.get("title"));
            product.setLastBuildDate((String) sourceAsMap.get("last_build_date"));
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
