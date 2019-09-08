package ep.project.search.service;

import ep.project.search.model.SearchRequest;
import ep.project.search.repository.SearchRepository;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.rest.RestStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class SearchService {
    private SearchRepository searchRepository; //의존성

    public SearchService(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public String searchWord(String searchWord) throws IOException {
        GetResponse read = searchRepository.read(searchWord);
        Map<String, Object> source = read.getSource();
        Object value = ((Map)source.get("doc")).get("fieldName");
        return value.toString();
    }

    public int write(SearchRequest map, String id) throws IOException {
        RestStatus write = searchRepository.write(map, id);
        return write.getStatus();

    }
}
