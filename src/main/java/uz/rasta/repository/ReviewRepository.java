package uz.rasta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.rasta.entity.Review;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByShopIdOrderByCreatedAtDesc(UUID shopId);
    List<Review> findByShopIdAndProductIdOrderByCreatedAtDesc(UUID shopId, UUID productId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.shop.id = :shopId")
    Double findAverageRatingByShopId(UUID shopId);
}
