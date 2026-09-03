package uz.rasta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.rasta.entity.ShopConfig;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopConfigRepository extends JpaRepository<ShopConfig, UUID> {
    Optional<ShopConfig> findByShopId(UUID shopId);
}
