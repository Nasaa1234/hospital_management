package services;

import models.*;
import java.io.*;
import java.util.*;

public class UserLoader {
    private static List<User> users = new ArrayList<>();

    public static List<User> loadUsers(String filename) {
        users.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 4)
                    continue;

                String name = data[0];
                String email = data[1];
                String role = data[2];
                String password = data[3];

                switch (role) {
                    case "admin":
                        users.add(new Admin(name, email, password));
                        break;
                    case "doctor":
                        users.add(new Doctor(name, email, password));
                        break;
                    case "patient":
                        if (data.length >= 5) {
                            String image = data[4];
                            users.add(new Patient(name, email, password, image));
                        }
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }

        return users;
    }

    public static User getUserByName(String filename, String targetName) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 4)
                    continue;

                String name = data[0];
                String email = data[1];
                String role = data[2];
                String password = data[3];

                if (name.equalsIgnoreCase(targetName)) {
                    switch (role) {
                        case "admin":
                            return new Admin(name, email, password);
                        case "doctor":
                            return new Doctor(name, email, password);
                        case "patient":
                            if (data.length >= 5) {
                                String image = data[4];
                                return new Patient(name, email, password, image);
                            }
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users: " + e.getMessage());
        }

        return null;
    }

    public static void addUser(String name, String password, String role, String mail, String image) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("database/" + role + "s.csv", true))) {

            String line = String.join(",", name, password, role, mail, image);
            bw.newLine();
            bw.write(line);
            Patient user = new Patient(name, mail, password, image);
            users.add(user);
            user.accessPanel();
        } catch (IOException e) {
            System.out.println("Error signing up patient: " + e.getMessage());
        }
    }
}
