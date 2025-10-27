package javafx.demojavafxs3.service.impl;

import javafx.demojavafxs3.entity.SonyCategory;
import javafx.demojavafxs3.repository.SonyCategoryRepository;
import javafx.demojavafxs3.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final SonyCategoryRepository categoryRepository;

    public CategoryServiceImpl(SonyCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<SonyCategory> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<SonyCategory> findById(Integer id) {
        return categoryRepository.findById(id);
    }
}
