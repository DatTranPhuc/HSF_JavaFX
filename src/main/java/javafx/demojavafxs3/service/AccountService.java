package javafx.demojavafxs3.service;

import javafx.demojavafxs3.entity.SonyAccount;

import java.util.Optional;

public interface AccountService {
    Optional<SonyAccount> authenticate(String phone, String password);
}
