package main;

import config.config;
import java.util.Scanner;

public class Veterinarian {
    Scanner sc = new Scanner(System.in);
    config db = new config();
    int choice = 0;

    // ==================== VIEW ALL APPOINTMENTS ====================
    public static void viewAppointments() {
        config conf = new config();
        String votersQuery = "SELECT * FROM appointment";
        String[] votersHeaders = {"Appointment ID", "Pet ID", "Service", "Date", "Time", "Status"};
        String[] votersColumns = {"a_id", "a_pet_id", "a_service", "a_date", "a_time", "a_status"};
        conf.viewRecords(votersQuery, votersHeaders, votersColumns);
    }

    // ==================== VIEW ALL PET INFORMATION ====================
    public static void viewPetInformation() {
        config db = new config();

        String query = "SELECT " +
                       "P.p_id AS pet_id, " +
                       "P.p_name AS pet_name, " +
                       "P.p_age AS pet_age, " +
                       "P.p_species AS pet_species, " +
                       "P.p_breed AS pet_breed, " +
                       "P.p_dateBirth AS pet_birth, " +
                       "P.p_gender AS pet_gender, " +
                       "P.p_weight AS pet_weight, " +
                       "P.p_notes AS pet_notes, " +
                       "C.a_user AS owner_name, " +
                       "C.a_email AS owner_email " +
                       "FROM pet P " +
                       "LEFT JOIN account_tbl C ON P.p_owner_id = C.a_id " +
                       "ORDER BY P.p_id";

        String[] headers = {"Pet ID", "Name", "Age", "Species", "Breed", "Birth Date", "Gender", "Weight", "Notes", "Owner Name", "Owner Email"};
        String[] columns = {"pet_id", "pet_name", "pet_age", "pet_species", "pet_breed", "pet_birth", "pet_gender", "pet_weight", "pet_notes", "owner_name", "owner_email"};

        db.viewRecords(query, headers, columns);
    }

    // ==================== VIEW SPECIFIC CLIENT DETAILS ====================
    public static void viewClientDetails() {
        config db = new config();

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

    // ==================== VIEW PET BY ID (UPDATED LIKE CLIENT VIEW) ====================
    public static void viewPetById() {
        config db = new config();
        Scanner sc = new Scanner(System.in);

        // Show Pet IDs and Owners first
        String listQuery = "SELECT P.p_id AS pet_id, C.a_user AS owner_name " +
                           "FROM pet P " +
                           "LEFT JOIN account_tbl C ON P.p_owner_id = C.a_id " +
                           "ORDER BY P.p_id";

        java.util.List<java.util.Map<String, Object>> pets = db.fetchRecords(listQuery);

        if (pets.isEmpty()) {
            System.out.println("No pets found in the database.");
            return;
        }

        System.out.println("\n=== Available Pets ===");
        System.out.println("--------------------------------");
        System.out.printf("%-10s %-20s%n", "Pet ID", "Owner Name");
        System.out.println("--------------------------------");

        for (java.util.Map<String, Object> pet : pets) {
            System.out.printf("%-10s %-20s%n", pet.get("pet_id"), pet.get("owner_name"));
        }
        System.out.println("--------------------------------");

        java.util.List<Integer> validPetIds = new java.util.ArrayList<>();
        for (java.util.Map<String, Object> pet : pets) {
            validPetIds.add(Integer.parseInt(pet.get("pet_id").toString()));
        }

        int petId;
        while (true) {
            System.out.print("\nEnter Pet ID to view details: ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input! Please enter a number.");
                sc.next();
                continue;
            }
            petId = sc.nextInt();

            if (validPetIds.contains(petId)) {
                break;
            } else {
                System.out.println("Pet ID not found. Please choose one from the list above.");
            }
        }
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
                       "WHERE P.p_id = " + petId + " " +
                       "ORDER BY A.a_id";

        String[] headers = {"Client ID", "Name", "Email", "Pet ID", "Pet Name", "Species", "Appointment ID", "Service", "Date", "Time", "Status"};
        String[] columns = {"client_id", "client_name", "client_email", "pet_id", "pet_name", "pet_species", "appointment_id", "a_service", "a_date", "a_time", "a_status"};

        db.viewRecords(query, headers, columns);
    }

  
    public void veterinarian() {
        do {
            System.out.println("\nWELCOME TO VETERINARIAN DASHBOARD");
            System.out.println("1. View Appointments");
            System.out.println("2. Approve Appointment");
            System.out.println("3. View All Pet Information");
            System.out.println("4. View Specific Client Pet Information");
            System.out.println("5. View Pet by ID");
            System.out.println("6. Exit");

            while (true) {
                System.out.print("Enter Choice (1-6): ");
                if (!sc.hasNextInt()) {
                    System.out.println("Invalid input! Please enter a number only.");
                    sc.next();
                    continue;
                }
                choice = sc.nextInt();
                if (choice >= 1 && choice <= 6) break;
                System.out.println("Invalid input. Choose between 1-6.");
            }

            switch (choice) {
                case 1: 
                    viewAppointments();
                break;
                case 2: 
                    int appId;
                        viewAppointments();

                        java.util.List<java.util.Map<String, Object>> appointments = db.fetchRecords(
                            "SELECT a_id FROM appointment"
                        );

                        java.util.Set<Integer> validIds = new java.util.HashSet<>();
                        for (java.util.Map<String, Object> appt : appointments) {
                            validIds.add(Integer.parseInt(appt.get("a_id").toString()));
                        }

                        if (validIds.isEmpty()) {
                            System.out.println("There are no appointments to approve.");
                            break;
                        }
                        System.out.print("Enter Appointment ID to approve: ");
                        while (true) {
                            if (!sc.hasNextInt()) {
                                System.out.print("Invalid input! Please enter a number only: ");
                                sc.next(); 
                                continue;
                            }
                            appId = sc.nextInt();

                            if (validIds.contains(appId)) {
                                break;
                            } else {
                                System.out.print("Invalid ID! Please enter an existing appointment ID: ");
                            }
                        }
                        String sqlApprove = "UPDATE appointment SET a_status = ? WHERE a_id = ?";
                        db.updateRecord(sqlApprove, "Approved", appId);
                        System.out.println("Appointment approved successfully!");
                break;
                case 3:
                    viewPetInformation();
                break;
                case 4:
                    viewClientDetails();
                break;
                case 5:
                    viewPetById();
                break;
            }

        } while (choice != 6);
    }
}
