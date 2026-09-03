package uz.rasta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.rasta.entity.Sale;

import java.util.List;
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {
    List<Sale> findByShopIdOrderByCreatedAtDesc(UUID shopId);

    @Query("SELECT COALESCE(MAX(s.saleNo), 0) FROM Sale s WHERE s.shop.id = :shopId")
    int findMaxSaleNoByShopId(UUID shopId);
}
