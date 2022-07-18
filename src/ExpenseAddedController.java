import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ExpenseAddedController
{
    @FXML
    private Button homeButton, newBillExpenseButton;
public void backToHomeButton(ActionEvent event) throws IOException {
    // get a handle to the stage
    Stage stage = (Stage) homeButton.getScene().getWindow();
    // closes current window
    stage.close();
    // Creates the stage for the "what kind of expense window"
    Stage homePageStage = new Stage();
    // loads the fml file that is included in the parameters
    Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
    //Creates the scene from the "root" that is loaded from the inputted fxml file
    Scene scene = new Scene(root);
    homePageStage.setTitle("HomePage");
    //Sets the scene of the enteringNewExpenseStage as the scene that is loaded from the inputted fxml fiel
    homePageStage.setScene(scene);
    // displays the newly loaded stage with its new scene
    homePageStage.show();
}

public void backToChoosingExpenseType(ActionEvent event) throws IOException {
    // get a handle to the stage
    Stage stage = (Stage) newBillExpenseButton.getScene().getWindow();
    // closes current window
    stage.close();
    // Creates the stage for the "what kind of expense window"
    Stage homePageStage = new Stage();
    // loads the fml file that is included in the parameters
    Parent root = FXMLLoader.load(getClass().getResource("ChoosingExpenseType.fxml"));
    //Creates the scene from the "root" that is loaded from the inputted fxml file
    Scene scene = new Scene(root);
    homePageStage.setTitle("Choose Expense Type");
    //Sets the scene of the enteringNewExpenseStage as the scene that is loaded from the inputted fxml fiel
    homePageStage.setScene(scene);
    // displays the newly loaded stage with its new scene
    homePageStage.show();
}
}
