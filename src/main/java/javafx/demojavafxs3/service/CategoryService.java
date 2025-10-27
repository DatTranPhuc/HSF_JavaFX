package javafx.demojavafxs3.service;

import javafx.demojavafxs3.entity.SonyCategory;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<SonyCategory> findAll();

    Optional<SonyCategory> findById(Integer id);
}
