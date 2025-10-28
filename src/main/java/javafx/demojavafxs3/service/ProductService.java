package javafx.demojavafxs3.service;

import javafx.demojavafxs3.entity.SonyProduct;

import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface ProductService {
    List<SonyProduct> findAllOrdered();

    SonyProduct save(SonyProduct product);

    SonyProduct update(SonyProduct product);

    void deleteById(Long id);

    Optional<SonyProduct> findById(Long id);

    Map<String, List<SonyProduct>> findTopProductsByStockPerCategory();
}
