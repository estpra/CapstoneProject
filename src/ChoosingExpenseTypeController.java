import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChoosingExpenseTypeController
{
    @FXML
    private Button backButton;

    @FXML
    private Button personalButton;

    @FXML
    private Button splitBillButton;

    public void backToHomeButton(){
// get a handle to the stage
        Stage stage = (Stage) backButton.getScene().getWindow();
        // closes current window
        stage.close();
        try {
            Stage enteringNewExpenseStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
            Scene scene = new Scene(root);
            enteringNewExpenseStage.setTitle("Home Page");
            enteringNewExpenseStage.setScene(scene);
            enteringNewExpenseStage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void setSplitBillButton()
    {
        // get a handle to the stage
        Stage stage = (Stage) backButton.getScene().getWindow();
        // closes current window
        stage.close();
        try {
            Stage enteringNewExpenseStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("SplitByNumOfPeople.fxml"));
            Scene scene = new Scene(root);
            enteringNewExpenseStage.setTitle("Specify Number Of People");
            enteringNewExpenseStage.setScene(scene);
            enteringNewExpenseStage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void personalBillButton()
    {
        // get a handle to the stage
        Stage stage = (Stage) backButton.getScene().getWindow();
        // closes current window
        stage.close();
        try {
            Stage enteringNewExpenseStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("BillExpenseInfo.fxml"));
            Scene scene = new Scene(root);
            enteringNewExpenseStage.setTitle("Enter Bill/Expense Info");
            enteringNewExpenseStage.setScene(scene);
            enteringNewExpenseStage.show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            e.getCause();
        }
    }
}
