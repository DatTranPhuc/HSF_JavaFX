package javafx.demojavafxs3.config;

import javafx.demojavafxs3.entity.SonyAccount;
import javafx.demojavafxs3.entity.SonyCategory;
import javafx.demojavafxs3.entity.SonyProduct;
import javafx.demojavafxs3.repository.SonyAccountRepository;
import javafx.demojavafxs3.repository.SonyCategoryRepository;
import javafx.demojavafxs3.repository.SonyProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadSampleData(SonyAccountRepository accountRepository,
                                     SonyCategoryRepository categoryRepository,
                                     SonyProductRepository productRepository) {
        return args -> {
            if (accountRepository.count() == 0) {
                SonyAccount admin = new SonyAccount();
                admin.setPhone("0905111111");
                admin.setPassword("@1");
                admin.setRoleId(1);

                SonyAccount staff = new SonyAccount();
                staff.setPhone("0905222222");
                staff.setPassword("@1");
                staff.setRoleId(2);

                SonyAccount customer = new SonyAccount();
                customer.setPhone("0905333333");
                customer.setPassword("@1");
                customer.setRoleId(3);

                accountRepository.saveAll(List.of(admin, staff, customer));
            }

            if (categoryRepository.count() == 0) {
                SonyCategory headphone = new SonyCategory();
                headphone.setCateName("HeadPhone");
                headphone.setStatus("active");

                SonyCategory cameras = new SonyCategory();
                cameras.setCateName("Cameras");
                cameras.setStatus("active");

                SonyCategory tvs = new SonyCategory();
                tvs.setCateName("TVs");
                tvs.setStatus("active");

                categoryRepository.saveAll(List.of(headphone, cameras, tvs));
            }

            if (productRepository.count() == 0) {
                List<SonyCategory> categories = categoryRepository.findAll();
                SonyCategory cameras = categories.stream().filter(c -> "Cameras".equals(c.getCateName())).findFirst().orElseThrow();
                SonyCategory tvs = categories.stream().filter(c -> "TVs".equals(c.getCateName())).findFirst().orElseThrow();
                SonyCategory headphone = categories.stream().filter(c -> "HeadPhone".equals(c.getCateName())).findFirst().orElseThrow();

                SonyProduct product1 = new SonyProduct();
                product1.setProductName("Alpha 1 II - Full-frame Mirrorless");
                product1.setPrice(6000);
                product1.setStock(3);
                product1.setCreatedAt(LocalDateTime.of(2025, Month.MARCH, 3, 0, 0));
                product1.setCategory(cameras);

                SonyProduct product2 = new SonyProduct();
                product2.setProductName("Alpha 7C II – Full-frame");
                product2.setPrice(2000);
                product2.setStock(5);
                product2.setCreatedAt(LocalDateTime.of(2025, Month.APRIL, 4, 0, 0));
                product2.setCategory(cameras);

                SonyProduct product3 = new SonyProduct();
                product3.setProductName("BRAVIA 8 OLED 4K HDR TV");
                product3.setPrice(2500);
                product3.setStock(10);
                product3.setCreatedAt(LocalDateTime.of(2025, Month.JANUARY, 1, 0, 0));
                product3.setCategory(tvs);

                SonyProduct product4 = new SonyProduct();
                product4.setProductName("LinkBuds Fit Truly Wireless Noise Canceling");
                product4.setPrice(180);
                product4.setStock(15);
                product4.setCreatedAt(LocalDateTime.of(2025, Month.MARCH, 3, 0, 0));
                product4.setCategory(headphone);

                productRepository.saveAll(List.of(product1, product2, product3, product4));
            }
        };
    }
}
