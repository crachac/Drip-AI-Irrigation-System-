package drip.irrigation.system.ai;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 *
 * @author Muzaffar
 */
public class LoginController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXButton login;
    @FXML
    private AnchorPane rootAnchor;
    @FXML
    private Text hintText;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        String[] array = {"Суний онг", "Кулай интерфейс","Тежамкор ва иннаватсион ечим", "Минтака муаммосига ечим",""};
        Random rand = new Random();
        int n = rand.nextInt(array.length);
        int duration = 3000;
        hintText.setText(array[n]);

    }

    @FXML
    private void handleLoginAction(ActionEvent event) throws IOException {
        String user = username.getText();
        String pass = password.getText();

        if (user.equals("") && pass.equals("")) {

            //loginPane.setVisible(false);
            //FadeTransition transition = new FadeTransition(new Duration(1500), loginPane);
            // transition.setFromValue(1);
            //transition.setToValue(0);
            //transition.play();
            AnchorPane pane = FXMLLoader.load(getClass().getResource("Main.fxml"));
            rootAnchor.getChildren().setAll(pane);
        } else {
            label.setText("Логин ёки Парол хато! ");

        }
    }

    @FXML
    private void handleTextAnimation(MouseEvent event) {

        String[] array = {"Суний онг", "Кулай интерфейс","Тежамкор ва иннаватсион ечим", "Минтака муаммосига ечим",""};
        Random rand = new Random();
        int n = rand.nextInt(array.length);
        int duration = 3000;
        hintText.setText(array[n]);
    }

}
