package uz.rasta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.rasta.entity.StockMovement;

import java.util.List;
import java.util.UUID;

@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {
    List<StockMovement> findByShopIdOrderByCreatedAtDesc(UUID shopId);
    List<StockMovement> findByVariantIdOrderByCreatedAtDesc(UUID variantId);
}
