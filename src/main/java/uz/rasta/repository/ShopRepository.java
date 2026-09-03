package uz.rasta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.rasta.entity.Shop;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopRepository extends JpaRepository<Shop, UUID> {
    Optional<Shop> findByHandle(String handle);
    boolean existsByHandle(String handle);
    List<Shop> findByOwnerId(UUID ownerId);
    List<Shop> findByStatus(Shop.ShopStatus status);
}
