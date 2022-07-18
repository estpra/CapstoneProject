import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

import javafx.stage.Stage;

public class ModifyingBillExpenseController
{
    @FXML
    public Button applyChangesBttn, backButton, deleteBillBttn, imagesButton;

    @FXML
    private TextField billNameTxtField;

    @FXML
    private ComboBox categoryComboBox;

    @FXML
    private TextArea descriptionTxtArea;

    @FXML
    private CheckBox noCheckBox, yesCheckBox;

    @FXML
    public Label selectedBillLabel, selectedFilesLabel, selectedFile1, selectedFile2, selectedFile3, selectedFile4;

    @FXML
    private AnchorPane modifyingBillExpensePane;


public void backButtonAction()
{
    // get a handle to the stage
    Stage stage = (Stage) backButton.getScene().getWindow();
    // closes current window
    stage.close();
    try {
        // Creates the stage for the "what kind of expense window"
        Stage enteringNewExpenseStage = new Stage();
        // loads the fml file that is included in the parameters
        Parent root = FXMLLoader.load(getClass().getResource("ModifyBillExpense.fxml"));
        //Creates the scene from the "root" that is loaded from the inputted fxml file
        Scene scene = new Scene(root);
        enteringNewExpenseStage.setTitle("Enter Bill/Expense Info");
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


public void imageButton()
{
    //Put these setText methods here so if user clicks select image button again, the text that had been set from the previous selections also gets cleared as even though the List gets
    //cleared, the Labels are separate from the list and are not cleared once the list is recreated; the list will accurately keep the count of the files selected each time the method
    //is called because a new list is created every single time
    selectedFile1.setText("");
    selectedFile2.setText("");
    selectedFile3.setText("");
    selectedFile4.setText("");
    selectedFilesLabel.setText("");
    //Put this here for troubleshooting to see how the children are arranged as a list in the Parent(the pane(window)) and to see if the order of the children as a list matches the
    //order of the children in the "hierarchy" drop down menu in scenebuilder(which we verified does with this line of code as we made the "enter" Button be the last node in the list and
    //got back a button when we ran this code)
//        System.err.println(billExpenseInfoPane.getChildren().get(billExpenseInfoPane.getChildren().size() - 1).getClass());
    Label fileSelected = new Label();
    //Put this if statement here since the way we set up the hierarchy(order) of the children in the pane list is by having a Button being the last node in the list, so if there is anything
    //other than a button at the end of the list, that means the fileSelected label(label we created manually in code) was added so we need to make sure we delete it off the Parent(pane) list
    //when the user presses the "select image" button again; main reason we created a new Label rather than reusing a pre-existing Label is becuase we add effects(color, font size, textwrap, etc)
    //to the label and those effects stay on the label permanently until you change them back manually in code so rather than dealing with having to change the label's appearance for every amount of
    //files selected(for every if statement) we simply get rid of the label that warns when you have selected more than 4 files and only add it back when it is needed again
    if(modifyingBillExpensePane.getChildren().get(modifyingBillExpensePane.getChildren().size() - 1) instanceof Label)
    {
        modifyingBillExpensePane.getChildren().remove(modifyingBillExpensePane.getChildren().size() - 1);
    }
    try {
        //This creates a filechooser object and gives the button the ability to be able to choose files that are saved in the computer
        FileChooser fc = new FileChooser();
        //This creates a list that holds the files(actual File objects) that were chosen by user when filechooser window popped up; designing it with the funcitonality that if a user
        //clicks on choose image button again, the list will be cleared of previous selection(s) and user will have to select all desired files again as each time the method is called
        //a brand new list is created which means each time that the method is called, you start off with an empty list; unless user hits cancel, than the original list that was created
        //and the files selected reamin(verified this in a unit test)
        List<File> selectedFiles = fc.showOpenMultipleDialog(null);
        //Added these if statements to fix bug where array would go out of bounds if user decided to choose different files than the ones they originally chose
        //and to fix bug where the labels wouldn't correctly display the names of the selected files; one way we can clean this up is by making a tag that specifically
        //says "Selected File(s): " and then just line up the other tags like we already have them, not too important tho
        if(selectedFiles.size() > 4)
        {
            //This makes sure that the text will wrap if it exceeds the amount set for setPrefWidth
            fileSelected.setWrapText(true);
            //This sets the preferred width that the label will take up(the amount of width space that the label has to expand); this basically sets the width the label can reach before
            //needing to wrap
            fileSelected.setPrefWidth(350);
            fileSelected.setText("Chose more than 4 files!!!");
            //This sets the text color to red(using hex value here)
            fileSelected.setTextFill(Paint.valueOf("#f20101"));
            //This changes the size of the text to whatever is in parameters
            fileSelected.setFont(new Font(25));
            //setLayoutX and setLayoutY place the label in desired position in window
            fileSelected.setLayoutX(330);
            fileSelected.setLayoutY(230);
            modifyingBillExpensePane.getChildren().add(fileSelected);
        }
        if(selectedFiles.size() == 1)
        {
            selectedFilesLabel.setText("Selected File(s): ");
            selectedFile1.setText(selectedFiles.get(0).getName());
        }
        if(selectedFiles.size() == 2)
        {
            selectedFilesLabel.setText("Selected File(s): ");
            selectedFile1.setText(selectedFiles.get(0).getName());
            selectedFile2.setText(selectedFiles.get(1).getName());
        }
        if(selectedFiles.size() == 3)
        {
            selectedFilesLabel.setText("Selected File(s): ");
            selectedFile1.setText(selectedFiles.get(0).getName());
            selectedFile2.setText(selectedFiles.get(1).getName());
            selectedFile3.setText(selectedFiles.get(2).getName());
        }
        if(selectedFiles.size() == 4)
        {
            selectedFilesLabel.setText("Selected File(s): ");
            selectedFile1.setText(selectedFiles.get(0).getName());
            selectedFile2.setText(selectedFiles.get(1).getName());
            selectedFile3.setText(selectedFiles.get(2).getName());
            selectedFile4.setText(selectedFiles.get(3).getName());
        }
        //Put this here for debugging purposes to see if list would need to be cleared if user decided to add or choose new files rather than the ones they originally chose, found
        //that there was no need to clear the list as the way we have code setup, each time method is called, a brand new filechooser object is created which means that it doesnt
        //have the original files that were selected before(old list is overridden by the creation of the new list)
//            System.err.println(selectedFiles.size());
    }
    catch (Exception e) {
        e.printStackTrace();
    }
}

public void setApplyChangesBttn()
{
    // get a handle to the stage
    Stage stage = (Stage) backButton.getScene().getWindow();
    // closes current window
    stage.close();
    try {
        // Creates the stage for the "what kind of expense window"
        Stage enteringNewExpenseStage = new Stage();
        // loads the fml file that is included in the parameters
        Parent root = FXMLLoader.load(getClass().getResource("ChangesSaved.fxml"));
        //Creates the scene from the "root" that is loaded from the inputted fxml file
        Scene scene = new Scene(root);
        enteringNewExpenseStage.setTitle("Changes Saved Successfully");
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

public void testScroll()
{
    for(int i =0; i < 10; i++)
    {
        Label t = new Label();
        t.setText("test# " + i);

    }
}

}

