package javafx.demojavafxs3.service.impl;

import javafx.demojavafxs3.entity.SonyCategory;
import javafx.demojavafxs3.entity.SonyProduct;
import javafx.demojavafxs3.repository.SonyProductRepository;
import javafx.demojavafxs3.service.CategoryService;
import javafx.demojavafxs3.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final SonyProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductServiceImpl(SonyProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SonyProduct> findAllOrdered() {
        return productRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public SonyProduct save(SonyProduct product) {
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(LocalDateTime.now());
        }
        return productRepository.save(product);
    }

    @Override
    public SonyProduct update(SonyProduct product) {
        if (product.getProductId() == null) {
            throw new IllegalArgumentException("Product id is required for update");
        }
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SonyProduct> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, List<SonyProduct>> findTopProductsByStockPerCategory() {
        Map<String, List<SonyProduct>> result = new LinkedHashMap<>();
        List<SonyCategory> categories = categoryService.findAll();
        for (SonyCategory category : categories) {
            List<SonyProduct> topProducts = productRepository.findTop3ByCategoryOrderByStockDesc(category);
            result.put(category.getCateName(), topProducts);
        }
        return result;
    }
}
