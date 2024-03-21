package in.vsrivastava.allendigital.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DealRequest {
    private Integer id;

    private Integer itemNumber;

    private Double price;

    private Integer quantity;

    private Long startDate;

    private Long endData;

    private String Status;
}
