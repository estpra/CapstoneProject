import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//import java.awt.event.KeyEvent;


public class SplitByNumOfPeopleController
{
    @FXML
    private TextField numOfPeopleTextField;

    @FXML
    private Button nextButton;

    //This method generates the appropriate number of labels and textfields based on the number inputted on the numOfPeopleTextField
    public void textFieldsandLabels() {
        // get a handle to the stage
        Stage stage = (Stage) nextButton.getScene().getWindow();
        // closes current window
        stage.close();
        //Creating a new stage to hold the pane(VBox in this case) and the scene for the labels and text fields that will be generated
                Stage textFieldsandLabelsStage = new Stage();
                //These creates a pane(VBox) that adds the nodes vertically in a single column
                VBox box = new VBox(Integer.parseInt(numOfPeopleTextField.getText()));
                //This loop creates the labels and textfields based on the number entered on the "num of people" textfield, start on a 1 based index to accurately label the order each label and textfield is created in
                for (int i = 1; i <= Integer.parseInt(numOfPeopleTextField.getText()); i++) {
                    //Creating nodes(the text fields)
                    TextField textFieldi = new TextField();
                    //Sets the greyed out text that is in the textbox but user will not have to manually erase it when they enter text in that textfield
                    textFieldi.setPromptText("Person" + " " + i + "'s" + " " + "Name");
                    //Creating labels
                    Label labeli = new Label("Person" + " " + i);
                    //Sets the padding for both the labels and textfields
                    box.setPadding(new Insets(15, 5, 5, 50));
                    //Adding labels for nodes, commented out version does the same thing as uncommented out version, it is just performed it two lines instead of one as the commented out version adds the label and textfield separately, one at a time
//                    box.getChildren().add(labeli);
//                    box.getChildren().add(textFieldi);
                    box.getChildren().addAll(labeli, textFieldi);
                }
                //Creating an enter button to be able to create an actionevent to go to the next window, which is the billexpenseinfo window, once user finishes entering the names of the people
                Button enterPeopleButton = new Button("Enter");
                //To add an element(button) to the pane you chose(VBox) you must call the parent element's(VBox in this case) variable name and getChildren() since buttons are children of the parent element, which is the pane(see javafx window hierarchy documentation for info) and then add the button
                box.getChildren().add(enterPeopleButton);
                //Creating a new scene for the textFieldsandLabelsStage as the stage is what houses everything(it is the window itself)
                Scene scene = new Scene(new ScrollPane(box), 400, 400);
                textFieldsandLabelsStage.setTitle("Enter People's name");
                textFieldsandLabelsStage.setScene(scene);
                textFieldsandLabelsStage.show();
            //Creating the actionevent to trigger once user is done entering the people's names, next window will appear and we are linking to an fxml file even though we created the textFieldsandLabelsStage and all its elements manually(awesome!!)
            //The override and the handle methods are generated automatically when you instantiate the eventhandler; added some try catch statements to catch any exceptions that may occur
//            EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
//                    @Override
//                    public void handle(ActionEvent actionEvent) {
//
//                            // get a handle to the stage
//                            Stage stage = (Stage) enterPeopleButton.getScene().getWindow();
//                            // closes current window
//                            stage.close();
//                            // Entering a try catch to catch any exceptions that could be thrown
//                            try {
//                                // Creates the stage for the "what kind of expense window"
//                                Stage enteringNewExpenseStage = new Stage();
//                                // loads the fxml file that is included in the parameters
//                                Parent p  = FXMLLoader.load(getClass().getResource("BillExpenseInfo.fxml"));
//                                //Creates the scene from the "root" that is loaded from the inputted fxml file
//                                Scene scene = new Scene(p);
//                                enteringNewExpenseStage.setTitle("Enter Bill/Expense Info");
//                                //Sets the scene of the enteringNewExpenseStage as the scene that is loaded from the inputted fxml file
//                                enteringNewExpenseStage.setScene(scene);
//                                // displays the newly loaded stage with its new scene
//                                enteringNewExpenseStage.show();
//                            }
//                            catch(Exception e)
//                            {
//                                e.printStackTrace();
//                                e.getCause();
//                        }
//                }
//            };
            //This call here actually causes the actionevent that we created to go into action once the enter button is clicked, hence actionevent and eventhandler; since the enterbutton is the element that we want to cause an event(displaying enteringNewExpenseStage) when an action(clicking on the button)
            //is performed, we must call setOnAction() method using the button and pass in the EventHandler event we created which displays the enteringNewExpenseStage, we simply set this action to button, but technically could apply it to any element, just need to call setOnAction() method using the desried
            //element you wish to inherit that actionevent
            enterPeopleButton.setOnAction(event -> {
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getConnection();
                for (int i = 1; i < box.getChildren().size(); i+=2) {
                    String query = "INSERT INTO `Capstone`.`People_Bills_Split_With` (`PersonName`, `billID`, `Paid`, `Amount`) VALUES " + "('"+ ((TextField) box.getChildren().get(i)).getText() + "'" + ", '1', '1', '20');";
                    try{
                        Statement statement = connectDB.createStatement();
                        statement.executeUpdate(query);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        e.getCause();
                    }
                }
                try {
                    connectNow.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                // get a handle to the stage
                Stage stage1 = (Stage) enterPeopleButton.getScene().getWindow();
                // closes current window
                stage1.close();
                // Entering a try catch to catch any exceptions that could be thrown
                try {
                    // Creates the stage for the "what kind of expense window"
                    Stage enteringNewExpenseStage = new Stage();
                    // loads the fxml file that is included in the parameters
                    Parent root  = FXMLLoader.load(getClass().getResource("BillExpenseInfo.fxml"));
                    //Creates the scene from the "root" that is loaded from the inputted fxml file
                    Scene scene1 = new Scene(root);
                    enteringNewExpenseStage.setTitle("Enter Bill/Expense Info");
                    //Sets the scene of the enteringNewExpenseStage as the scene that is loaded from the inputted fxml file
                    enteringNewExpenseStage.setScene(scene1);
                    // displays the newly loaded stage with its new scene
                    enteringNewExpenseStage.show();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    e.getCause();
                }
            });
    }


}
