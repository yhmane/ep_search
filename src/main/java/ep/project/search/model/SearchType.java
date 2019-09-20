package ep.project.search.model;

import lombok.*;

/**
 * @author hwang-yunho on 2019. 9. 20.
 * @project search
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchType {
    private String type;
    private String search;
}
