package ep.project.search.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author hwang-yunho on 2019. 9. 20.
 * @project search
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    private Integer productId;
    private Integer hprice;
    private String image;
    private String link;
    private Integer lprice;
    private String mallName;
    private Integer productType;
    private String title;
    private Integer pmNo;
    private Integer display;
    private String lastBuildDate;
    private Integer start;
    private Integer total;
}
