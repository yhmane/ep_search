package ep.project.search.model;

import com.google.gson.Gson;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        ;

        return getData(response.getHits());
    }

    public List<Product> getData(SearchHits searchHits) {

        return Stream.of(searchHits.getHits()).parallel().map(hit -> {
            return new Gson().fromJson(hit.getSourceAsString(), Product.class);
        }).collect(Collectors.toList());
    }
}
