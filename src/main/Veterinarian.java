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
        String[] votersHeaders = {"Appointment ID", "Pet ID", "Service", "Date", "Time", "Status"};
        String[] votersColumns = {"a_id", "a_pet_id", "a_service", "a_date", "a_time", "a_status"};
        conf.viewRecords(votersQuery, votersHeaders, votersColumns);
    }

    public static void viewSpecificClientPets() {
        config db = new config();
        Scanner sc = new Scanner(System.in);

        String listClientsQuery = "SELECT a_id AS client_id, a_user AS client_name, a_email AS client_email " +
                                  "FROM account_tbl WHERE a_type = 'Client'";
        java.util.List<java.util.Map<String, Object>> clients = db.fetchRecords(listClientsQuery);
        if (clients.isEmpty()) {
            System.out.println("No clients found.");
            return;
        }

        String[] clientHeaders = {"Client ID", "Name", "Email"};
        String[] clientColumns = {"client_id", "client_name", "client_email"};
        db.viewRecords(listClientsQuery, clientHeaders, clientColumns);

        int clientId;
        java.util.Set<Integer> validClientIds = new java.util.HashSet<>();
        for (java.util.Map<String, Object> c : clients) {
            validClientIds.add(Integer.parseInt(c.get("client_id").toString()));
        }

        while (true) {
            System.out.print("\nEnter Client ID to view their appointments and pets: ");
            if (!sc.hasNextInt()) {
                System.out.println("Invalid input! Enter a number.");
                sc.next();
                continue;
            }
            clientId = sc.nextInt();
            if (validClientIds.contains(clientId)) break;
            System.out.println("Invalid Client ID! Choose from the list above.");
        }

        String appointmentQuery = "SELECT C.a_email, A.a_service, A.a_date, A.a_time, A.a_status " +
                                  "FROM appointment A " +
                                  "INNER JOIN pet P ON A.a_pet_id = P.p_id " +
                                  "INNER JOIN account_tbl C ON P.p_owner_id = C.a_id " +
                                  "WHERE C.a_id = " + clientId;
        String[] headers = {"Email", "Service", "Date", "Time", "Status"};
        String[] columns = {"a_email", "a_service", "a_date", "a_time", "a_status"};
        db.viewRecords(appointmentQuery, headers, columns);

        String petQuery = "SELECT * FROM pet WHERE p_owner_id = " + clientId;
        java.util.List<java.util.Map<String, Object>> pets = db.fetchRecords(petQuery);

        if (pets.isEmpty()) {
            System.out.println("\nThis client has no registered pets.");
        } else {
            System.out.println("\n--- PET INFORMATION ---");
            for (java.util.Map<String, Object> pet : pets) {
                System.out.println("Pet ID: " + pet.get("p_id"));
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
            }
        }
    }

    public static void viewClientDetails() {
        config db = new config();

        String listClientsQuery =
            "SELECT a_id AS client_id, a_user AS client_name, a_email AS client_email " +
            "FROM account_tbl WHERE a_type = 'Client'";
        String[] listHeaders = {"Client ID", "Name", "Email"};
        String[] listColumns = {"client_id", "client_name", "client_email"};
        java.util.List<java.util.Map<String, Object>> clients = db.fetchRecords(listClientsQuery);

        if (clients.isEmpty()) {
            System.out.println("No clients found.");
            return;
        }

        db.viewRecords(listClientsQuery, listHeaders, listColumns);

        Scanner sc = new Scanner(System.in);
        int clientId;
        java.util.List<Integer> validClientIds = new java.util.ArrayList<>();
        for (java.util.Map<String, Object> c : clients) {
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

        String detailsQuery =
            "SELECT C.a_id AS client_id, C.a_user AS client_name, C.a_email AS client_email, " +
            "P.p_id AS pet_id, P.p_name AS pet_name, P.p_species AS pet_species, " +
            "A.a_id AS appointment_id, A.a_service, A.a_date, A.a_time, A.a_status " +
            "FROM account_tbl C " +
            "LEFT JOIN pet P ON P.p_owner_id = C.a_id " +
            "LEFT JOIN appointment A ON A.a_pet_id = P.p_id " +
            "WHERE C.a_type = 'Client' AND C.a_id = " + clientId + " " +
            "ORDER BY P.p_id, A.a_id";

        String[] headers = {"Client ID", "Name", "Email", "Pet ID", "Pet Name", "Species", "Appointment ID", "Service", "Date", "Time", "Status"};
        String[] columns = {"client_id", "client_name", "client_email", "pet_id", "pet_name", "pet_species", "appointment_id", "a_service", "a_date", "a_time", "a_status"};
        db.viewRecords(detailsQuery, headers, columns);
    }

    public void veterinarian() {

        do {
            System.out.println("|=======================================|");
            System.out.println("|       🐾 VETERINARIAN DASHBOARD 🐾      |");
            System.out.println("|=======================================|");
            System.out.println("|  1. View Appointments                 |");
            System.out.println("|  2. Approve Appointment               |");
            System.out.println("|  3. View Specific Client Pet Info     |");
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
                if (choice >= 1 && choice <= 4) break;
                System.out.println("Invalid input. Choose between 1-4.");
            }

            switch (choice) {

                case 1:
                    viewAppointments();
                    break;

                case 2:
                    int appId;
                    viewAppointments();

                    java.util.List<java.util.Map<String, Object>> appointments = db.fetchRecords(
                        "SELECT a_id, a_status FROM appointment"
                    );

                    java.util.Set<Integer> validIds = new java.util.HashSet<>();
                    java.util.Map<Integer, String> statusMap = new java.util.HashMap<>();

                    for (java.util.Map<String, Object> appt : appointments) {
                        int id = Integer.parseInt(appt.get("a_id").toString());
                        String status = appt.get("a_status").toString();
                        validIds.add(id);
                        statusMap.put(id, status);
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
                        if (!validIds.contains(appId)) {
                            System.out.print("Invalid ID! Please enter an existing appointment ID: ");
                            continue;
                        }
                        if (statusMap.get(appId).equalsIgnoreCase("Cancelled")) {
                            System.out.println("This appointment is CANCELLED and cannot be approved.");
                            break;
                        }
                        break;
                    }

                    if (statusMap.get(appId).equalsIgnoreCase("Cancelled")) {
                        break;
                    }

                    String sqlApprove = "UPDATE appointment SET a_status = ? WHERE a_id = ?";
                    db.updateRecord(sqlApprove, "Approved", appId);

                    System.out.println("Appointment approved successfully!");
                    break;

                case 3:
                    viewSpecificClientPets();
                    break;

            }

        } while (choice != 4);
    }
}
