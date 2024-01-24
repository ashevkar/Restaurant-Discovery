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

public class RestaurantController {
	private static String userId;
	private static ResultSet restaurantInfo;

    @FXML
    private TextField addname;
    
    @FXML
    private TextField addlocation;
	 
    @FXML
    private TextField addcuisine;
    
    @FXML
    private TextField addrating;
    
    @FXML
    private TextField addtiming;
    
    @FXML
    private TextField addcost;
    
    @FXML
    private TextField addcontact;
    
    @FXML
    private Label comment;
    
	DBConnect conn = null;
    
    public RestaurantController () {  
        conn = new DBConnect();
    }
    
    public void setUserId(String data) {
        this.userId = data;
    }
    public static ResultSet getRestaurantInfo() {
        return restaurantInfo;
    }
    
    public void addRestaurants(ActionEvent event) throws Exception {
		Connection connection=null;
		try {
			setUserId(controller.RestaurantLandingController.getUserId());
			String rname = addname.getText();
			String rlocation = addlocation.getText();
			String rcuisine = addcuisine.getText();
			String rrating = addrating.getText();
			String rtiming = addtiming.getText();
			String rcost = addcost.getText();
			String rcontact = addcontact.getText();

			connection = conn.connect();
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			String sql = "INSERT INTO aish_restaurant (name,location,cuisine,timings,contact,average_cost,rating) VALUES (?,?,?,?,?,?,?)";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, rname);
			preparedStatement.setString(2, rlocation);
			preparedStatement.setString(3, rcuisine);
			preparedStatement.setString(4, rtiming);
			preparedStatement.setString(5, rcontact);
			preparedStatement.setString(6, rcost);
			preparedStatement.setString(7, rrating);
			int rowsAffected = preparedStatement.executeUpdate();
			
			ResultSet getResto = statement.executeQuery("SELECT * FROM aish_restaurant WHERE name = '" + rname + "';");
			if(rowsAffected > 0 && getResto.next()) {
				restaurantInfo = getResto;
				String query = "UPDATE aish_users SET restaurant_id=? WHERE id=?"; 

				PreparedStatement preparedStatement2 = connection.prepareStatement(query);
				preparedStatement2.setString(1, getResto.getString("id"));
				preparedStatement2.setString(2, userId);
				
				int rowsAffected2 = preparedStatement2.executeUpdate();
				if (rowsAffected2 > 0) {
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Update.fxml"));
	    			Parent root = loader.load();
	    			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	                stage.setScene(new Scene(root));
				}
			}
			 preparedStatement.close();
			 connection.close();
		}
		 catch (Exception e) {
			 if (connection != null) {
					connection.rollback();
				}
			 comment.setText("Failed to add data.");
			 e.printStackTrace();
		}
    }
    
    public void gotoLogin(ActionEvent event) throws Exception {
		Connection connection;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent firstPage = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(firstPage));
		} catch (Exception e) {
			e.printStackTrace();
		}     
    }
}