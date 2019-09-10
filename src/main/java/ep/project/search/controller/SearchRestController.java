package ep.project.search.controller;

import ep.project.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * @author hwang-yunho on 2019. 9. 5.
 * @project search
 */
@RestController
public class SearchRestController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/movie_rest/_doc/{id}")
    public String get(@PathVariable String id) throws IOException {
        Map<String, Object> map = searchService.getOne(id);
        String movieCd = (String) map.get("movieCd");
        String movieNm = (String) map.get("movieNm");
        String movieNmEn = (String) map.get("movieNmEn");
        return movieCd + "/" + movieNm + "/" + movieNmEn;
    }

    @PostMapping("/movie_rest/_doc/{id}")
    public String post(@PathVariable String id) {
        System.out.println(id);
        return "";
    }

    @PutMapping("/movie_rest/_doc/{id}")
    public String put(@PathVariable String id) {
        System.out.println(id);
        return "";
    }

    @DeleteMapping("/movie_rest/_doc/{id}")
    public String delete(@PathVariable String id) {
        System.out.println(id);
        return "";
    }

    @PatchMapping("/movie_rest/_doc/{id}")
    public String patch(@PathVariable String id) {
        System.out.println(id);
        return "";
    }
}
