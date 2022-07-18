import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RequestedMetricsController
{
    @FXML
    Button homeButton, viewOtherMetricsBttn;

    public void backToHomeButton(){
// get a handle to the stage
        Stage stage = (Stage) homeButton.getScene().getWindow();
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

    public void storedMetricsWindow(ActionEvent event)
    {
        // get a handle to the stage
        Stage stage = (Stage) viewOtherMetricsBttn.getScene().getWindow();
        // closes current window
        stage.close();
        // Entering a try catch to catch any exceptions that could be thrown
        try {
            // Creates the stage for the "Stored Metrics window"
            Stage enteringNewExpenseStage = new Stage();
            // loads the fml file that is included in the parameters
            Parent root = FXMLLoader.load(getClass().getResource("StoredMetrics.fxml"));
            //Creates the scene from the "root" that is loaded from the inputted fxml file
            Scene scene = new Scene(root);
            enteringNewExpenseStage.setTitle("Choose Stored Metrics");
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
}
