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

public class RestaurantLandingController {
	private static String userId;
	private static ResultSet restaurantInfo;
	
    @FXML
    private Label landinglabel;
    
	DBConnect conn = null;
    public RestaurantLandingController () {
    	conn = new DBConnect();
    }
    
    public void setUserId(String data) {
        this.userId = data;
    }
    public static String getUserId() {
        return userId;
    }
    public static ResultSet getRestaurantInfo() {
        return restaurantInfo;
    }
     
    public void addRestaurantAction(ActionEvent event) throws Exception {
    	Connection connection;
		try {
			setUserId(controller.LoginController.getUserId());
			connection = conn.connect();
			Statement statement = connection.createStatement();
		    ResultSet resultSet = statement.executeQuery("select restaurant_id from aish_users where id = " + userId + ";");
		    if(resultSet.next() && resultSet.getString("restaurant_id") == null) {
		    	FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Restaurant.fxml"));
	            Parent adminPage = loader.load();
	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	            stage.setScene(new Scene(adminPage));
		    } else {
		    	ResultSet restInfo = statement.executeQuery("select * from aish_restaurant where id = '" + resultSet.getString("restaurant_id") + "';");
	            if(restInfo.next() && restInfo.getString("name") != null) {
	            	restaurantInfo = restInfo;
	            }
		    	FXMLLoader loaderUpdate = new FXMLLoader(getClass().getResource("../view/Update.fxml"));
	            Parent adminUpdatePage = loaderUpdate.load();
	            Stage stageUpdate = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	            stageUpdate.setScene(new Scene(adminUpdatePage));
		    }
		    connection.close();
		}
		 catch (Exception e) {
			landinglabel.setText("Failed to add data.");
			e.printStackTrace();
		}
    }

    public void updateRestaurantAction(ActionEvent event) throws Exception {
    	Connection connection;
    	try {
    		setUserId(controller.LoginController.getUserId());
    		connection = conn.connect();
			Statement statement = connection.createStatement();
		    ResultSet resultSet = statement.executeQuery("select restaurant_id from aish_users where id = " + userId + ";");
		    if(resultSet.next() && resultSet.getString("restaurant_id") == null) {
		    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Update.fxml"));
	            Parent adminPage = loader.load();
	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	            stage.setScene(new Scene(adminPage));
		    } else {
		    	ResultSet restInfo = statement.executeQuery("select * from aish_restaurant where id = '" + resultSet.getString("restaurant_id") + "';");
	            if(restInfo.next() && restInfo.getString("name") != null) {
	            	restaurantInfo = restInfo;
	            }
		    	FXMLLoader loaderUpdate = new FXMLLoader(getClass().getResource("/view/Restaurant.fxml"));
	            Parent adminUpdatePage = loaderUpdate.load();
	            Stage stageUpdate = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	            stageUpdate.setScene(new Scene(adminUpdatePage));
		    }
		    connection.close();
		} catch (Exception e) {
			landinglabel.setText("Failed to redirect.");
			e.printStackTrace();
		}
    }

    public void deleteRestaurantAction(ActionEvent event) throws Exception {
    	Connection connection=null;
    	try {
    		setUserId(controller.LoginController.getUserId());
		    connection = conn.connect();
		    connection.setAutoCommit(false);
		    Statement statement = connection.createStatement();
		    ResultSet restId = statement.executeQuery("SELECT restaurant_id from aish_users where id = " + userId + ";");
		    String uquery = "DELETE FROM aish_users WHERE id = " + userId + ";";	    
		    PreparedStatement upreparedStatement = connection.prepareStatement(uquery);
		    int urowsAffected = upreparedStatement.executeUpdate();
		    if (urowsAffected > 0) {
		    	upreparedStatement.close();
		    	int rrowsAffected = 0;
		    	if (restId.next()) {
		    	    String rquery = "DELETE FROM aish_restaurant WHERE id = " + restId.getString("restaurant_id") + ";"; 
					PreparedStatement rpreparedStatement = connection.prepareStatement(rquery);
					rrowsAffected = rpreparedStatement.executeUpdate();
		    	}
				if (rrowsAffected > 0) {
					connection.commit();
					connection.close();
	            	FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
	    			Parent root = loader.load();
	    			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	                stage.setScene(new Scene(root));
	            }
		    }
		} catch (Exception e) {
			if (connection != null) {
				connection.rollback();
			}
			landinglabel.setText("Failed to delete restaurant listing.");
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