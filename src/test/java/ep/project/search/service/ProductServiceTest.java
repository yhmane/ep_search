package ep.project.search.service;

import ep.project.search.model.Product;
import ep.project.search.model.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * @author hwang-yunho on 2019. 9. 20.
 * @project search
 */
public class ProductServiceTest {

    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockProductRepository();
        productService = new ProductService(productRepository);
    }

    private void mockProductRepository() throws Exception{

        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .hprice(711300)
                .image("https://shopping-data-image1")
                .link("https://shopping-data-link1")
                .mallName("mallName")
                .title("뉴트로지나 노르웨이젼 포뮬러 핸드크림")
                .build());

        given(productRepository.searchProduct("mallName", "네이버")).willReturn(products);
    }

    @Test
    public void search() throws Exception {

        List<Product> products = productService.search("mallName", "네이버");

        Product product = products.get(0);
        assertThat(product.getTitle(), is("뉴트로지나 노르웨이젼 포뮬러 핸드크림"));
    }
}