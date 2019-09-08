package ep.project.search.model;

import java.util.Map;

public class SearchRequest {
    private Map<String, String> doc;

    public Map<String, String> getDoc() {
        return doc;
    }

    public void setDoc(Map<String, String> doc) {
        this.doc = doc;
    }
}
