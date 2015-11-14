package redsox;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */





import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Comparator;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.ListIterator;



public class MessageBoard {
    
   
    
    public static Scanner scan = new Scanner(System.in);
    
    public static void main(String[] args) {
        
        Connection con = JDBCConnection.connect();
        
        
        String listChoice;     
        String keepGoing = "yes";
        String addMessage = "no";
        String replyMessage = "no";
        String rateMessage = "no";
        String userName = "";
        int marker = 9;
        int currentUser = 0;
        Statement stmt = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        String fName ="";
        String lName = "";
        
        System.out.println("What is your user name? (if you don't have one enter the one you want)");
        userName = scan.nextLine();
        
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
                            System.out.println("You are not a registered user\n\nWhat is your first name?");
                            fName = scan.nextLine();
                            System.out.println("What is you last name?");
                            lName = scan.nextLine();
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
        
        
        while ((keepGoing.equalsIgnoreCase("yes"))){
            System.out.println("\n\n\nPrinting messages(how do you want them organized?)");
            System.out.println("List by Topic(T)    List by User(U)    List by Posting Date(D)");
            listChoice = scan.next();
            
            
            
            printView(con,listChoice);
            
            
            System.out.println("\n\n\nSelect from the following:\n"
                    + "1. RePrint Messages\n2. New Message\n3. Reply to a Message"
                    + "\n4. Rate a message\n5. Delete a Message\n6. End Program");
            
            marker = scan.nextInt();
            
            if(marker < 7 || marker>0){
            
           if (marker == 1){
               System.out.println("How you would like to organize and view these messages?");
            System.out.println("List by Topic(T)    List by User(U)    List by Posting Date(D)");
            listChoice = scan.next();
            printView(con,listChoice);}
           if (marker == 2){
            addNewMessages(currentUser,con);}
            
            if (marker == 3){
                replyToMessages(con, currentUser);
            }
            
            if (marker == 4){
                rateMessages(con,currentUser);
            }
            if (marker ==5 ){
                deleteMessages(con,currentUser);
            }
            if(marker ==6){
            if( con != null ) 
	  {
		  try { con.close(); 
			  }
		  catch( SQLException e ) 
		  {
			e.printStackTrace(); 
		  }
		  System.exit(0);
	  }
            }
            }
            
                      
        }        
    }
    
    // ADD MESSAGES
    //public static ArrayList<MessagesTestJ>theCurrentView addNewMessages(){
    public static void addNewMessages(int currentUser,Connection con){    
        Statement stmt = null;
        
        
        
        
            String keepAdding = "yes";
            while (keepAdding.equals("yes")){
                System.out.println("Topic: ");
                scan.nextLine();
                String addTopic = scan.nextLine();
                String addMessageBody = "";
                String addPostDate = "";
                
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
	
                String query = "";
                
                do{
                
                System.out.println("Message: ");
                addMessageBody = scan.nextLine();
                
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
                
                System.out.println("Add another message? ");
                keepAdding = scan.next();
                // Validate that they enter "yes" or "no" in upper or lower case.
                while ((!keepAdding.equalsIgnoreCase("yes"))&&(!keepAdding.equalsIgnoreCase("no"))){ 
                
                        //keepAdding = "yes";
                        System.out.println("Please enter upper or lower case yes or no.");
                        keepAdding = scan.next();}
                }
      
    }
    
    
    
    public static void replyToMessages(Connection con, int currentUser){
        int topicName;
        System.out.println("Enter message ID for reply: ");
        scan.nextLine();
        topicName = scan.nextInt();
        
        String thread = ""; 
        
        
        Statement stmt = null;
	ResultSet rs = null;
        String query ="";
      
        scan.nextLine();
             
        do{
                
        System.out.println("Enter your reply: ");
        
        thread = scan.nextLine();
        
        
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
    
    public static void rateMessages(Connection con, int currentUser){
//        printMessage(theCurrentView);
                    
        System.out.println("Enter message ID for rating: ");
        scan.nextLine();
        int topicName = scan.nextInt();
        int i = 0;
        Statement stmt = null;
	ResultSet rs = null;
        String query ="";
        
//        
        scan.nextLine();
        int enterRating = 0;
        do{
        System.out.println("Enter your rating(1-5): ");
        
        enterRating = scan.nextInt();
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
    
   
    public static void deleteMessages(Connection con,int currentUser){
        
        System.out.println("Enter message ID for deletion: ");
        scan.nextLine();
        int topicName = scan.nextInt();
         
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


