package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DBConnect;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerController {
	DBConnect conn = null;

    @FXML
    private TextField searchbox;
    
    @FXML
    private Label errorlabel;
    
    @FXML
    private Label restname;
    
    @FXML
    private Label restlocation;
    
    @FXML
    private Label restcuisine;
    
    @FXML
    private Label resttimings;
    
    @FXML
    private Label restcontact;
    
    @FXML
    private Label restcost;
    
    @FXML
    private Label restrating;
    
    @FXML
    private ChoiceBox restlist;
    
    public CustomerController() {  
        conn = new DBConnect();
    }

    public void searchRestaurants(ActionEvent event) throws IOException {
		Connection connection;
		try {
			restlist.getItems().clear();
			connection = conn.connect();
			Statement statement = connection.createStatement();
			String search_term = searchbox.getText().toString();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM aish_restaurant WHERE LOWER(name) LIKE LOWER('%" + search_term + "%');");
			while (resultSet.next()) {
				String item = resultSet.getString("name");
				restlist.getItems().add(item);
			}
	        connection.close();
		} catch (SQLException e) {
			errorlabel.setText("Restaurant not found.");
			e.printStackTrace();
		}
    }
    
    public void handleRestListAction(ActionEvent event) throws Exception {
    	Connection connection;
		try {
			connection = conn.connect();
			Statement statement = connection.createStatement();
			Object rnametemp = restlist.getValue();
			String rname = "";
			if(rnametemp != null) {
				rname = rnametemp.toString();
			}
			ResultSet resultSet = statement.executeQuery("SELECT * FROM aish_restaurant WHERE name = '" + rname + "';");
			while (resultSet.next()) {
				restname.setText(resultSet.getString("name"));
				restlocation.setText(resultSet.getString("location"));
				restcuisine.setText(resultSet.getString("cuisine"));
				resttimings.setText(resultSet.getString("timings"));
				restcontact.setText(resultSet.getString("contact"));
				restcost.setText(resultSet.getString("average_cost"));
				restrating.setText(resultSet.getString("rating"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void gotoLogin(ActionEvent event) throws Exception {
		Connection connection;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
            Parent firstPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(firstPage));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}