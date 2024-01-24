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

public class LoginController implements Initializable {
    	private static String userId;
		DBConnect conn = null;
	
	    @FXML
	    private TextField username;

	    @FXML
	    private PasswordField password;

	    @FXML
	    private Button login;

	    @FXML
	    private Label wrongLogin;
	    
	    @FXML
        private ChoiceBox usertype;
	    
	    public LoginController() {  
	        conn = new DBConnect();
	    }
	    
	    public void setUserId(String data) {
	        this.userId = data;
	    }
	    public static String getUserId() {
	        return userId;
	    }

	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
	    	Connection connection;
	    	try {
	    		connection = conn.connect();    		
	    		Statement stmt_rest = connection.createStatement();
	    		Statement stmt_users = connection.createStatement();

	    		String restaurant = "CREATE TABLE IF NOT EXISTS `aish_restaurant` (\r\n"
    	      		+ " `id` int(5) NOT NULL AUTO_INCREMENT,\r\n"
    	      		+ " `name` varchar(30) NOT NULL,\r\n"
    	      		+ " `location` varchar(50) NOT NULL,\r\n"
    	      		+ " `cuisine` varchar(50) NOT NULL,\r\n"
    	      		+ " `timings` varchar(50) NOT NULL,\r\n"
    	      		+ " `contact` int(15) NOT NULL,\r\n"
    	      		+ " `average_cost` int(5) NOT NULL DEFAULT 0,\r\n"
    	      		+ " `rating` int(11) DEFAULT 5,\r\n"
    	      		+ " PRIMARY KEY (`id`)\r\n"
    	      		+ ") ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";

	    		String users = "CREATE TABLE IF NOT EXISTS `aish_users` (\r\n"
	    				+ " `id` int(11) NOT NULL AUTO_INCREMENT,\r\n"
	    				+ " `username` varchar(10) NOT NULL,\r\n"
	    				+ " `password` varchar(32) NOT NULL,\r\n"
	    				+ " `user_type` varchar(7) DEFAULT NULL,\r\n"
	    				+ " `name` varchar(30) DEFAULT NULL,\r\n"
	    				+ " `restaurant_id` int(5) DEFAULT NULL,\r\n"
	    				+ " PRIMARY KEY (`id`),\r\n"
	    				+ " UNIQUE KEY `username` (`username`),\r\n"
	    				+ " KEY `restaurant_id` (`restaurant_id`),\r\n"
	    				+ " CONSTRAINT `users_ibfk_1` FOREIGN KEY (`restaurant_id`) REFERENCES `aish_restaurant` (`id`)\r\n"
	    				+ ") ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;";

	    		stmt_rest.executeUpdate(restaurant);
	    		stmt_users.executeUpdate(users);
	    		System.out.println("Created tables in given database!");
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	usertype.getItems().addAll("USER", "MANAGER", "ADMIN");
	    }

	    public void gotoHome(ActionEvent event) throws IOException {
    		Connection connection;
			try {
				connection = conn.connect();
				Statement statement = connection.createStatement();
				String user = username.getText().toString();
				String pass = password.getText().toString();
				Object utypetemp = usertype.getValue();
				String utype = "";
				if(utypetemp != null) {
					utype = utypetemp.toString();
				}
				ResultSet resultSet = statement.executeQuery("SELECT id, username, password, user_type FROM aish_users where username = '" + user + "' AND user_type = '" + utype.toLowerCase() + "';");
				if(resultSet.next() && resultSet.getString("username").equals(user) && resultSet.getString("password").equals(controller.SignUpController.hashPassword(pass))) {
    	    		if(utype.equals("MANAGER") == true) {
    	    			userId = resultSet.getString("id").toString();
    	    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RestaurantLanding.fxml"));
        	            Parent secondPage = loader.load();
        	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        	            stage.setScene(new Scene(secondPage));
    	    		}
    	    		else if(utype.equals("ADMIN") == true) {
    	    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Admin.fxml"));
        	            Parent secondPage = loader.load();
        	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        	            stage.setScene(new Scene(secondPage));
    	    		}
    	    		else {
    	    			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Customer.fxml"));
        	            Parent secondPage = loader.load();
        	            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        	            stage.setScene(new Scene(secondPage));
    	    		}
    	    	}
    	    	else if(user == null) {
    	    		wrongLogin.setText("Please enter Username !");
    	    	}
    	    	else if(pass == null) {
    	    		wrongLogin.setText("Please enter Password !");
    	    	}
    	    	else if(utypetemp == null) {
    	    		System.out.println(user);
    	    		wrongLogin.setText("Please select User Type !");
    	    	}
    	    	else {
    	    		wrongLogin.setText("Wrong Username or Password");
    	    	}
		        connection.close();
			} catch (Exception e) {
				wrongLogin.setText("Error! Please try again later.");
				e.printStackTrace();
			}     
	    }
	    
	    public void gotoSignup(ActionEvent event) throws Exception {
	    	try {
	    		FXMLLoader sloader = new FXMLLoader(getClass().getResource("/view/SignUp.fxml"));
	            Parent signupPage = sloader.load();
	            Stage sstage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
	            sstage.setScene(new Scene(signupPage));
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }
}
		
