package javafx.demojavafxs3.controller;

import javafx.demojavafxs3.entity.SonyAccount;
import javafx.demojavafxs3.service.AccountService;
import javafx.demojavafxs3.service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class HelloController {

    private final AccountService accountService;
    private final SessionManager sessionManager;
    private final ApplicationContext applicationContext;

    @FXML
    private TextField txtPhone;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label messageLabel;

    @Autowired
    public HelloController(AccountService accountService,
                           SessionManager sessionManager,
                           ApplicationContext applicationContext) {
        this.accountService = accountService;
        this.sessionManager = sessionManager;
        this.applicationContext = applicationContext;
    }

    @FXML
    public void onLoginClick() {
        messageLabel.setText("");

        String phone = txtPhone.getText() == null ? "" : txtPhone.getText().trim();
        String password = txtPassword.getText() == null ? "" : txtPassword.getText();

        if (phone.isEmpty() || password.isEmpty()) {
            messageLabel.setText("You do not have permission to access this function!");
            return;
        }

        Optional<SonyAccount> accountOptional = accountService.authenticate(phone, password);
        if (accountOptional.isEmpty() || !accountOptional.get().getRole().canManageProducts()) {
            messageLabel.setText("You do not have permission to access this function!");
            return;
        }

        sessionManager.login(accountOptional.get());
        openManagementPage();
    }

    private void openManagementPage() {
        Stage stage = (Stage) txtPhone.getScene().getWindow();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafx/demojavafxs3/sony-management-view.fxml"));
            loader.setControllerFactory(applicationContext::getBean);
            Scene scene = new Scene(loader.load(), 1100, 720);
            stage.setTitle("StudentName -  Sony Management Page");
            stage.setScene(scene);
        } catch (IOException e) {
            messageLabel.setText("Unable to open management page.");
        }
    }

    @FXML
    public void onExitClick() {
        Stage stage = (Stage) txtPhone.getScene().getWindow();
        stage.close();
    }
}
