package in.vsrivastava.allendigital.controller;

import in.vsrivastava.allendigital.entity.Deal;
import in.vsrivastava.allendigital.repository.DealsRepository;
import in.vsrivastava.allendigital.request.DealRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
public class DealsController {

    private final DealsRepository dealsRepository;

    public DealsController(DealsRepository dealsRepository) {
        this.dealsRepository = dealsRepository;
    }

    @PutMapping("/deal")
    public ResponseEntity<DealRequest> createDeal(@RequestBody DealRequest dealRequest) {
        if (dealRequest.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DealRequest.builder().Status("API is to create "
                    + "not update").build());
        }
        Deal deal =
                Deal.builder().itemNumber(dealRequest.getItemNumber()).price(dealRequest.getPrice()).startDate(dealRequest.getStartDate()).endData(dealRequest.getEndData()).quantity(dealRequest.getQuantity()).build();
        Deal save = dealsRepository.save(deal);
        DealRequest dealResponse =
                DealRequest.builder().id(save.getId()).itemNumber(save.getItemNumber()).price(save.getPrice()).quantity(save.getQuantity()).startDate(save.getStartDate()).endData(save.getEndData()).build();
        return ResponseEntity.status(HttpStatus.OK).body(dealResponse);
    }

    @PostMapping("/deal")
    public ResponseEntity<DealRequest> updateDeal(@RequestBody DealRequest dealRequest) {
        if (dealRequest.getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DealRequest.builder().Status("API is to update "
                    + "not create").build());
        }
        Deal deal =
                Deal.builder().itemNumber(dealRequest.getItemNumber()).price(dealRequest.getPrice()).startDate(dealRequest.getStartDate()).endData(dealRequest.getEndData()).id(dealRequest.getId()).quantity(dealRequest.getQuantity()).build();
        Deal save = dealsRepository.save(deal);
        DealRequest dealResponse =
                DealRequest.builder().id(save.getId()).itemNumber(save.getItemNumber()).price(save.getPrice()).quantity(save.getQuantity()).startDate(save.getStartDate()).endData(save.getEndData()).build();
        return ResponseEntity.status(HttpStatus.OK).body(dealResponse);
    }

    @GetMapping("/deal")
    public ResponseEntity<List<Deal>> getDeals() {
        Iterable<Deal> all = dealsRepository.findAll();
        List<Deal> deals = new LinkedList<>();
        all.forEach(deals::add);
        return ResponseEntity.status(HttpStatus.OK).body(deals);
    }

    @PostMapping("/endDeal/{id}")
    public ResponseEntity<DealRequest> endDeal(@PathVariable("id") Integer id) {
        Optional<Deal> byId = dealsRepository.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DealRequest.builder().Status("Deal not found").build());
        }
        Deal deal = byId.get();
        deal.setEndData(Instant.now().minus(Duration.ofMinutes(1)).getEpochSecond());
        Deal save = dealsRepository.save(deal);
        DealRequest dealResponse =
                DealRequest.builder().id(save.getId()).itemNumber(save.getItemNumber()).price(save.getPrice()).quantity(save.getQuantity()).startDate(save.getStartDate()).endData(save.getEndData()).build();
        return ResponseEntity.status(HttpStatus.OK).body(dealResponse);
    }

    @PostMapping("/claimDeal/{itemNumber}/{quantity}")
    @Transactional
    public ResponseEntity<DealRequest> claimDeal(@PathVariable("itemNumber") Integer itemNumber, @PathVariable(
            "quantity") Integer quantity) {
        long epochSecond = Instant.now().getEpochSecond();
        List<Deal> allDealsDateRange = dealsRepository.findAllDealsDateRange(epochSecond, itemNumber);
        if (allDealsDateRange.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(DealRequest.builder().Status("Deal for the item " + "not found").build());
        }
        Deal deal = allDealsDateRange.get(0);
        if (deal.getQuantity() < quantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DealRequest.builder().Status("Requested " +
                    "Quantity is more than the available Quantity").build());
        }
        deal.setQuantity(deal.getQuantity() - quantity);
        Deal save = dealsRepository.save(deal);
        DealRequest dealResponse =
                DealRequest.builder().id(save.getId()).itemNumber(save.getItemNumber()).price(save.getPrice()).quantity(save.getQuantity()).startDate(save.getStartDate()).endData(save.getEndData()).Status("Deal Processed Successfully").build();
        return ResponseEntity.status(HttpStatus.OK).body(dealResponse);
    }
}
