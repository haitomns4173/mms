package mms;
import java.sql.*;
import javax.swing.*;

public class mysql {
    static Connection connect = null;
    static Statement stmt = null;
    static ResultSet result = null;
    static boolean login_status = false;
    
    static String company_name;
    static String company_address;
    static String company_phoneNo;
    
    static String users_name;
    static String username;
    static String user_type;
    
    public static void main(String args[]) {
        String db_url = "jdbc:mysql://localhost:3306/pharma_knowhere";
        String db_username = "root";
        String db_password = "";  
        
        try {
            connect = DriverManager.getConnection(db_url, db_username, db_password);
        }
        catch(SQLException database_error_message){
            JOptionPane.showMessageDialog(null, database_error_message);
        }
    }
    
    public static void login_validator(String username, String password) throws SQLException{
        int number_of_rows = 0;
        stmt = connect.createStatement();
        String query = "SELECT * FROM `user` WHERE `username` LIKE '"+username+"' AND `password` LIKE '"+password+"'";
        result = stmt.executeQuery(query);
        while(result.next()) {
            number_of_rows++;
            loginPage.user_id_stored = result.getInt(1);
        }
        if(number_of_rows == 1){
            JOptionPane.showMessageDialog(null, "Login Successful");
            login_status = true;
        }
        else{
            JOptionPane.showMessageDialog(null, "Username or Password is Incorrect");
            login_status = false;
        }
    }
    
    public static void company_find_query() throws SQLException{
        stmt = connect.createStatement();
        String query = "SELECT `name`, `address`, `phone` FROM `company` WHERE 1";
        result = stmt.executeQuery(query);
        while(result.next()) {
            company_name = result.getString(1);
            company_address = result.getString(2);
            company_phoneNo = result.getString(3);
        }
    }
    
    public static void user_find_query() throws SQLException{
        stmt = connect.createStatement();
        String query = "SELECT * FROM `user` WHERE `username` LIKE '"+loginPage.username_stored+"'";
        result = stmt.executeQuery(query);
        while(result.next()) {
            users_name = result.getString(2);
            user_type = result.getString(3);
            username = result.getString(4);
        }
    }
    
    public static int user_update_query(String users_name_update, String username_update, String password_update, String username_to_change) throws SQLException{
        int username_duplicate = 0;
        int temp_user_id = 0;
        
        stmt = connect.createStatement(); 
        String query = "SELECT * FROM `user` WHERE `username` LIKE '"+username_update+"'";
        result = stmt.executeQuery(query);
        while(result.next()) {
            username_duplicate++;
            temp_user_id = result.getInt(1);
        }
        
        if(username_duplicate != 0){
            if(temp_user_id == loginPage.user_id_stored){
                username_duplicate = 0;
            }
            else{
                username_duplicate = 1;
            }
        }
        
        if(username_duplicate == 0){
            if(password_update.equals("password")){
                String update_query = "UPDATE `user` SET `name`='"+users_name_update+"',`username`='"+username_update+"' WHERE `username` = '"+username_to_change+"'";
                stmt.executeUpdate(update_query);
                return 0;
            }
            else{
                String update_query = "UPDATE `user` SET `name`='"+users_name_update+"',`username`='"+username_update+"',`password`='"+password_update+"' WHERE `username` = '"+username_to_change+"'";
                stmt.executeUpdate(update_query);
                return 0;
            }
        }
        else{
            return 1;
        }
    }
    
    public static void company_update_query(String shop_name_update, String shop_address_update, String shop_contact_update) throws SQLException{
        String update_query = "UPDATE `company` SET `name`='"+shop_name_update+"',`address`='"+shop_address_update+"',`phone`='"+shop_contact_update+"' WHERE 1";
        stmt.executeUpdate(update_query);
    }
    
    public static void auto_suggestion_medicine() throws SQLException{
        stmt = connect.createStatement();
        String query = "SELECT * FROM `medicine_list`";
        result = stmt.executeQuery(query);
        while(result.next()) {
            medicine_management.medicine_name_auto.add(result.getString(2));  
        }
    }
    
    public static void medicine_mrp() throws SQLException{
        stmt = connect.createStatement();
        String query = "SELECT * FROM `medicine_list` WHERE `medicine_name` LIKE '%"+medicine_management.medicine_with_under+"%'";
        result = stmt.executeQuery(query);
        while(result.next()) {
            medicine_management.medicine_price = result.getFloat(8);
        }
    }
}