/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redsox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author jereckley
 */
public class FXMLController implements Initializable {
    @FXML
    private ToggleGroup sorting;
    @FXML
    private ToggleGroup ratings;
    @FXML 
    private TextField userName;
    @FXML 
    private TextField databaseName;
    @FXML 
    private TextField password;
    @FXML
    private RadioButton sortTopic;
    @FXML
    private RadioButton sortUser;
    @FXML
    private TextField messageNumberReply;
    @FXML
    private TextArea reply;
    @FXML
    private TextField messageDelete;
    @FXML
    private TextField messageRating;
    @FXML
    private RadioButton rate1;
    @FXML
    private RadioButton rate2;
    @FXML
    private RadioButton rate3;
    @FXML
    private RadioButton rate4;
    @FXML
    private RadioButton rate5;
    @FXML
    private TextArea newMessage;
    @FXML
    private TextField userNameLogin;
    @FXML
    private Label alertLabel;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private RadioButton sortDate;
            
   Connection con =null;
   int currentUser = 0;
   
    @FXML
    private ScrollPane sp;
    @FXML
    private TextField topic;
   
   
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    

    @FXML
    private void ConnectToDatabase(ActionEvent event) {
        String thePassword = password.getText();
        String theUserName = userName.getText();
        String theDatabase = databaseName.getText();
        
        System.out.println(thePassword);
        System.out.println(theUserName);
        System.out.println(theDatabase);
        con = JDBCConnection.connect(2,theUserName,thePassword,theDatabase);
    }


    @FXML
    private void replySubmit(ActionEvent event) {
         int messID;
        
        messID = Integer.valueOf(messageNumberReply.getText()); //fixthis
        
        String thread = ""; 
        
        
        Statement stmt = null;
	ResultSet rs = null;
        String query ="";
      
        
             
        do{
                
        
        
        thread = reply.getText();  //fixthis
        
        
                if(thread.length()>301){
                    alertLabel.setText("\n\nMessage body must be less than 300 characters\n\n");
                }else{
                alertLabel.setText("Reply Added");    
                query = "call replying('" + messID + "','" + thread + "'," + currentUser + ")";
                
                }
                
                }while(thread.length() > 301);
                
    
                try
                {

                        stmt = con.createStatement();

                        stmt.executeUpdate(query);

                               
                }

                catch (SQLException e)
                {
                        e.printStackTrace();
                        System.out.println("SQL Exception");				
                }

                finally {
                  
                }
    }

    @FXML
    private void deleteSubmit(ActionEvent event) {
        System.out.println("Enter message ID for deletion: ");
        
        int topicName = Integer.valueOf(messageDelete.getText()); //fixthis
         
        Statement stmt = null;
        ResultSet rs = null;
        String query ="";
        Statement stmt2 = null;
        ResultSet rs2 = null;
        String query2 ="";
        
        try
	{
			stmt = con.createStatement();
                        query = "select userID from Messages where messageID = " + topicName;
                        stmt2 = con.createStatement();
                        query2 = "delete from Messages where messageID =  ?";
                              
			
			rs  = stmt.executeQuery(query);
                        PreparedStatement preparedStmt = con.prepareStatement(query2);
                        preparedStmt.setInt(1, topicName);
 
                        
			if (rs.next()){
                            
                            System.out.println(rs.getInt("userID"));
                            if(rs.getInt("userID") == currentUser){
                            preparedStmt.execute();
                            alertLabel.setText("Message Deleted");
                            }else{
                            alertLabel.setText("You do not have permission to delete this message.");
                            }
                        }
                            
                            
			}
			
			
			
	
	
	catch (SQLException e)
	{
		e.printStackTrace();
		System.out.println("SQL Exception");				
	}
	
	finally 
	{
	  
	}
        
        
    }

    @FXML
    private void ratingSubmit(ActionEvent event) {
        System.out.println("Enter message ID for rating: ");
        
        int topicName = Integer.valueOf(messageRating.getText()); 
       
        Statement stmt = null;
	ResultSet rs = null;
        String query ="";
      
        RadioButton lc = (RadioButton) ratings.getSelectedToggle();
        int enterRating = Integer.valueOf(lc.getText());
        
        System.out.println(enterRating);
        
                    
                query = "call rating('" + topicName + "','" + enterRating + "')";
                
                
                
    
                try
                {

                        stmt = con.createStatement();

                        stmt.executeUpdate(query);

                               
                }

                catch (SQLException e)
                {
                        e.printStackTrace();
                        System.out.println("SQL Exception");				
                }

                finally {
                  
                }
    }

    @FXML
    private void newMessageSubmit(ActionEvent event) {
        Statement stmt = null;
        
        
        
        
            
              
                String addTopic = topic.getText(); //fixthis
                String addMessageBody = "";
                
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
	
                String query = "";
                
                do{
                
                addMessageBody = newMessage.getText();
                
                if(addMessageBody.length()>301){
                    alertLabel.setText("Message body must be less than 300 characters");
                }else{
                    
                query = "call messaging('" + currentUser + "','" + dateFormat.format(date) + "','" + addTopic + "','" + addMessageBody + "')";
                alertLabel.setText("Message added");
                }
                
                }while(addMessageBody.length() > 301);
                
    
                try
                {

                        stmt = con.createStatement();

                        stmt.executeUpdate(query);

                               
                }

                catch (SQLException e)
                {
                        e.printStackTrace();
                        System.out.println("SQL Exception");				
                }

                finally {
                  
                }
                
                
      
    
    }

    @FXML
    private void loginUser(ActionEvent event) {
        
        
          
       
        String userName = this.userNameLogin.getText();
        
        System.out.println("username");
        Statement stmt = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        
        String query4 = "call getUser('" + userName + "')";
        String query5 = "";
        try
	{
			stmt = con.createStatement();
                        stmt2 = con.createStatement();
                        stmt3 = con.createStatement();
			
			rs  = stmt.executeQuery(query4);
			
                        
			if (rs.next()) {
                            
                           currentUser = rs.getInt("userID");
                            
                            System.out.println("your logged in");
			}else{
                            
                            //this is where you can make hidden field visable 
                            System.out.println("user not registered");
                            
                            }
                        
			
			
			}
	
	
	catch (SQLException e)
	{
		e.printStackTrace();
		System.out.println("SQL Exception");				
	}
	
	finally 
	{
	  
	}
        
    }

    @FXML
    private void refresh(ActionEvent event) {
        
            RadioButton lc = (RadioButton) sorting.getSelectedToggle();
            String listChoice = String.valueOf(Character.toLowerCase(lc.getText().charAt(0)));
            System.out.println(listChoice);
        
            
            
            printView(con,listChoice);
           
            
            
                      
        
    }
    
    public void printView(Connection con, String listChoice)
    {
        
        Statement stmt = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
	ResultSet rs = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        String query ="";
        String query2 ="";
        String query3 ="";
        String output = "";
        
        
        if (listChoice.equalsIgnoreCase("t")){
            
	
	query = "call byMessages()";
		
        }else if (listChoice.equalsIgnoreCase("u")){
        query = "call byUsers()";
        }else if (listChoice.equalsIgnoreCase("d")){
        query = "call byDate()";
        }
	       
	try
	{
			stmt = con.createStatement();
                        stmt2 = con.createStatement();
                        stmt3 = con.createStatement();
			
			rs  = stmt.executeQuery(query);
			output = "";	
			System.out.println("\n\nMessages:");
			while (rs.next()) {
                            query2 = "call avgRatings('"+ rs.getInt("messageID") + "')";
                            query3 = "call callReplies('"+ rs.getInt("messageID") + "')";
                            rs2  = stmt2.executeQuery(query2);
                            rs3  = stmt3.executeQuery(query3);
                            
                            
                            String rating = "";
                            if(rs2.next()){
                            rating = Integer.toString(rs2.getInt("avgRating"));
                            }
                            
                            if(rating.equalsIgnoreCase("0")){
                            rating = "No Ratings";}
                           
                            output = output + "\n\nMessage Id: " + rs.getInt("messageID") + "\nMessage: " + rs.getString("message") + "\nTopic: " + rs.getString("topic") +  
                                    "\nUsername: " + rs.getString("userName") + "\nPost Date: " + rs.getDate("postDate") + "\nRating: " + rating;
                            while (rs3.next()) {
                            output = output + "\nReplies: " + rs3.getString("reply") +  "\nUsername: " + rs3.getString("userName");
                            }
                            
                            
			}
                        
			}
       
        
	
	
	catch (SQLException e)
	{
		e.printStackTrace();
		System.out.println("SQL Exception");				
	}
	
	finally 
	{
	  
	}
        
        setText(output);
    
    }

    @FXML
    private void registerUser(ActionEvent event) {
        
        String userName = this.userNameLogin.getText();
        
        
        Statement stmt2 = null;
        Statement stmt3 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String fName = firstName.getText();
        String lName = lastName.getText();
        
        String query5 = "";
        String query4 = "call getUser('" + userName + "')";
        
        try
	{
			
                        stmt2 = con.createStatement();
                        stmt3 = con.createStatement();
			
			
                        
                            
                            query5 = "call addUser('" + fName + "', '" + lName + "', '" + userName + "')";
                            stmt2.executeUpdate(query5);
                            rs2  = stmt3.executeQuery(query4);
                            if (rs2.next()) {
                            
                            currentUser = rs2.getInt("userID");
                                System.out.println("new user created");
                            
                            
                        }
			
			
			}
	
	
	catch (SQLException e)
	{
		e.printStackTrace();
		System.out.println("SQL Exception");				
	}
	
	finally 
	{
	  
	}
        
        
        
        
    }
    
    
    public void setText(String value){
        
        Label messagesPrintBox = new Label();
        messagesPrintBox.setText(value);
        sp.setContent(messagesPrintBox);
    }
    
}
