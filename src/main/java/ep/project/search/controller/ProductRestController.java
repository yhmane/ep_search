package ep.project.search.controller;

import ep.project.search.model.Product;
import ep.project.search.model.SearchType;
import ep.project.search.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hwang-yunho on 2019. 9. 20.
 * @project search
 */
@RestController
public class ProductRestController {

    @Autowired
    private ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/_search")
    public List<Product> searchProduct(@RequestParam String type, @RequestParam String search) throws Exception {
        SearchType searchType = SearchType.builder()
                .type(type)
                .search(search)
                .build();
        return productService.search(searchType);
    }
}
