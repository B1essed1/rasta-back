package uz.rasta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.rasta.entity.Order;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByShopIdOrderByCreatedAtDesc(UUID shopId);

    @Query("SELECT COALESCE(MAX(o.orderNo), 0) FROM Order o WHERE o.shop.id = :shopId")
    int findMaxOrderNoByShopId(UUID shopId);
}
