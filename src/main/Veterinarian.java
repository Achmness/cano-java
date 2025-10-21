
package main;

import config.config;
import java.util.Scanner;


public class Veterinarian {
    
    Scanner sc = new Scanner(System.in);
    config db = new config();
    int choice = 0;
        public static void viewAppointments() {
        config conf = new config();
        String votersQuery = "SELECT * FROM appointment";
        String[] votersHeaders = {"Appinment ID", "Pet ID", "Service", "Date", "Time", "Status"};
        String[] votersColumns = {"a_id", "a_pet_id", "a_service", "a_date", "a_time", "a_status"};
        
        conf.viewRecords(votersQuery, votersHeaders, votersColumns);
    } 
    public static void viewPetInformation() {
        config db = new config();

        String query = "SELECT " +
                       "C.a_id AS client_id, " +
                       "C.a_user AS client_name, " +
                       "C.a_email AS client_email, " +
                       "P.p_id AS pet_id, " +
                       "P.p_name AS pet_name, " +
                       "P.p_species AS pet_species, " +
                       "A.a_id AS appointment_id, " +
                       "A.a_service, " +
                       "A.a_date, " +
                       "A.a_time, " +
                       "A.a_status " +
                       "FROM account_tbl C " +
                       "LEFT JOIN pet P ON P.p_owner_id = C.a_id " +
                       "LEFT JOIN appointment A ON A.a_pet_id = P.p_id " +
                       "WHERE C.a_type = 'Client' " +
                       "ORDER BY C.a_id, P.p_id, A.a_id";

        String[] headers = {"Client ID", "Name", "Email", "Pet ID", "Pet Name", "Species", "Appointment ID", "Service", "Date", "Time", "Status"};
        String[] columns = {"client_id", "client_name", "client_email", "pet_id", "pet_name", "pet_species", "appointment_id", "a_service", "a_date", "a_time", "a_status"};

        db.viewRecords(query, headers, columns);
    }
public static void viewClientDetails() {
    config db = new config();

    // First, list all clients
    String listClientsQuery = "SELECT a_id AS client_id, a_user AS client_name, a_email AS client_email " +
                              "FROM account_tbl WHERE a_type = 'Client'";
    String[] listHeaders = {"Client ID", "Name", "Email"};
    String[] listColumns = {"client_id", "client_name", "client_email"};

    java.util.List<java.util.Map<String, Object>> clients = db.fetchRecords(listClientsQuery);
    if (clients.isEmpty()) {
        System.out.println("No clients found.");
        return;
    } else {
        db.viewRecords(listClientsQuery, listHeaders, listColumns);
    }

    // Ask user to select a client ID
    Scanner sc = new Scanner(System.in);
    int clientId;
    java.util.List<Integer> validClientIds = new java.util.ArrayList<>();
    for (java.util.Map<String,Object> c : clients) {
        validClientIds.add(Integer.parseInt(c.get("client_id").toString()));
    }

    while (true) {
        System.out.print("\nEnter Client ID to view their pets & appointments: ");
        if (!sc.hasNextInt()) {
            System.out.println("Invalid input! Enter a number only.");
            sc.next();
            continue;
        }
        clientId = sc.nextInt();
        if (validClientIds.contains(clientId)) break;
        System.out.println("Invalid Client ID! Please choose one from the list above.");
    }

    // Show selected client's pets and appointments
    String detailsQuery = "SELECT " +
                          "C.a_id AS client_id, " +
                          "C.a_user AS client_name, " +
                          "C.a_email AS client_email, " +
                          "P.p_id AS pet_id, " +
                          "P.p_name AS pet_name, " +
                          "P.p_species AS pet_species, " +
                          "A.a_id AS appointment_id, " +
                          "A.a_service, " +
                          "A.a_date, " +
                          "A.a_time, " +
                          "A.a_status " +
                          "FROM account_tbl C " +
                          "LEFT JOIN pet P ON P.p_owner_id = C.a_id " +
                          "LEFT JOIN appointment A ON A.a_pet_id = P.p_id " +
                          "WHERE C.a_type = 'Client' AND C.a_id = " + clientId + " " +
                          "ORDER BY P.p_id, A.a_id";

    String[] headers = {"Client ID", "Name", "Email", "Pet ID", "Pet Name", "Species", "Appointment ID", "Service", "Date", "Time", "Status"};
    String[] columns = {"client_id", "client_name", "client_email", "pet_id", "pet_name", "pet_species", "appointment_id", "a_service", "a_date", "a_time", "a_status"};

    db.viewRecords(detailsQuery, headers, columns);
}



        public static void listAllClients() {
    config db = new config();

    String query = "SELECT a_id AS client_id, a_user AS client_name, a_email AS client_email " +
                   "FROM account_tbl " +
                   "WHERE a_type = 'Client' " +
                   "ORDER BY a_id";

    String[] headers = {"Client ID", "Name", "Email"};
    String[] columns = {"client_id", "client_name", "client_email"};

    java.util.List<java.util.Map<String, Object>> result = db.fetchRecords(query);

    if(result.isEmpty()){
        System.out.println("\nNo clients found.");
    } else {
        db.viewRecords(query, headers, columns); 
    }
}



    public void veterinarian(){
        
        do{
            System.out.println("WELCOME TO VETERINARIAN DASHBOARD");
            System.out.println("1. View Appointments");
            System.out.println("2. Approved Appointment");
            System.out.println("3. View All Client Pet Information");
            System.out.println("4. View Specific Client Pet Information");
            System.out.println("5. Exit");
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
                    viewAppointments();    
                    break;
                    
                    case 2:
                    viewAppointments();
                    System.out.print("Enter ID to Approve: ");
                    int appID = sc.nextInt();

                    String sqls = "UPDATE appointment SET a_status = ? WHERE a_id = ?";
                    db.updateRecord(sqls, "Approved", appID);
                    break;
                    case 3:
                    viewPetInformation();    
                    break;
                    case 4:
                        listAllClients();
                        System.out.print("Enter Client ID: ");
                        int clientId = sc.nextInt();
                        viewClientDetails();
                        break;
                }
        
        
        }while(choice !=4);  
    }
    
}
