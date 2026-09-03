package uz.rasta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.rasta.entity.Product;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByShopIdOrderBySortOrderAsc(UUID shopId);
    List<Product> findByShopIdAndVisibleTrueOrderBySortOrderAsc(UUID shopId);
}
