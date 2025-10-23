package javafx.demojavafxs3.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField txtUserName;

    @FXML
    private PasswordField txtPassword;

    @FXML
    protected void onHelloButtonClick() {
        if(txtUserName.getText().equals("admin") && txtPassword.getText().equals("admin")){
            welcomeText.setText("Login Successful! Welcome " + txtUserName.getText());
        } else {
            welcomeText.setText("Login Failed! Invalid Credentials.");
        }
    }

    public void onByeByeClick(ActionEvent actionEvent) {
        System.exit(0);
    }
}