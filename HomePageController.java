import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static javafx.application.Application.launch;

public class HomePageController
{
    @FXML
    private BorderPane homeWindow;

    @FXML
    private Button logoutButton, modBillButton, newExpenseButton, storedDataButton;

    //We just created this method to figure out/test how to close a window
    public void testCloseWindow(){
        // get a handle to the stage
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        // closes current window
        stage.close();
    }

    //This function closes the homepage window and creates and displays the "what kind of expense" window
    public void enteringNewExpenseWindow() throws IOException {
        // get a handle to the stage
        Stage stage = (Stage) newExpenseButton.getScene().getWindow();
        // closes current window
        stage.close();
        // Entering a try catch to catch any exceptions that could be thrown
        try {
            // Creates the stage for the "what kind of expense window"
            Stage enteringNewExpenseStage = new Stage();
            // loads the fml file that is included in the parameters
            Parent root = FXMLLoader.load(getClass().getResource("ChoosingExpenseType.fxml"));
            //Creates the scene from the "root" that is loaded from the inputted fxml file
            Scene scene = new Scene(root);
            enteringNewExpenseStage.setTitle("Choose Expense Type");
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

    public void storedMetricsWindow(ActionEvent event)
    {
        // get a handle to the stage
        Stage stage = (Stage) storedDataButton.getScene().getWindow();
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
            enteringNewExpenseStage.setTitle("Stored Metrics");
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

    public void modifyBillWindow(ActionEvent event)
    {
        // get a handle to the stage
        Stage stage = (Stage) modBillButton.getScene().getWindow();
        // closes current window
        stage.close();
        // Entering a try catch to catch any exceptions that could be thrown
        Stage modifyBillExpenseStage = new Stage();
        AnchorPane modifyBillExpensePane = new AnchorPane();
        modifyBillExpensePane.setPrefWidth(572);
        modifyBillExpensePane.setPrefHeight(400);

        Label windowTitleLabel = new Label("What Bill Would You Like To Modify");
        windowTitleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        windowTitleLabel.setLayoutX(55);
        windowTitleLabel.setLayoutY(31);
        modifyBillExpensePane.getChildren().add(windowTitleLabel);

        Label dateLabel = new Label("Date:");
        dateLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        dateLabel.setLayoutX(29);
        dateLabel.setLayoutY(98);
        modifyBillExpensePane.getChildren().add(dateLabel);

        Label categoryLabel = new Label("Category:");
        categoryLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        categoryLabel.setLayoutX(25);
        categoryLabel.setLayoutY(173);
        modifyBillExpensePane.getChildren().add(categoryLabel);

        Label billNameLabel = new Label("Bill Name:");
        billNameLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        billNameLabel.setLayoutX(24);
        billNameLabel.setLayoutY(265);
        modifyBillExpensePane.getChildren().add(billNameLabel);

        DatePicker dateTxtField = new DatePicker();
        dateTxtField.setLayoutX(100);
        dateTxtField.setLayoutY(95);
        modifyBillExpensePane.getChildren().add(dateTxtField);

        ComboBox<String> categoryTxtField = new ComboBox<>();
        categoryTxtField.setEditable(true);
        categoryTxtField.setLayoutX(110);
        categoryTxtField.setLayoutY(170);
        modifyBillExpensePane.getChildren().add(categoryTxtField);
        categoryTxtField.setOnMouseClicked(e -> {
            //Added this if statement to fix bug where the combobox would keep adding the items to the list each time the box was clicked(which is the action we chose to invoke this method)
            if(categoryTxtField.getItems().isEmpty()) {
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getConnection();
                String query = "SELECT Categories.categoryName FROM Capstone.Categories";
                try {
                    Statement statement = connectDB.createStatement();
                    ResultSet queryResult = statement.executeQuery(query);
                    while(queryResult.next())
                    {
                        categoryTxtField.getItems().add(queryResult.getString(1));
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    e1.getCause();
                }
                try {
                    connectNow.getConnection().close();
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        });

        ComboBox<String> billNameTextField = new ComboBox<>();
        //.getItems().addAll() methods allows you to add available item user has to choose from the combo box
        billNameTextField.getItems().addAll("Item1", "Item2", "Item3");
        //This allows user to be able to enter their own option into the ComboBox's text field
        billNameTextField.setEditable(true);
        billNameTextField.setLayoutX(105);
        billNameTextField.setLayoutY(262);
        modifyBillExpensePane.getChildren().add(billNameTextField);

        Button homeButton = new Button("Home");
        homeButton.setPadding(new Insets(15));
        homeButton.setLayoutX(27);
        homeButton.setLayoutY(323);
        modifyBillExpensePane.getChildren().add(homeButton);

        Button getBillButton = new Button("Get Bill");
        getBillButton.setPadding(new Insets(15));
        getBillButton.setLayoutX(427);
        getBillButton.setLayoutY(321);
        getBillButton.setStyle("-fx-background-color: red");
        getBillButton.setFont(Font.font("System", FontWeight.BOLD, 16));
        modifyBillExpensePane.getChildren().add(getBillButton);

        Scene scene = new Scene(modifyBillExpensePane);
        modifyBillExpenseStage.setTitle("Choose a Bill/Expense");
        modifyBillExpenseStage.setScene(scene);
        modifyBillExpenseStage.show();

        homeButton.setOnAction(actionEvent -> {
            // get a handle to the stage
            Stage stage1 = (Stage) homeButton.getScene().getWindow();
            // closes current window
            stage1.close();
            // Entering a try catch to catch any exceptions that could be thrown
            try {
                // Creates the stage for the "what kind of expense window"
                Stage enteringNewExpenseStage = new Stage();
                // loads the fml file that is included in the parameters
                Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
                //Creates the scene from the "root" that is loaded from the inputted fxml file
                Scene scene1 = new Scene(root);
                enteringNewExpenseStage.setTitle("HomePage");
                //Sets the scene of the enteringNewExpenseStage as the scene that is loaded from the inputted fxml fiel
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
        getBillButton.setOnMouseClicked(event1 -> {
            // get a handle to the stage
            Stage stage2 = (Stage) getBillButton.getScene().getWindow();
            // closes current window
            stage2.close();

            Stage modifyingBillExpenseStage = new Stage();
            AnchorPane modifyingBillExpensePane = new AnchorPane();
            modifyingBillExpensePane.setPrefWidth(600);
            modifyingBillExpensePane.setPrefHeight(400);

            //dont have to explicitly state dateTxtField.getValue() to be a string as it is automatically converted to a string by using concatinating(+) symbol
            Label billTitle = new Label("Selected Bill: " + billNameTextField.getValue());
            billTitle.setPrefWidth(Region.USE_COMPUTED_SIZE);
            billTitle.setFont(Font.font("System", FontWeight.BOLD, 21));
            billTitle.setLayoutY(56);
            billTitle.setLayoutX(14);
            modifyingBillExpensePane.getChildren().add(billTitle);

            Label billNameLabel1 = new Label("Bill Name: ");
            billNameLabel1.setPrefWidth(Region.USE_COMPUTED_SIZE);
            billNameLabel1.setFont(Font.font("System", FontWeight.BOLD, 16));
            billNameLabel1.setLayoutY(108);
            billNameLabel1.setLayoutX(14);
            modifyingBillExpensePane.getChildren().add(billNameLabel1);

            Label categoryNameLabel = new Label("Category: ");
            categoryNameLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
            categoryNameLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            categoryNameLabel.setLayoutY(158);
            categoryNameLabel.setLayoutX(14);
            modifyingBillExpensePane.getChildren().add(categoryNameLabel);

            Label descriptionLabel = new Label("Description: ");
            descriptionLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
            descriptionLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            descriptionLabel.setLayoutY(265);
            descriptionLabel.setLayoutX(14);
            modifyingBillExpensePane.getChildren().add(descriptionLabel);

            Label recurringBillLabel = new Label("Recurring Bill: ");
            recurringBillLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
            recurringBillLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            recurringBillLabel.setLayoutY(108);
            recurringBillLabel.setLayoutX(301);
            modifyingBillExpensePane.getChildren().add(recurringBillLabel);

            Label imagesLabel = new Label("Image(s): ");
            imagesLabel.setPrefWidth(Region.USE_COMPUTED_SIZE);
            imagesLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
            imagesLabel.setLayoutY(159);
            imagesLabel.setLayoutX(301);
            modifyingBillExpensePane.getChildren().add(imagesLabel);

            Label windowTitle = new Label("What Would You Like To Modify?");
            windowTitle.setPrefWidth(Region.USE_COMPUTED_SIZE);
            windowTitle.setFont(Font.font("System", FontWeight.BOLD, 26));
            windowTitle.setLayoutY(14);
            windowTitle.setLayoutX(119);
            modifyingBillExpensePane.getChildren().add(windowTitle);

            TextField billNameTxtField = new TextField();
            billNameTxtField.setPromptText("Enter New Bill Name");
            billNameTxtField.setLayoutX(92);
            billNameTxtField.setLayoutY(106);
            modifyingBillExpensePane.getChildren().add(billNameTxtField);

            ComboBox<String> categoryTxtField2 = new ComboBox<>();
            categoryTxtField2.setPromptText("Select new category for selected bill");
            categoryTxtField2.setLayoutX(92);
            categoryTxtField2.setLayoutY(156);
            categoryTxtField2.setMaxWidth(160);
            //Allows user to be able to type in their own category if not already existing
            categoryTxtField2.setEditable(true);
            modifyingBillExpensePane.getChildren().add(categoryTxtField2);

            TextArea descriptionTxt = new TextArea("New Description");
            descriptionTxt.setMaxWidth(220);
            descriptionTxt.setPrefHeight(150);
            descriptionTxt.setLayoutX(98);
            descriptionTxt.setLayoutY(200);
            modifyingBillExpensePane.getChildren().add(descriptionTxt);

            CheckBox yesCheckBox = new CheckBox();
            yesCheckBox.setLayoutX(410);
            yesCheckBox.setLayoutY(110);
            yesCheckBox.setText("Yes");
            CheckBox noCheckBox = new CheckBox();
            noCheckBox.setLayoutX(482);
            noCheckBox.setLayoutY(110);
            noCheckBox.setText("No");
            modifyingBillExpensePane.getChildren().add(yesCheckBox);
            modifyingBillExpensePane.getChildren().add(noCheckBox);

            Button imageButton = new Button("Select images, 4 max");
            imageButton.setLayoutX(392);
            imageButton.setLayoutY(157);
            modifyingBillExpensePane.getChildren().add(imageButton);

            Button backButton = new Button("Back");
            backButton.setLayoutX(19);
            backButton.setLayoutY(346);
            backButton.setFont(Font.font("System", FontWeight.BOLD, 17));
            modifyingBillExpensePane.getChildren().add(backButton);

            Button deleteBillBttn = new Button("Delete Bill");
            deleteBillBttn.setLayoutX(331);
            deleteBillBttn.setLayoutY(348);
            deleteBillBttn.setFont(Font.font("System", FontWeight.BOLD, 17));
            deleteBillBttn.setStyle("-fx-text-fill: red");
            modifyingBillExpensePane.getChildren().add(deleteBillBttn);

            Button applyChangesBttn = new Button("Apply Changes");
            applyChangesBttn.setLayoutX(447);
            applyChangesBttn.setLayoutY(348);
            applyChangesBttn.setFont(Font.font("System", FontWeight.BOLD, 17));
            applyChangesBttn.setStyle("-fx-text-fill: green");
            modifyingBillExpensePane.getChildren().add(applyChangesBttn);

            Scene scene2 = new Scene(modifyingBillExpensePane);
            modifyingBillExpenseStage.setTitle("What Would Like To Modify?");
            modifyingBillExpenseStage.setScene(scene2);
            modifyingBillExpenseStage.show();

            backButton.setOnAction(event2 -> {
                try {
                    // get a handle to the stage
                    Stage stage3 = (Stage) backButton.getScene().getWindow();
                    // closes current window
                    stage3.close();
                    modifyBillWindow(event);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    e.getCause();
                }
            });
            deleteBillBttn.setOnAction(e3 -> {
                // get a handle to the stage
                Stage stage4 = (Stage) deleteBillBttn.getScene().getWindow();
                // closes current window
                stage4.close();

                Stage deletedStage = new Stage();
                AnchorPane deletedPane = new AnchorPane();
                deletedPane.setPrefWidth(600);
                deletedPane.setPrefHeight(314);

                Label deletedWinTitle = new Label("Bill Has Been Successfully Deleted!");
                deletedWinTitle.setFont(Font.font("System", FontWeight.BOLD, 30));
                deletedWinTitle.setLayoutX(29);
                deletedWinTitle.setLayoutY(31);
                deletedPane.getChildren().add(deletedWinTitle);

                Label deletedLabel = new Label("Deleted: " + billNameTextField.getValue());
                deletedLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
                deletedLabel.setLayoutX(29);
                deletedLabel.setLayoutY(101);
                deletedPane.getChildren().add(deletedLabel);

                //Will probably have to come back and fix this to pull the category name using a query or something from the database
                Label fromCategoryLabel = new Label("From Category: " + categoryTxtField.getValue());
                fromCategoryLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
                fromCategoryLabel.setLayoutX(29);
                fromCategoryLabel.setLayoutY(157);
                deletedPane.getChildren().add(fromCategoryLabel);

                Button returnHome = new Button("Return Home");
                returnHome.setFont(Font.font("System", FontWeight.BOLD, 18));
                returnHome.setLayoutX(14);
                returnHome.setLayoutY(232);
                deletedPane.getChildren().add(returnHome);

                Button modDeleteButton = new Button("Modify/Delete Another Expense");
                modDeleteButton.setFont(Font.font("System", FontWeight.BOLD, 18));
                modDeleteButton.setLayoutX(309);
                modDeleteButton.setLayoutY(232);
                deletedPane.getChildren().add(modDeleteButton);

                Scene scene3 = new Scene(deletedPane);
                deletedStage.setTitle("Deletion Successful!");
                deletedStage.setScene(scene3);
                deletedStage.show();

                returnHome.setOnAction(e -> {
                    // get a handle to the stage
                    Stage stage1 = (Stage) returnHome.getScene().getWindow();
                    // closes current window
                    stage1.close();
                    // Entering a try catch to catch any exceptions that could be thrown
                    try {
                        // Creates the stage for the "what kind of expense window"
                        Stage enteringNewExpenseStage = new Stage();
                        // loads the fml file that is included in the parameters
                        Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
                        //Creates the scene from the "root" that is loaded from the inputted fxml file
                        Scene scene1 = new Scene(root);
                        enteringNewExpenseStage.setTitle("HomePage");
                        //Sets the scene of the enteringNewExpenseStage as the scene that is loaded from the inputted fxml fiel
                        enteringNewExpenseStage.setScene(scene1);
                        // displays the newly loaded stage with its new scene
                        enteringNewExpenseStage.show();
                    }
                    catch(Exception e2)
                    {
                        e2.printStackTrace();
                        e2.getCause();
                    }
                });
                modDeleteButton.setOnAction(e2 -> {
                    // get a handle to the stage
                    Stage s = (Stage) modDeleteButton.getScene().getWindow();
                    // closes current window
                    s.close();
                    modifyBillWindow(event);
                });
            });
            applyChangesBttn.setOnAction(event3 -> {
                DatabaseConnection connectNow = new DatabaseConnection();
                Connection connectDB = connectNow.getConnection();
                try {
                    Statement statement = connectDB.createStatement();
                    //This line is beautiful, it updates the Categories table in our database if a user has entered a category name that is not already in the Categories table; does this by making the combobox a stream and using the predicate that we created(i -> i.equals(categoryTxtField.getValue()) which checks all the items currently in the combobox and tries to match them with whatever the user enters in the category combobox and if it returns false, then we add that category to the table(Put this query on method we will create for entering whole bill, will leave here for now)
                    if (!categoryTxtField2.getItems().stream().anyMatch(i -> i.equals(categoryTxtField2.getValue())))
                    {
                        String addNewCategoryQuery = "INSERT INTO `Capstone`.`Categories` (`categoryName`) VALUES " + "('" + categoryTxtField2.getValue() + "');";
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
                // get a handle to the stage
                Stage stage4 = (Stage) applyChangesBttn.getScene().getWindow();
                // closes current window
                stage4.close();

                Stage modsAppliedStage = new Stage();
                AnchorPane changesSavedPane = new AnchorPane();
                changesSavedPane.setPrefWidth(600);
                changesSavedPane.setPrefHeight(314);

                Label modsAppliedWinTitle = new Label("Your Changes Have Been Saved & Applied!");
                modsAppliedWinTitle.setFont(Font.font("System", FontWeight.BOLD, 30));
                modsAppliedWinTitle.setLayoutX(29);
                modsAppliedWinTitle.setLayoutY(31);
                changesSavedPane.getChildren().add(modsAppliedWinTitle);

                Label modifiedLabel = new Label("Modified: " + billNameTextField.getValue());
                modifiedLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
                modifiedLabel.setLayoutX(29);
                modifiedLabel.setLayoutY(101);
                changesSavedPane.getChildren().add(modifiedLabel);

                //Will probably have to come back and fix this to pull the category name using a query or something from the database
                Label fromCategoryLabel = new Label("From Category: " + categoryTxtField.getValue());
                fromCategoryLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
                fromCategoryLabel.setLayoutX(29);
                fromCategoryLabel.setLayoutY(157);
                changesSavedPane.getChildren().add(fromCategoryLabel);

                Button returnHome = new Button("Return Home");
                returnHome.setFont(Font.font("System", FontWeight.BOLD, 18));
                returnHome.setLayoutX(14);
                returnHome.setLayoutY(232);
                changesSavedPane.getChildren().add(returnHome);

                Button modDeleteButton = new Button("Modify/Delete Another Expense");
                modDeleteButton.setFont(Font.font("System", FontWeight.BOLD, 18));
                modDeleteButton.setLayoutX(309);
                modDeleteButton.setLayoutY(232);
                changesSavedPane.getChildren().add(modDeleteButton);

                Scene scene3 = new Scene(changesSavedPane);
                modsAppliedStage.setTitle("Modifications Applied");
                modsAppliedStage.setScene(scene3);
                modsAppliedStage.show();

                returnHome.setOnAction(e -> {
                    // get a handle to the stage
                    Stage stage1 = (Stage) returnHome.getScene().getWindow();
                    // closes current window
                    stage1.close();
                    // Entering a try catch to catch any exceptions that could be thrown
                    try {
                        // Creates the stage for the "what kind of expense window"
                        Stage enteringNewExpenseStage = new Stage();
                        // loads the fml file that is included in the parameters
                        Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
                        //Creates the scene from the "root" that is loaded from the inputted fxml file
                        Scene scene1 = new Scene(root);
                        enteringNewExpenseStage.setTitle("HomePage");
                        //Sets the scene of the enteringNewExpenseStage as the scene that is loaded from the inputted fxml fiel
                        enteringNewExpenseStage.setScene(scene1);
                        // displays the newly loaded stage with its new scene
                        enteringNewExpenseStage.show();
                    }
                    catch(Exception e2)
                    {
                        e2.printStackTrace();
                        e2.getCause();
                    }
                });
                modDeleteButton.setOnAction(e2 -> {
                    // get a handle to the stage
                    Stage s = (Stage) modDeleteButton.getScene().getWindow();
                    // closes current window
                    s.close();
                    modifyBillWindow(event);
                });
            });
        });
    }



}
