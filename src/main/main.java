
package main;

import config.config;
import java.util.Scanner;


public class main {
    public void client(int clientId, int petOwnerId) {
    this.client(clientId, petOwnerId);
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); 
        config db = new config();
        char cont;
        int choice = 0;
        
        
    do{    
        System.out.println("Main Menu");
        System.out.println("1. Register Account");
        System.out.println("2. Login Account");
        System.out.println("3. Exit");
            while (true) {
                System.out.print("Enter Choice (1-3): ");

                if (!sc.hasNextInt()) {
                    System.out.println("Invalid input! Please enter a number only.");
                    sc.next(); 
                    continue;
                }
                choice = sc.nextInt();
                
                if (choice >= 1 && choice <= 3) {
                    break; 
                } else {
                    System.out.println("Invalid input. Choose between 1-3.");
                }
            }
      
        switch(choice){
            case 1:
                System.out.print("Enter User Name: ");
                String userName = sc.next();
                System.out.print("Enter Email: ");
                String email = sc.next();
                
                    while(true){
                        
                        String qry = "SELECT * FROM account_tbl WHERE a_email = ?";
                        java.util.List<java.util.Map<String, Object>> result = db.fetchRecords(qry, email);
                        
                        if(result.isEmpty()){
                            break;
                        }else{
                            System.out.print("Email already exist, Enter other email: ");
                            email = sc.next();
                        }  
                    }
                    int utype = 0;
                    while(true) {
                        System.out.print("Enter User Type (1 - Client/ 2 - Veterinarian): ");
                        if (!sc.hasNextInt()) {
                            System.out.println("Invalid input! Please enter a number only.");
                            sc.next(); 
                            continue;
                        }
                        utype = sc.nextInt();
                        
                        if(utype >= 1 && utype <= 2){
                            break; 
                        } else {
                            System.out.println("Invalid input. Choose between 1-2.");
                        }
                        
                    }

                    String tp = "";
                    if(utype == 1){
                        tp = "Client";
                    }else{
                        tp = "Veterinarian";
                    }
                    
                    System.out.print("Enter Password: ");
                    String pass = sc.next();
                    
                    String hashedPass = db.hashPassword(pass);
                    String sql = "INSERT INTO account_tbl (a_user, a_email, a_type, a_status, a_pass) VALUES (?, ?, ?, ?, ?)";
                    db.addRecord(sql, userName, email, tp, "Pending", hashedPass);
            break;    
                
             
            case 2:
                System.out.print("Enter Email: ");
                String logEmail = sc.next();
                System.out.print("Enter Password: ");
                String logPass = sc.next();
                
                String hashPass = db.hashPassword(logPass);
                while(true){
                    
                    String qry = "SELECT * FROM account_tbl WHERE a_email = ? AND a_pass = ?";
                    java.util.List<java.util.Map<String, Object>> result = db.fetchRecords(qry, logEmail, hashPass);
                    
                    if(result.isEmpty()){
                        System.out.println("INVALID CREDENTIALS");
                        break;
                    }else{
                        java.util.Map<String, Object> user = result.get(0);
                        int petOwnerId = Integer.parseInt(user.get("a_id").toString());
                        int clientId = Integer.parseInt(user.get("a_id").toString());
                        String stat = user.get("a_status").toString();
                        String type = user.get("a_type").toString();
                        if(stat.equalsIgnoreCase("Pending")){
                                    System.out.println("Account is Pending, Contact the Admin!");
                                    break;
                                }else{
                                    System.out.println("LOGIN SUCCESS!");
                                   
                                    if(type.equals("Admin")){
                                                Admin admin = new Admin();
                                                admin.admin();
                                                     
                                    }else if(type.equalsIgnoreCase("Client")){
                                        Client client = new Client();
                                        client.client(clientId, petOwnerId);
    
                                    }else if(type.equalsIgnoreCase("Veterinarian")){
                                        Veterinarian veterinarian = new Veterinarian();
                                        veterinarian.veterinarian();
                                        
                                    }else{
                                        
                                        
                                    }
                                    break;
                                }
                    }
                }
            break;
            case 3:
                System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice.");
            break;

    
     
        }
            System.out.print("Do you want to continue? (Y/N): ");
            cont = sc.next().charAt(0);
            
    }while (cont == 'Y' || cont == 'y');
        
    }
    
}
    

