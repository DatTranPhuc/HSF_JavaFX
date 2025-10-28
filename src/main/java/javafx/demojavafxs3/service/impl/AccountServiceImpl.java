package javafx.demojavafxs3.service.impl;

import javafx.demojavafxs3.entity.SonyAccount;
import javafx.demojavafxs3.repository.SonyAccountRepository;
import javafx.demojavafxs3.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AccountServiceImpl implements AccountService {

    private final SonyAccountRepository accountRepository;

    public AccountServiceImpl(SonyAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Optional<SonyAccount> authenticate(String phone, String password) {
        return accountRepository.findByPhoneAndPassword(phone, password);
    }
}
