package javafx.demojavafxs3.repository;

import javafx.demojavafxs3.entity.SonyCategory;
import javafx.demojavafxs3.entity.SonyProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SonyProductRepository extends JpaRepository<SonyProduct, Long> {
    List<SonyProduct> findAllByOrderByCreatedAtDesc();

    List<SonyProduct> findTop3ByCategoryOrderByStockDesc(SonyCategory category);
}
