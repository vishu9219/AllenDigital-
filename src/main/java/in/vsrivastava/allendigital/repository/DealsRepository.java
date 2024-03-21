package in.vsrivastava.allendigital.repository;

import in.vsrivastava.allendigital.entity.Deal;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DealsRepository extends CrudRepository<Deal, Integer> {
    @Query("SELECT d FROM Deal d WHERE d.itemNumber=:itemNumber and :time BETWEEN d.startDate AND d.endData")
    List<Deal> findAllDealsDateRange(@Param("time") Long time, @Param("itemNumber") Integer itemNumber);
}
