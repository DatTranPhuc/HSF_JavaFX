package javafx.demojavafxs3.service;

import javafx.demojavafxs3.entity.AccountRole;
import javafx.demojavafxs3.entity.SonyAccount;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionManager {

    private SonyAccount currentAccount;

    public void login(SonyAccount account) {
        this.currentAccount = account;
    }

    public void logout() {
        this.currentAccount = null;
    }

    public Optional<SonyAccount> getCurrentAccount() {
        return Optional.ofNullable(currentAccount);
    }

    public boolean isAdmin() {
        return currentAccount != null && currentAccount.getRole().isAdmin();
    }

    public boolean canManageProducts() {
        return currentAccount != null && currentAccount.getRole().canManageProducts();
    }

    public AccountRole getRole() {
        return currentAccount == null ? null : currentAccount.getRole();
    }
}
