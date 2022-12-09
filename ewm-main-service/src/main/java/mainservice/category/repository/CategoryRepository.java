package mainservice.category.repository;

import mainservice.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * FROM categories c " +
            "LEFT JOIN events e on c.id = e.category_id " +
            "WHERE category_id=:id", nativeQuery = true)
    Category findEventLinkedWithCategory(@Param("id") Long catId);
}
