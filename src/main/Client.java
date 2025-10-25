
package main;

import config.config;
import java.util.Scanner;


public class Client {
    Scanner sc = new Scanner (System.in);
    config db = new config();
    int clientid;

    
    public static void viewAppointmentsForClient(int clientId) {
    config db = new config();

    String query = "SELECT A.a_id AS appointment_id, " +
                    "A.a_pet_id, " +
                    "A.a_service, " +
                    "A.a_date, " +
                    "A.a_time, " +
                    "A.a_status, " +
                    "C.a_user AS client_name, " +
                    "C.a_email AS client_email " +
                    "FROM appointment A " +
                    "INNER JOIN account_tbl C ON A.a_client_id = C.a_id " +
                    "WHERE C.a_id = " + clientId; 

    String[] headers = {"Appointment ID", "Pet ID", "Service", "Date", "Time", "Status", "Name", "Email"};
    String[] columns = {"appointment_id", "a_pet_id", "a_service", "a_date", "a_time", "a_status", "client_name", "client_email"};
    
    java.util.List<java.util.Map<String, Object>> result = db.fetchRecords(query);
    
        if (result.isEmpty()) {
                System.out.println("\nYou have no appointments yet.");
            } else {
                db.viewRecords(query, headers, columns);
            }
    }
    public static java.util.List<Integer> viewPetInformation(int petOwnerId) {
        config db = new config();

        String query = "SELECT P.p_id AS pet_id, " +
            "P.p_name, " +
            "P.p_age, " +
            "P.p_species, " +
            "P.p_breed, " +
            "P.p_dateBirth, " +
            "P.p_gender, " +
            "P.p_weight, " +
            "P.p_notes, " +
            "C.a_user AS owner_name, " +
            "C.a_email AS owner_email " +
            "FROM pet P " +
            "INNER JOIN account_tbl C ON P.p_owner_id = C.a_id " +
            "WHERE C.a_id = " + petOwnerId;

        String[] headers = {"ID", "Pet Name", "Age", "Species", "Breed", "Date Of Birth", "Gender", "Weight", "Notes", "Name", "Email"};
        String[] columns = {"pet_id", "p_name", "p_age", "p_species", "p_breed", "p_dateBirth", "p_gender", "p_weight", "p_notes", "owner_name", "owner_email"};

        java.util.List<java.util.Map<String, Object>> result = db.fetchRecords(query);
        java.util.List<Integer> petIds = new java.util.ArrayList<>();

        if (result.isEmpty()) {
            System.out.println("\nYou have no Pet Information yet.");
        } else {
            db.viewRecords(query, headers, columns);

            
            for (java.util.Map<String, Object> row : result) {
                petIds.add(Integer.parseInt(row.get("pet_id").toString()));
            }
        }
        return petIds;
    }




    public void client(int clientId, int petOwnerId){
        int choice;
        int pchoice;
        int achoice;
        int id;
        int pid;
        int aid;
        
        do{
            System.out.println("WELCOME TO CLIENT DASHBOARD");
            System.out.println("1. Manage Pet");
            System.out.println("2. Book Appointment");
            System.out.println("3. Manage My Appointments");
            System.out.println("4. ");
            System.out.println("5. ");
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
                do{
                    
                    System.out.println("1. Add Pet Information");
                    System.out.println("2. View Pet Information");
                    System.out.println("3. Update Pet Information");
                    System.out.println("4. Delete Pet Information");
                    System.out.println("5. Exit");
                        while(true){
                            System.out.print("Enter Choice (1-5): ");

                            if (!sc.hasNextInt()) {
                                System.out.println("Invalid input! Please enter a number only.");
                                sc.next(); 
                                continue;                              
                            }    
                            pchoice = sc.nextInt();

                            if (pchoice >= 1 && pchoice <= 5) {
                                break; 
                            } else {
                                System.out.println("Invalid input. Choose between 1-5.");
                            }
                        }
                    
                    switch(pchoice){
                        case 1:
                            System.out.print("Enter Pet Name: ");
                            String pet = sc.next();
                            System.out.print("Enter Pet Age: ");
                            int age = sc.nextInt();
                            System.out.print("Enter Pet Species: ");
                            String species = sc.next();
                            System.out.print("Enter Pet Breed: ");
                            String breed = sc.next();
                            System.out.print("Enter Date Of Birth: ");
                            String birth = sc.next();
                            System.out.print("Enter Pet Gender: ");
                            String gender = sc.next();
                            System.out.print("Enter Pet Weight: ");
                            float weight =sc.nextFloat();
                            System.out.print("Enter Pet Notes: ");
                            String notes = sc.next();
                            
                            String sql = "INSERT INTO pet (p_owner_id, p_name, p_age, p_species, p_breed, p_dateBirth, p_gender, p_weight, p_notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            db.addRecord(sql, petOwnerId, pet, age, species, breed, birth, gender, weight, notes);
                            System.out.println("Pet Information Added Successfully!");
                            
                        break;
                        case 2:
                            Client.viewPetInformation(petOwnerId);
                        break;
                        case 3:
                            Client.viewPetInformation(petOwnerId);
                            java.util.List<java.util.Map<String, Object>> pets = db.fetchRecords(
                                        "SELECT p_id FROM pet WHERE p_owner_id = ?", petOwnerId
                                    );
                            java.util.Set<Integer> validIds = new java.util.HashSet<>();
                                    for (java.util.Map<String, Object> pett : pets) {
                                        validIds.add(Integer.parseInt(pett.get("p_id").toString()));
                                    }

                                    if (validIds.isEmpty()) {
                                        System.out.println("You have no pet information to update.");
                                        break;
                                    }
                                System.out.print("Enter ID to update: ");
                                while (true) {
                                    if (!sc.hasNextInt()) {
                                        System.out.print("Invalid input! Please enter a number only: ");
                                        sc.next(); 
                                        continue;
                                    }
                                    pid = sc.nextInt();
                                    
                                    if(validIds.contains(pid)){
                                        break;
                                    }else{
                                        System.out.print("Invalid ID! Please enter one of your pet IDs: ");
                                    }
                                    
                                }            
                                System.out.print("Enter New Age: ");
                                int nAge = sc.nextInt();
                                System.out.print("Enter New Weight: ");
                                float nWeight = sc.nextFloat();
                                
                                String sqlUpdate = "UPDATE pet SET p_age = ?, p_weight = ? WHERE p_id = ?";
                                db.updateRecord(sqlUpdate, nAge, nWeight, pid);
                                System.out.println("Appointment updated successfully!");
                            
                        break;
                        case 4:
                            Client.viewPetInformation(petOwnerId);
                            java.util.List<java.util.Map<String, Object>> petss = db.fetchRecords(
                                        "SELECT p_id FROM pet WHERE p_owner_id = ?", petOwnerId
                                    );
                            java.util.Set<Integer> validIdds = new java.util.HashSet<>();
                                    for (java.util.Map<String, Object> pettt : petss) {
                                        validIdds.add(Integer.parseInt(pettt.get("p_id").toString()));
                                    }

                                    if (validIdds.isEmpty()) {
                                        System.out.println("You have no pet information to delete.");
                                        break;
                                    }
                                System.out.print("Enter ID to update: ");
                                while (true) {
                                    if (!sc.hasNextInt()) {
                                        System.out.print("Invalid input! Please enter a number only: ");
                                        sc.next(); 
                                        continue;
                                    }
                                    pid = sc.nextInt();
                                    
                                    if(validIdds.contains(pid)){
                                        break;
                                    }else{
                                        System.out.print("Invalid ID! Please enter one of your pet IDs: ");
                                    }
                                    
                                }            
                                
                                String sqlDelete = "DELETE FROM pet WHERE p_id = ?";
                                db.deleteRecord(sqlDelete, pid);
                                System.out.println("Pet Information Deleted successfully!");
                            break;
                        
                    }
                }while(pchoice != 5);    
                    
                break;
                case 2:
                    java.util.List<Integer> petIds = Client.viewPetInformation(petOwnerId);

                    if (!petIds.isEmpty()) {
                        int petId;

                        while (true) {
                            System.out.print("Enter Pet Id: ");
                            if (!sc.hasNextInt()) {
                                System.out.print("Invalid input! Please enter a number only: ");
                                sc.next();
                                continue;
                            }

                            petId = sc.nextInt();
                            if (petIds.contains(petId)) {
                                break; 
                            } else {
                                System.out.print("Invalid Pet ID! Please enter one from the list above: ");
                            }
                        }

                        System.out.print("Service Type: ");
                        String service = sc.next();
                        System.out.print("Enter Date(YYYY-MM-DD): ");
                        String date = sc.next();
                        System.out.print("Enter Time(HH:MM AM/PM): ");
                        String time = sc.next();

                        String sql = "INSERT INTO appointment (a_client_id, a_pet_id, a_service, a_date, a_time, a_status) VALUES (?, ?, ?, ?, ?, ?)";
                        db.addRecord(sql, clientId, petId, service, date, time, "Pending");
                        System.out.println("Appointment Added Successfully!");
                    } else {
                        System.out.println("Please add a pet first before booking an appointment.");
                    }


                break;
                
                case 3:
                do{    
                    System.out.println("1. Reschedule an Appointment");    
                    System.out.println("2. Delete an Appointment");
                    System.out.println("3. View Details & Confirmation");
                    System.out.println("4. Exit");
                        while (true) {
                        System.out.print("Enter Choice (1-4): ");

                        if (!sc.hasNextInt()) {
                            System.out.println("Invalid input! Please enter a number only.");
                            sc.next(); 
                            continue;
                        }
                        achoice = sc.nextInt();

                        if (achoice >= 1 && achoice <= 4) {
                            break; 
                        } else {
                            System.out.println("Invalid input. Choose between 1-3.");
                        }
                        }
                   
                        
                        switch(achoice){
                            case 1:
                                Client.viewAppointmentsForClient(clientId);
                                    java.util.List<java.util.Map<String, Object>> appointments = db.fetchRecords(
                                        "SELECT a_id FROM appointment WHERE a_client_id = ?", clientId
                                    );
                                    java.util.Set<Integer> validIds = new java.util.HashSet<>();
                                    for (java.util.Map<String, Object> appt : appointments) {
                                        validIds.add(Integer.parseInt(appt.get("a_id").toString()));
                                    }

                                    if (validIds.isEmpty()) {
                                        System.out.println("You have no appointments to reschedule.");
                                        break;
                                    }
                                System.out.print("Enter ID to update: ");
                                while (true) {
                                    if (!sc.hasNextInt()) {
                                        System.out.print("Invalid input! Please enter a number only: ");
                                        sc.next(); 
                                        continue;
                                    }
                                    id = sc.nextInt();
                                    
                                    if(validIds.contains(id)){
                                        break;
                                    }else{
                                        System.out.print("Invalid ID! Please enter one of your appointment IDs: ");
                                    }
                                    
                                }            
                                System.out.print("Enter New Date: ");
                                String nDate = sc.next();
                                System.out.print("Enter New Time: ");
                                String nTime = sc.next();
                                
                                String sqlUpdate = "UPDATE appointment SET a_date = ?, a_time = ? WHERE a_id = ?";
                                db.updateRecord(sqlUpdate, nDate, nTime, id);
                                System.out.println("Appointment updated successfully!");
 
                            break;
                            case 2: 
                                Client.viewAppointmentsForClient(clientId);
                            java.util.List<java.util.Map<String, Object>> appoint = db.fetchRecords(
                                        "SELECT a_id FROM appointment WHERE a_client_id = ?", clientId
                                    );
                            java.util.Set<Integer> validIdds = new java.util.HashSet<>();
                                    for (java.util.Map<String, Object> apps : appoint) {
                                        validIdds.add(Integer.parseInt(apps.get("a_id").toString()));
                                    }

                                    if (validIdds.isEmpty()) {
                                        System.out.println("You have no Appointment to delete.");
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
                                
                                String sqlDelete = "DELETE FROM appointment WHERE a_id = ?";
                                db.deleteRecord(sqlDelete, aid);
                                System.out.println("Appointment Deleted successfully!");
                            break;
                            case 3:
                                    Client.viewAppointmentsForClient(clientId);    
                            break;
                            
                        }
                }while(achoice != 4);    
                
                break;
            }
        }while(choice !=4);
        
    }
    
}
