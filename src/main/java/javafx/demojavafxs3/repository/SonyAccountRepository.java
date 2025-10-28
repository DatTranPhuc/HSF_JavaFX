package javafx.demojavafxs3.repository;

import javafx.demojavafxs3.entity.SonyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SonyAccountRepository extends JpaRepository<SonyAccount, Integer> {
    Optional<SonyAccount> findByPhoneAndPassword(String phone, String password);
}
