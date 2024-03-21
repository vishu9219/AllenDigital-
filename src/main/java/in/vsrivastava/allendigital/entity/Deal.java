package in.vsrivastava.allendigital.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {@Index(name = "idx_itemNumber_startDate_endDate", columnList = "itemNumber,startDate,endData"),
        @Index(name = "unique_itemNumber", columnList = "itemNumber", unique = true)})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deal {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer itemNumber;

    private Double price;

    private Integer quantity;

    private Long startDate;

    private Long endData;
}
