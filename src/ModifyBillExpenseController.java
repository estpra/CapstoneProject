import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ModifyBillExpenseController
{
    @FXML
    private ComboBox<String> categoryTxtField;

    @FXML
    private TextField dateTxtField;

    @FXML
    private Button getBillButton, homeButton;

    @FXML
    private AnchorPane modifyBillExpensePane;

public void backToHome()
{
    // get a handle to the stage
    Stage stage = (Stage) homeButton.getScene().getWindow();
    // closes current window
    stage.close();
    // Entering a try catch to catch any exceptions that could be thrown
    try {
        // Creates the stage for the "Stored Metrics window"
        Stage enteringNewExpenseStage = new Stage();
        // loads the fml file that is included in the parameters
        Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        //Creates the scene from the "root" that is loaded from the inputted fxml file
        Scene scene = new Scene(root);
        enteringNewExpenseStage.setTitle("HomePage");
        //Sets the scene of the enteringNewExpenseStage as the scene that is loaded from the inputted fxml fiel
        enteringNewExpenseStage.setScene(scene);
        // displays the newly loaded stage with its new scene
        enteringNewExpenseStage.show();
    }
    catch(Exception e)
    {
        e.printStackTrace();
        e.getCause();
    }
}

public void getBillBttnAction()
{
    // get a handle to the stage
    Stage stage = (Stage) homeButton.getScene().getWindow();
    // closes current window
    stage.close();
    // Entering a try catch to catch any exceptions that could be thrown
    try {
        // Creates the stage for the "Stored Metrics window"
        Stage enteringNewExpenseStage = new Stage();
        // loads the fml file that is included in the parameters
        Parent root = FXMLLoader.load(getClass().getResource("ModifyingBillExpense.fxml"));
        //Creates the scene from the "root" that is loaded from the inputted fxml file
        Scene scene = new Scene(root);
        enteringNewExpenseStage.setTitle("What Would Like To Modify?");
        //Sets the scene of the enteringNewExpenseStage as the scene that is loaded from the inputted fxml fiel
        enteringNewExpenseStage.setScene(scene);
        // displays the newly loaded stage with its new scene
        enteringNewExpenseStage.show();
    }
    catch(Exception e)
    {
        e.printStackTrace();
        e.getCause();
    }
}



public void modifyingBillExpenseWindow()
{
    // get a handle to the stage
    Stage stage = (Stage) getBillButton.getScene().getWindow();
    // closes current window
    stage.close();

    Stage modifyingBillExpenseStage = new Stage();
    AnchorPane modifyingBillExpensePane = new AnchorPane();
    modifyingBillExpensePane.setPrefWidth(600);
    modifyingBillExpensePane.setPrefHeight(400);
    //dateTxtField.getText() is a palce holder for the text field we end up creating
    Label billTitle = new Label("Selected Bill: " + dateTxtField.getText());
    billTitle.setPrefWidth(Region.USE_COMPUTED_SIZE);
    billTitle.setFont(Font.font("System", FontWeight.BOLD, 21));
    billTitle.setLayoutY(56);
    billTitle.setLayoutX(14);
    modifyingBillExpensePane.getChildren().add(billTitle);

    Label windowTitle = new Label("What Would You Like To Modify?");
    windowTitle.setPrefWidth(Region.USE_COMPUTED_SIZE);
    windowTitle.setFont(Font.font("System", FontWeight.BOLD, 26));
    windowTitle.setLayoutY(14);
    windowTitle.setLayoutX(119);
    modifyingBillExpensePane.getChildren().add(windowTitle);


    Scene scene = new Scene(modifyingBillExpensePane);
    modifyingBillExpenseStage.setTitle("What Would Like To Modify?");
    modifyingBillExpenseStage.setScene(scene);
    modifyingBillExpenseStage.show();

}


}

