package javafx.demojavafxs3.repository;

import javafx.demojavafxs3.entity.SonyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SonyCategoryRepository extends JpaRepository<SonyCategory, Integer> {
}
