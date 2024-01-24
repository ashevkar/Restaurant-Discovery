package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DBConnect;

public class AdminController {
	private String restaurantId;
	
	@FXML
    private TextField searchboxAdmin;
    
    @FXML
    private ChoiceBox restlistAdmin;
    
    @FXML
    private Label errorlabelAdmin;
    
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

	DBConnect conn = null;
    public AdminController () {  
        conn = new DBConnect();
    }
    
    public void adminSearchRestaurants(ActionEvent event) throws IOException {
		Connection connection;
		try {
			restlistAdmin.getItems().clear();
			connection = conn.connect();
			Statement statement = connection.createStatement();
			String search_term = searchboxAdmin.getText().toString();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM aish_restaurant WHERE LOWER(name) LIKE LOWER('%" + search_term + "%');");
			while (resultSet.next()) {
				String item = resultSet.getString("name");
				restlistAdmin.getItems().add(item);
			}
	        connection.close();
		} catch (SQLException e) {
			errorlabelAdmin.setText("Restaurant not found.");
			e.printStackTrace();
		}
    }
    
    public void adminHandleRestListAction(ActionEvent event) throws Exception {
    	Connection connection;
		try {
			connection = conn.connect();
			Statement statement = connection.createStatement();
			Object rnametemp = restlistAdmin.getValue();
			String rname = "";
			if(rnametemp != null) {
				rname = rnametemp.toString();
			}
			ResultSet resultSet = statement.executeQuery("SELECT * FROM aish_restaurant WHERE name = '" + rname + "';");
			while (resultSet.next()) {
				updatename.setText(resultSet.getString("name").toString());
				updatelocation.setText(resultSet.getString("location").toString());
				updatecuisine.setText(resultSet.getString("cuisine").toString());
				updatetiming.setText(resultSet.getString("timings").toString());
				updatecontact.setText(resultSet.getString("contact").toString());
				updatecost.setText(resultSet.getString("average_cost").toString());
				updaterating.setText(resultSet.getString("rating").toString());
				
				restaurantId = resultSet.getString("id").toString();
			}
		} catch (Exception e) {
			errorlabelAdmin.setText("Error Occurred !");
			e.printStackTrace();
		}
    }
    
    public void updateRestaurant(ActionEvent event) throws IOException {
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
				errorlabelAdmin.setText("Data updated.");
            }
		}
		 catch (SQLException e) {
			errorlabelAdmin.setText("Failed to update data.");
			e.printStackTrace();
		}
    }
    
    public void deleteRestaurant(ActionEvent event) throws IOException {
		Connection connection;
		try {
			connection = conn.connect();
	        String query = "DELETE FROM aish_restaurant WHERE id = " + restaurantId + ";"; 
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			
			int rowsAffected = preparedStatement.executeUpdate();
			if (rowsAffected > 0) {
				preparedStatement.close();
				connection.close();
            	FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
    			Parent root = loader.load();
    			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
            }
		} catch (Exception e) {
			errorlabelAdmin.setText("Failed to remove restaurant.");
			e.printStackTrace();
		}
    }
    
    public void gotoLoginAdmin(ActionEvent event) throws IOException {
		Connection connection;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/Login.fxml"));
			Parent root = loader.load();
			Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
		} catch (Exception e) {
			e.printStackTrace();
		}     
    }
       
}