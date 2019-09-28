package ep.project.search.controller;

import ep.project.search.model.Product;
import ep.project.search.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author hwang-yunho on 2019. 9. 20.
 * @project search
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ProductRestController.class)
public class ProductRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    @Test
    public void search() throws Exception {

        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .hprice(711300)
                .image("https://shopping-data-image1")
                .link("https://shopping-data-link1")
                .mallName("mall_name")
                .title("뉴트로지나 노르웨이젼 포뮬러 핸드크림")
                .build());

        given(productService.search("mall_name", "네이버")).willReturn(products);

        mvc.perform(get("/products/_search")
                .param("type", "mall_name")
                .param("searchValue", "네이버"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"title\":\"뉴트로지나 노르웨이젼 포뮬러 핸드크림\"")))
                .andExpect(content().string(containsString("\"title\":\"뉴트로지나 노르웨이젼 포뮬러 핸드크림\"")));
    }
}