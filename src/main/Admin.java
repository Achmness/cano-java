
package main;



import config.config;
import java.util.Scanner;

public class Admin {
    Scanner sc = new Scanner (System.in);
    config db = new config();
    int choice = 0;
    
        public static void viewUsers() {
        config conf = new config();
        String votersQuery = "SELECT * FROM account_tbl";
        String[] votersHeaders = {"ID", "Name", "Email", "Type", "Status"};
        String[] votersColumns = {"a_id", "a_user", "a_email", "a_type", "a_status"};
        
        conf.viewRecords(votersQuery, votersHeaders, votersColumns);
    } 
    public void admin(){
        
        do{
            System.out.println("WELCOME TO ADMIN DASHBOARD");
            System.out.println("1. View Users");
            System.out.println("2. Approve Accounts");
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
                    viewUsers();
                    
                break;
                case 2:
                    viewUsers();
                    System.out.print("Enter ID to Approve: ");
                    int appID = sc.nextInt();

                    String sqls = "UPDATE account_tbl SET a_status = ? WHERE a_id = ?";
                    db.updateRecord(sqls, "Approved", appID);
                    
                break;
                    
                
            }
        }while (choice != 3);   
    }
}
