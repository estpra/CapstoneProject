import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StoredMetricsController
{
    @FXML
    private TextField billNameTxtBox;

    @FXML
    private ComboBox desiredMetricsTxtBox, categoryTxtBox;

    @FXML
    private Button getInfoBttn, homeButton;

    @FXML
    public DatePicker dateTxtBox;

    public void backToHome(ActionEvent event)
    {
        // get a handle to the stage
        Stage stage = (Stage) homeButton.getScene().getWindow();
        // closes current window
        stage.close();
        // Entering a try catch to catch any exceptions that could be thrown
        try {
            // Creates the stage for the "what kind of expense window"
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

    public void setCategoryTxtField()
    {
        //Added this if statement to fix bug where the combobox would keep adding the items to the list each time the box was clicked(which is the action we chose to invoke this method)
        if(categoryTxtBox.getItems().isEmpty()) {
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getConnection();
            String query = "SELECT Categories.categoryName FROM Capstone.Categories";
            try {
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(query);
//                System.err.println(queryResult.next());
//                System.err.println(queryResult.getString(1));
                while(queryResult.next())
                {
                    categoryTxtBox.getItems().add(queryResult.getString(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
            try {
                connectNow.getConnection().close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getInfoButtonAction(ActionEvent event)
    {
        // get a handle to the stage
        Stage stage = (Stage) getInfoBttn.getScene().getWindow();
        // closes current window
        stage.close();
        // Entering a try catch to catch any exceptions that could be thrown
        try {
            // Creates the stage for the "what kind of expense window"
            Stage enteringNewExpenseStage = new Stage();
            // loads the fml file that is included in the parameters
            Parent root = FXMLLoader.load(getClass().getResource("RequestedMetrics.fxml"));
            //Creates the scene from the "root" that is loaded from the inputted fxml file
            Scene scene = new Scene(root);
            enteringNewExpenseStage.setTitle("Search Results");
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

