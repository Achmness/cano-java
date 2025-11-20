
package main;

import config.config;
import java.util.Scanner;


public class Client {
    Scanner sc = new Scanner (System.in);
    config db = new config();
    int clientid;

    public static java.util.Map<String, String> validateAppointment(config db, Scanner sc, int petId) {

    java.util.Map<String, String> result = new java.util.HashMap<>();

    java.time.LocalTime open = java.time.LocalTime.parse("08:00");  
    java.time.LocalTime close = java.time.LocalTime.parse("18:00"); 

    String date;
    String time;

    sc.nextLine(); 

    while (true) {
        System.out.print("Enter Date (YYYY-MM-DD): ");
        date = sc.nextLine().trim();

        if (!date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            System.out.println("Invalid date format! Use YYYY-MM-DD.");
            continue;
        }

        try {
            java.time.LocalDate input = java.time.LocalDate.parse(date);
            java.time.LocalDate today = java.time.LocalDate.now();

            if (input.isBefore(today)) {
                System.out.println("You cannot select a past date.");
                continue;
            }

            break;

        } catch (Exception e) {
            System.out.println("Invalid date! Enter a real calendar date.");
        }
    }

    String petCheck = "SELECT * FROM appointment WHERE a_pet_id = ? AND a_date = ?";
    java.util.List<java.util.Map<String, Object>> petExisting =
        db.fetchRecords(petCheck, petId, date);

    if (!petExisting.isEmpty()) {
        System.out.println("This pet already has an appointment on this date!");
        result.put("invalid", "1");
        return result;
    }

    while (true) {
        System.out.print("Enter Time (HH:MM AM/PM): ");
        time = sc.nextLine().trim();

        if (!time.matches("^(0[1-9]|1[0-2]):[0-5][0-9]\\s(AM|PM)$")) {
            System.out.println("Invalid format!(HH:MM AM/PM)");
            continue;
        }

        try {
            java.time.format.DateTimeFormatter formatter =
                    java.time.format.DateTimeFormatter.ofPattern("hh:mm a");

            java.time.LocalTime inputTime =
                    java.time.LocalTime.parse(time, formatter);


            if (inputTime.isBefore(open) || inputTime.isAfter(close)) {
                System.out.println("Time must be between 08:00 AM and 06:00 PM.");
                continue;
            }

            java.time.LocalDate today = java.time.LocalDate.now();
            java.time.LocalDate inputDate = java.time.LocalDate.parse(date);

            if (inputDate.equals(today)) {
                java.time.LocalTime now = java.time.LocalTime.now();
                if (inputTime.isBefore(now)) {
                    System.out.println("You cannot choose a past time today.");
                    continue;
                }
            }

            String sqlCheck = "SELECT * FROM appointment WHERE a_date = ? AND a_time = ?";
            java.util.List<java.util.Map<String, Object>> existing =
                db.fetchRecords(sqlCheck, date, time);

            if (!existing.isEmpty()) {
                System.out.println("This time slot is already booked. Pick another.");
                continue;
            }

            result.put("date", date);
            result.put("time", time);
            return result;

        } catch (Exception e) {
            System.out.println("Invalid time! Try again.");
        }
    }
}

    public static void viewAppointmentsForClient(int clientId) {
        config db = new config();

        String query = "SELECT A.a_id AS appointment_id, " +
                       "A.a_pet_id, " +
                       "A.a_service, " +
                       "A.a_date, " +
                       "A.a_time, " +
                       "A.a_status " +
                       "FROM appointment A " +
                       "WHERE A.a_client_id = " + clientId;

        String[] headers = {"Appointment ID", "Pet ID", "Service", "Date", "Time", "Status"};
        String[] columns = {"appointment_id", "a_pet_id", "a_service", "a_date", "a_time", "a_status"};

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
            "P.p_notes " +
            "FROM pet P " +
            "WHERE P.p_owner_id = " + petOwnerId;

        java.util.List<java.util.Map<String, Object>> pets = db.fetchRecords(query);
        java.util.List<Integer> petIds = new java.util.ArrayList<>();

        if (pets.isEmpty()) {
            System.out.println("\nThis client has no registered pets.");
        } else {
            System.out.println("\n--- PET INFORMATION ---");
            for (java.util.Map<String, Object> pet : pets) {
                System.out.println("Pet ID: " + pet.get("pet_id"));
                System.out.println("Name: " + pet.get("p_name"));
                System.out.println("Age: " + pet.get("p_age"));
                System.out.println("Species: " + pet.get("p_species"));
                System.out.println("Breed: " + pet.get("p_breed"));
                System.out.println("Date of Birth: " + pet.get("p_dateBirth"));
                System.out.println("Gender: " + pet.get("p_gender"));
                double weight = Double.valueOf(pet.get("p_weight").toString());
                System.out.printf("Weight: %.2f%n", weight);    
                System.out.println("Notes: " + pet.get("p_notes"));
                System.out.println("------------------------");

                petIds.add(Integer.parseInt(pet.get("pet_id").toString()));
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
        int age;
        float weight;
        int nAge;
        float nWeight;
        do{
            System.out.println("|=======================================|");
            System.out.println("|         🐾 CLIENT DASHBOARD 🐾          |");
            System.out.println("|=======================================|");
            System.out.println("|  1. Manage Pet                        |");
            System.out.println("|  2. Book Appointment                  |");
            System.out.println("|  3. Manage My Appointments            |");
            System.out.println("|  4. Log Out                           |");
            System.out.println("|=======================================|");

                while (true) {
                    System.out.print("Enter Choice (1-4): ");

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
                            while (true) {
                                System.out.print("Enter Pet Age: ");

                                if (!sc.hasNextInt()) {
                                    System.out.println("Invalid input! Please enter a number only.");
                                    sc.next(); 
                                    continue;
                                }
                                age = sc.nextInt();

                                if (choice >= 1 && choice <= 50) {
                                    break; 
                                } else {
                                    System.out.println("Invalid input.The pet age is only between 1-50.");
                                }
                            }
                            System.out.print("Enter Pet Species: ");
                            String species = sc.next();
                            System.out.print("Enter Pet Breed: ");
                            String breed = sc.next();
                            System.out.print("Enter Date Of Birth: ");
                            String birth = sc.next();
                            System.out.print("Enter Pet Gender: ");
                            String gender = sc.next();
                            while (true) {
                                System.out.print("Enter Pet Weight: ");

                                if (!sc.hasNextInt()) {
                                    System.out.println("Invalid input! Please enter a number only.");
                                    sc.next(); 
                                    continue;
                                }
                                weight = sc.nextInt();

                                if (choice >= 1 && choice <= 50) {
                                    break; 
                                } else {
                                    System.out.println("Invalid input.The pet weight is only between 1-50.");
                                }
                            }
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
                                while (true) {
                                System.out.print("Enter Pet Age: ");

                                if (!sc.hasNextInt()) {
                                    System.out.println("Invalid input! Please enter a number only.");
                                    sc.next(); 
                                    continue;
                                }
                                nAge = sc.nextInt();

                                if (choice >= 1 && choice <= 50) {
                                    break; 
                                } else {
                                    System.out.println("Invalid input.The pet age is only between 1-50.");
                                }
                                }
                                while (true) {
                                System.out.print("Enter Pet Weight: ");

                                if (!sc.hasNextInt()) {
                                    System.out.println("Invalid input! Please enter a number only.");
                                    sc.next(); 
                                    continue;
                                }
                                nWeight = sc.nextInt();

                                if (choice >= 1 && choice <= 50) {
                                    break; 
                                } else {
                                    System.out.println("Invalid input.The pet weight is only between 1-50.");
                                }
                                }
                                
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
                System.out.print("Invalid Pet ID! Choose from the list above: ");
            }
        }

        System.out.print("Service Type: ");
        String service = sc.next();

        java.util.Map<String, String> schedule = validateAppointment(db, sc, petId);

        if (schedule.containsKey("invalid")) {
            System.out.println("Appointment creation failed.");
            break;
        }

        String date = schedule.get("date");
        String time = schedule.get("time");

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
                    System.out.println("2. Cancel an Appointment");
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
                           
                                java.util.List<java.util.Map<String, Object>> apptData =
                                    db.fetchRecords("SELECT a_pet_id FROM appointment WHERE a_id = ?", id);

                                int petId = Integer.parseInt(apptData.get(0).get("a_pet_id").toString());


                                java.util.Map<String, String> schedule = validateAppointment(db, sc, petId);

                                String nDate = schedule.get("date");
                                String nTime = schedule.get("time");

                                String sqlUpdate = "UPDATE appointment SET a_date = ?, a_time = ? WHERE a_id = ?";
                                db.updateRecord(sqlUpdate, nDate, nTime, id);

                                System.out.println("Appointment rescheduled successfully!");

                                
                                String sqlUpdates = "UPDATE appointment SET a_date = ?, a_time = ? WHERE a_id = ?";
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
                                    System.out.println("You have no Appointment to cancel.");
                                    break;
                                }

                                System.out.print("Enter ID to Cancel: ");
                                while (true) {
                                    if (!sc.hasNextInt()) {
                                        System.out.print("Invalid input! Please enter a number only: ");
                                        sc.next();
                                        continue;
                                    }

                                    aid = sc.nextInt();

                                    if (validIdds.contains(aid)) {
                                        break;
                                    } else {
                                        System.out.print("Invalid ID! Please enter one of your appointment IDs: ");
                                    }
                                }

                                String sqlCancel = "UPDATE appointment SET a_status = 'Cancelled' WHERE a_id = ?";
                                db.updateRecord(sqlCancel, aid);

                                System.out.println("Appointment cancelled successfully!");
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
