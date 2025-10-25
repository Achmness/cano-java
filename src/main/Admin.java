
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
            System.out.println("3. Delete Accounts");
            System.out.println("4. Exit");
                while (true) {
                    System.out.print("Enter Choice (1-3): ");

                    if (!sc.hasNextInt()) {
                        System.out.println("Invalid input! Please enter a number only.");
                        sc.next(); 
                        continue;
                    }
                    choice = sc.nextInt();

                    if (choice >= 1 && choice <= 4) {
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
                    int accId;
                        viewUsers();

                        java.util.List<java.util.Map<String, Object>> account = db.fetchRecords(
                            "SELECT a_id FROM account_tbl"
                        );

                        java.util.Set<Integer> validIds = new java.util.HashSet<>();
                        for (java.util.Map<String, Object> acc : account) {
                            validIds.add(Integer.parseInt(acc.get("a_id").toString()));
                        }

                        if (validIds.isEmpty()) {
                            System.out.println("There are no appointments to approve.");
                            break;
                        }
                        System.out.print("Enter Account ID to approve: ");
                        while (true) {
                            if (!sc.hasNextInt()) {
                                System.out.print("Invalid input! Please enter a number only: ");
                                sc.next(); 
                                continue;
                            }
                            accId = sc.nextInt();

                            if (validIds.contains(accId)) {
                                break;
                            } else {
                                System.out.print("Invalid ID! Please enter an existing appointment ID: ");
                            }
                        }
                        String sqlApprove = "UPDATE account_tbl SET a_status = ? WHERE a_id = ?";
                        db.updateRecord(sqlApprove, "Approved", accId);
                        System.out.println("Account approved successfully!");
                break;
                
                case 3:
                int aid;
                    viewUsers();
                        java.util.List<java.util.Map<String, Object>> accounts = db.fetchRecords(
                                    "SELECT a_id FROM account_tbl"
                                );
                        java.util.Set<Integer> validIdds = new java.util.HashSet<>();
                                for (java.util.Map<String, Object> accs : accounts) {
                                    validIdds.add(Integer.parseInt(accs.get("a_id").toString()));
                                }

                                if (validIdds.isEmpty()) {
                                    System.out.println("You have no Account to delete.");
                                    break;
                                }
                            System.out.print("Enter ID to Delete: ");
                            while (true) {
                                if (!sc.hasNextInt()) {
                                    System.out.print("Invalid input! Please enter a number only: ");
                                    sc.next(); 
                                    continue;
                                }
                                aid = sc.nextInt();

                                if(validIdds.contains(aid)){
                                    break;
                                }else{
                                    System.out.print("Invalid ID! Please enter one of your pet IDs: ");
                                }

                            }            

                            String sqlDelete = "DELETE FROM account_tbl WHERE a_id = ?";
                            db.deleteRecord(sqlDelete, aid);
                            System.out.println("Account Deleted successfully!");
                break;     
            }
        }while (choice != 4);   
    }
}
