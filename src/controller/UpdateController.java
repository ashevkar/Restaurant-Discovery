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

public class UpdateController implements Initializable {
	private ResultSet restaurantInfo;
	private String restaurantId;

    @FXML
    private TextField updatename;
    
    @FXML
    private TextField updatelocation;
	 
    @FXML
    private TextField updatecuisine;
    
    @FXML
    private TextField updaterating;
    
    @FXML
    private TextField updatetiming;
    
    @FXML
    private TextField updatecost;
    
    @FXML
    private TextField updatecontact;
    
    @FXML
    private Label comment;
    
	DBConnect conn = null;
    
    public UpdateController () {  
        conn = new DBConnect();
        }
    
    public void setRestaurantInfo(ResultSet resultSet) {
        this.restaurantInfo = resultSet;
    }
	    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	try {
    		if(restaurantInfo == null) {
    			setRestaurantInfo(controller.RestaurantLandingController.getRestaurantInfo());
    		}
    		if(restaurantInfo == null) {
    			setRestaurantInfo(controller.RestaurantController.getRestaurantInfo());
    		}
			updatename.setText(restaurantInfo.getString("name").toString());
			updatelocation.setText(restaurantInfo.getString("location").toString());
			updatecuisine.setText(restaurantInfo.getString("cuisine").toString());
			updatetiming.setText(restaurantInfo.getString("timings").toString());
			updatecontact.setText(restaurantInfo.getString("contact").toString());
			updatecost.setText(restaurantInfo.getString("average_cost").toString());
			updaterating.setText(restaurantInfo.getString("rating").toString());
			
			restaurantId = restaurantInfo.getString("id").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public void updateRestaurants(ActionEvent event) throws IOException {
		Connection connection;
		try {
			String rname = updatename.getText();
			String rlocation = updatelocation.getText();
			String rcuisine = updatecuisine.getText();
			String rrating = updaterating.getText();
			String rtiming = updatetiming.getText();
			String rcost = updatecost.getText();
			String rcontact = updatecontact.getText();

			connection = conn.connect();
	        String query = "UPDATE aish_restaurant SET name=?, location=?, cuisine=?, timings=?, contact=?, average_cost=?, rating=? WHERE id=?"; 

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, rname);
			preparedStatement.setString(2, rlocation);
			preparedStatement.setString(3, rcuisine);
			preparedStatement.setString(4, rtiming);
			preparedStatement.setString(5, rcontact);
			preparedStatement.setString(6, rcost);
			preparedStatement.setString(7, rrating);
			preparedStatement.setString(8, restaurantId);
			
			int rowsAffected = preparedStatement.executeUpdate();
			
			if (rowsAffected > 0) {
				preparedStatement.close();
				connection.close();
            	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RestaurantLanding.fxml"));
    			Parent root = loader.load();
    			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
            }
		}
		 catch (SQLException e) {
			comment.setText("Failed to update data.");
			e.printStackTrace();
		}
    }
    
    public void deleteRestaurants(ActionEvent event) throws IOException {
		Connection connection;
		try {
			connection = conn.connect();
	        String query = "DELETE FROM aish_restaurant WHERE id = " + restaurantId + ";"; 
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				preparedStatement.close();
				connection.close();
            	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RestaurantLanding.fxml"));
    			Parent root = loader.load();
    			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
            }
		} catch (Exception e) {
			comment.setText("Failed to remove restaurant.");
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