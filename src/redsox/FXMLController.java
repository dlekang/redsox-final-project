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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

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
    private TextArea messagesPrintBox;
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
         int topicName;
        System.out.println("Enter message ID for reply: ");
       
        topicName = 0; //fixthis
        
        String thread = ""; 
        
        
        Statement stmt = null;
	ResultSet rs = null;
        String query ="";
      
        
             
        do{
                
        System.out.println("Enter your reply: ");
        
        thread = "Fix this";  //fixthis
        
        
                if(thread.length()>301){
                    System.err.println("\n\nMessage body must be less than 300 characters\n\n");
                }else{
                    
                query = "call replying('" + topicName + "','" + thread + "'," + currentUser + ")";
                
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
        
        int topicName = 0; //fixthis
         
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
                            System.out.println("test");
                            System.out.println(rs.getInt("userID"));
                            if(rs.getInt("userID") == currentUser){
                            preparedStmt.execute();
                            System.out.println("Message Deleted");
                            }else{
                            System.out.println("\n\nYou do not have permission to delete this message.\n\n");
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
        
        int topicName = 0; //fixthis
        int i = 0;
        Statement stmt = null;
	ResultSet rs = null;
        String query ="";
      
        
        int enterRating = 0;
        do{
        System.out.println("Enter your rating(1-5): ");
        
        enterRating = 0; //fixthis
        }while(!(enterRating>0) || !(enterRating<6));
                    
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
        
        
        
        
            String keepAdding = "yes";
            while (keepAdding.equals("yes")){
                System.out.println("Topic: ");
              
                String addTopic = "fix this"; //fixthis
                String addMessageBody = "";
                String addPostDate = "";
                
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
	
                String query = "";
                
                do{
                
                System.out.println("Message: ");
                addMessageBody = "fix this"; //fixthis
                
                if(addMessageBody.length()>301){
                    System.err.println("\n\nMessage body must be less than 300 characters\n\n");
                }else{
                    
                query = "call messaging('" + currentUser + "','" + dateFormat.format(date) + "','" + addTopic + "','" + addMessageBody + "')";
                
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
    }

    @FXML
    private void loginUser(ActionEvent event) {
        
        
          
        String keepGoing = "yes";
        String addMessage = "no";
        String replyMessage = "no";
        String rateMessage = "no";
        String userName = "";
        
        
        Statement stmt = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String fName ="";
        String lName = "";
        
        System.out.println("What is your user name? (if you don't have one enter the one you want)");
        userName = "fix this"; //fixthis
        
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
                            
                            
			}else{
                            
                            query5 = "call addUser('" + fName + "', '" + lName + "', '" + userName + "')";
                            stmt2.executeUpdate(query5);
                            rs2  = stmt3.executeQuery(query4);
                            if (rs2.next()) {
                            
                            currentUser = rs2.getInt("userID");
                            
                            
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
    private void refresh(ActionEvent event) {
        
            String listChoice;   
        
            listChoice = "fix this"; //fixthis
            int marker = 9;
            
            
            printView(con,listChoice);
            
            
            System.out.println("\n\n\nSelect from the following:\n"
                    + "1. RePrint Messages\n2. New Message\n3. Reply to a Message"
                    + "\n4. Rate a message\n5. Delete a Message\n6. End Program");
            
            marker = 0; //fixthis
            
            if(marker < 7 || marker>0){
            
               System.out.println("How you would like to organize and view these messages?");
            System.out.println("List by Topic(T)    List by User(U)    List by Posting Date(D)");
            listChoice = "fix this";  //fixthis
            printView(con,listChoice);
           
            
            
                      
        }
    }
    
    public static void printView(Connection con, String listChoice)
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
                           
                            System.out.println("\n\nMessage Id: " + rs.getInt("messageID") + "\nMessage: " + rs.getString("message") + "\nTopic: " + rs.getString("topic") +  
                                    "\nUsername: " + rs.getString("userName") + "\nPost Date: " + rs.getDate("postDate") + "\nRating: " + rating);
                            while (rs3.next()) {
                            System.out.println("\nReplies: " + rs3.getString("reply") +  "\nUsername: " + rs3.getString("userName"));
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
    
}
