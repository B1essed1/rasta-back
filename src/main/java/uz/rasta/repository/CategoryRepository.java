package uz.rasta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.rasta.entity.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByKindAndActiveTrueOrderBySortOrder(Category.Kind kind);
    Optional<Category> findByKindAndSlug(Category.Kind kind, String slug);
    boolean existsByKindAndSlug(Category.Kind kind, String slug);
}
