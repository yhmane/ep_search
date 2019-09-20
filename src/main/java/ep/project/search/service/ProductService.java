package ep.project.search.service;

import ep.project.search.model.Product;
import ep.project.search.model.ProductRepository;
import ep.project.search.model.SearchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hwang-yunho on 2019. 9. 20.
 * @project search
 */
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> search(SearchType searchType) throws Exception {
        return productRepository.searchProduct(searchType.getType(), searchType.getSearch());
    }
}
