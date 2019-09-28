package ep.project.search.controller;

import ep.project.search.model.Product;
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

    private ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products/_search")
    public List<Product> search(
            @RequestParam("type") String type,
            @RequestParam("searchValue") String searchValue) throws Exception {
        return productService.search(type, searchValue);
    }
}
