package ep.project.search.controller;

import ep.project.search.model.SearchRequest;
import ep.project.search.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class SearchController {
    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search/{searchWord}")
    public String helloWorld(@PathVariable String searchWord) throws IOException {
        return searchService.searchWord(searchWord);
    }

    @PostMapping("/search/{id}")
    public int write(SearchRequest body, @PathVariable String id) throws IOException {
        return searchService.write(body, id);
    }


}

