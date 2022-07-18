import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import javafx.event.ActionEvent;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javafx.stage.Stage;

public class BillExpenseInfoController
{
    @FXML
    private Button imageSelectButton, enterBillExpenseInfoButton, cancelBillBttn;

    @FXML
    private Label fileSelected1, fileSelected2, fileSelected3, fileSelected4;

    @FXML
    private Label missingInfoLabel;

    @FXML
    private TextField amountTxtField, billNameTxtField;

    @FXML
    public DatePicker dateOfEntryTxtField, billDueDateTxtField;

    @FXML
    public ComboBox<String> categoryTxtField;

    @FXML
    private CheckBox yesCheckBox, noCheckBox;

    @FXML
    private AnchorPane billExpenseInfoPane;

    @FXML
    private Label message1, message2, message3, message4, noteForDates;


    public void cancelBillCreation()
    {
            // get a handle to the stage
            Stage stage = (Stage) cancelBillBttn.getScene().getWindow();
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
        if(categoryTxtField.getItems().isEmpty()) {
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
                    categoryTxtField.getItems().add(queryResult.getString(1));
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

    public void imageButtonAction(ActionEvent actionEvent)
    {
        //Put these setText methods here so if user clicks select image button again, the text that had been set from the previous selections also gets cleared as even though the List gets cleared, the Labels are separate from the list and are not cleared once the list is recreated; the list will accurately keep the count of the files selected each time the method
        //is called because a new list is created every single time
        fileSelected1.setText("");
        fileSelected2.setText("");
        fileSelected3.setText("");
        fileSelected4.setText("");
        //Put this here for troubleshooting to see how the children are arranged as a list in the Parent(the pane(window)) and to see if the order of the children as a list matches the order of the children in the "hierarchy" drop down menu in scenebuilder(which we verified does with this line of code as we made the "enter" Button be the last node in the list and got back a button when we ran this code) System.err.println(billExpenseInfoPane.getChildren().get(billExpenseInfoPane.getChildren().size() - 1).getClass());
        Label fileSelected = new Label();
        //Put this if statement here since the way we set up the hierarchy(order) of the children in the pane list is by having a Button being the last node in the list, so if there is anything other than a button at the end of the list, that means the fileSelected label(label we created manually in code) was added so we need to make sure we delete it off the Parent(pane) list when the user presses the "select image" button again; main reason we created a new Label rather than reusing a pre-existing Label is becuase we add effects(color, font size, textwrap, etc) to the label and those effects stay on the label permanently until you change them back manually in code so rather than dealing with having to change the label's appearance for every amount of files selected(for every if statement) we simply get rid of the label that warns when you have selected more than 4 files and only add it back when it is needed again
        if(billExpenseInfoPane.getChildren().get(billExpenseInfoPane.getChildren().size() - 1) instanceof Label)
        {
            billExpenseInfoPane.getChildren().remove(billExpenseInfoPane.getChildren().size() - 1);
        }
        try {
            //This creates a filechooser object and gives the button the ability to be able to choose files that are saved in the computer
            FileChooser fc = new FileChooser();
            //This creates a list that holds the files(actual File objects) that were chosen by user when filechooser window popped up; designing it with the funcitonality that if a user clicks on choose image button again, the list will be cleared of previous selection(s) and user will have to select all desired files again as each time the method is called a brand new list is created which means each time that the method is called, you start off with an empty list; unless user hits cancel, than the original list that was created and the files selected reamin(verified this in a unit test)
            List<File> selectedFiles = fc.showOpenMultipleDialog(null);
            //Added these if statements to fix bug where array would go out of bounds if user decided to choose different files than the ones they originally chose and to fix bug where the labels wouldn't correctly display the names of the selected files; one way we can clean this up is by making a tag that specifically says "Selected File(s): " and then just line up the other tags like we already have them, not too important tho
            if(selectedFiles.size() > 4)
            {
                //This makes sure that the text will wrap if it exceeds the amount set for setPrefWidth
                fileSelected.setWrapText(true);
                //This sets the preferred width that the label will take up(the amount of width space that the label has to expand); this basically sets the width the label can reach before needing to wrap
                fileSelected.setPrefWidth(350);
                fileSelected.setText("Chose more than 4 files!!!");
                //This sets the text color to red(using hex value here)
                fileSelected.setTextFill(Paint.valueOf("#f20101"));
                //This changes the size of the text to whatever is in parameters
                fileSelected.setFont(new Font(25));
                //setLayoutX and setLayoutY place the label in desired position in window
                fileSelected.setLayoutX(342);
                fileSelected.setLayoutY(360);
                billExpenseInfoPane.getChildren().add(fileSelected);
            }
            if(selectedFiles.size() == 1)
            {
                fileSelected1.setText("Selected File(s): " + selectedFiles.get(0).getName());
            }
            if(selectedFiles.size() == 2)
            {
                fileSelected1.setText("Selected File(s): " + selectedFiles.get(0).getName());
                fileSelected2.setText("Selected File(s): " + selectedFiles.get(1).getName());
            }
            if(selectedFiles.size() == 3)
               {
                fileSelected1.setText("Selected File(s): " + selectedFiles.get(0).getName());
                fileSelected2.setText("Selected File(s): " + selectedFiles.get(1).getName());
                fileSelected3.setText("Selected File(s): " + selectedFiles.get(2).getName());
            }
            if(selectedFiles.size() == 4)
            {
                fileSelected1.setText("Selected File(s): " + selectedFiles.get(0).getName());
                fileSelected2.setText("Selected File(s): " + selectedFiles.get(1).getName());
                fileSelected3.setText("Selected File(s): " + selectedFiles.get(2).getName());
                fileSelected4.setText("Selected File(s): " + selectedFiles.get(3).getName());
            }
            //Put this here for debugging purposes to see if list would need to be cleared if user decided to add or choose new files rather than the ones they originally chose, found that there was no need to clear the list as the way we have code setup, each time method is called, a brand new filechooser object is created which means that it doesnt have the original files that were selected before(old list is overridden by the creation of the new list)
//            System.err.println(selectedFiles.size());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    //This method is activates when the user checks either true or false and sets the text for the recurring bill note and the dates note(set it up like this cuz we were having issues having the textfeild activate added note for dates here for ease as it was giving us issues the original way we wanted to implement it
    public void showRecurringBillNote(ActionEvent event)
    {
        //Made this if statement like this so it can only show the split-bill note if the user selects yes for recurring-bill, did this to make it look neater and cuz it made more sense lol
        if(yesCheckBox.isSelected())
        {
message1.setText("*If split-bill, program will assume that recurring bill will continue");
message2.setText("to be split between people it is initially created with; only");
message3.setText("need to create a recurring bill once, after that it is added to your ");
message4.setText("monthly total until deleted or modified*");
        }
        noteForDates.setText("*Note: Date of Entry and Bill Due Date entries are FINAL once bill is saved*");
    }


    public void missingInfoError(ActionEvent event)
    {
        try {
            //Setup the if statement in the logical order that we did becuase or(||) will only check both conditions if the first condition is false(if 1st condition is true, it will not check the 2nd) and this is the behavior we want cuz we're checking if all the required fields are empty to throw an error so if one is empty, there is no need to check the others as user needs to at least fill out all required fields to be able to create a bill and if one required field is missing, there is no point in checking the other fields, and the last condition(!yesCheckBox.isSelected() & !noCheckBox.isSelected()) uses an &(single) cuz we want to make sure that at least one checkbox is checked, and if one is checked, since we have code set up to return true if it is not checked, if one is checked it will return false and make the statement return false and move on to else statement; what this code essentially says is return true if all required fields are empty and if its true then it goes into if statement(like usual) if one is false then keep checking the rest until all fields are false(this is achieved using or(||) operator) and the last statement returns false as single & operator will return false if one of the statements is false, this wouldnt work with or as or requires both statements to be false for it to return false.
            // Had to use getEditor method for DatePicker objects(date textfields) to convert them into regular textfields so user can type in their own date if they want to cuz if you checking for if the DatePicker object is empty(= to null) then the DatePicker object considers anything a user types null, the DatePicker object only recognizes input when utilizing the calendar
            if ((amountTxtField.getText().equals("") || dateOfEntryTxtField.getEditor().getText().equals("")) || (billDueDateTxtField.getEditor().getText().equals("") || billNameTxtField.getText().equals("")) || (!yesCheckBox.isSelected() & !noCheckBox.isSelected()))
            {
                missingInfoLabel.setText("Havent filled out all required fields!");
            }
            else{
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getConnection();
                try {
                    Statement statement = connectDB.createStatement();
                    //This line is beautiful, it updates the Categories table in our database if a user has entered a category name that is not already in the Categories table; does this by making the combobox a stream and using the predicate that we created(i -> i.equals(categoryTxtField.getValue()) which checks all the items currently in the combobox and tries to match them with whatever the user enters in the category combobox and if it returns false, then we add that category to the table(Put this query on method we will create for entering whole bill, will leave here for now)
                    if (!categoryTxtField.getItems().stream().anyMatch(i -> i.equals(categoryTxtField.getValue())))
                    {
                        String addNewCategoryQuery = "INSERT INTO `Capstone`.`Categories` (`categoryName`) VALUES " + "('" + categoryTxtField.getValue() + "');";
                        statement.executeUpdate(addNewCategoryQuery);
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    connectNow.getConnection().close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) enterBillExpenseInfoButton.getScene().getWindow();
                // closes current window
                stage.close();
                try {
                    // Creates the stage for the "what kind of expense window"
                    Stage enteringNewExpenseStage = new Stage();
                    // loads the fml file that is included in the parameters
                    Parent root = FXMLLoader.load(getClass().getResource("ExpenseAdded.fxml"));
                    //Creates the scene from the "root" that is loaded from the inputted fxml file
                    Scene scene = new Scene(root);
                    enteringNewExpenseStage.setTitle("Successfully Added Bill/Expense");
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
        catch(Exception e)
        {
          e.printStackTrace();
        }
//        //This section here was used to debug/test how to add children to the existing window created in scenebuilder using an fxml file and we figured it out!!(finally)
//        //Below demonstrates with an example how to add a child created from code to a window created with scenebuilder and an fxml file
//        Label test = new Label();
//        test.setText("This is a test world!!");
//        //Using setLayoutX() and setLayoutY() methods together will place the node you are adding in the location you want it to be in the window you are adding the child to
//        test.setLayoutX(392);
//        test.setLayoutY(350);
//        billExpenseInfoPane.getChildren().add(test);
    }

}
