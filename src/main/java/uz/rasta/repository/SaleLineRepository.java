package uz.rasta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.rasta.entity.SaleLine;

import java.util.List;
import java.util.UUID;

@Repository
public interface SaleLineRepository extends JpaRepository<SaleLine, UUID> {
    List<SaleLine> findBySaleId(UUID saleId);
}
