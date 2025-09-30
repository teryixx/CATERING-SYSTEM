package cateringsystem;

import config.config;
import java.util.Scanner;

public class Cateringsystem {

    public static void main(String[] args) {   
        Scanner sc = new Scanner(System.in);
        String resp;
        do{

            System.out.println("1. ADD USER");
            System.out.println("2. VIEW USER");
            System.out.println("3. UPDATE USER");
            System.out.println("4. DELETE USER");
            System.out.println("5. EXIT");

            System.out.print("Enter Action: ");
            int action = sc.nextInt();
            Cateringsystem catering = new Cateringsystem();
            switch(action){
                case 1:                 
                     catering.addUser();
                break;
                case 2:
                     catering.viewUser();
                break;
                case 3:
                     catering.viewUser();
                     catering.updateUser();
                break;
                case 4:
                     catering.viewUser();
                     catering.deleteUser();
                break;
            }
            
            System.out.print("Continue? ");
            resp = sc.next();

        }while(resp.equalsIgnoreCase("yes"));
            System.out.println("Thank You!");

    }
 private void addUser(){
        Scanner sc = new Scanner(System.in);
        config conf = new config();
        System.out.print("Name: ");
        String name = sc.nextLine();
        System.out.print("Contact: ");
        String contact = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Role(Admin//Customer): ");
        String role = sc.nextLine();

        String sql = "INSERT INTO tbl_user (u_name, u_contact, u_email, u_role) VALUES (?, ?, ?, ?)";
        conf.addRecord(sql, name, contact, email, role);
    } 

private void viewUser() {
        
        String qry = "SELECT * FROM tbl_user";
        String[] hdrs = {"ID", "Name", "Contact", "Email", "Role"};
        String[] clms = {"u_id", "u_name", "u_contact", "u_email", "u_role"};

        config conf = new config();
        conf.viewRecords(qry, hdrs, clms);
        
    }
 private void updateUser(){ 
    
        Scanner sc= new Scanner(System.in);
        System.out.print("Enter the ID to Update: ");
        int id = sc.nextInt();
        
        System.out.print("Enter new Name: ");
        String nname = sc.next();
        System.out.print("Enter new Contact: ");
        String ncontact = sc.next();
        System.out.print("Enter new Email: ");
        String nemail = sc.next();
        System.out.print("Enter new Role(Admin//Customer): ");
        String nrole = sc.next();
        String qry = "UPDATE tbl_user SET u_name = ?, u_contact = ?, u_email = ?, u_role = ? WHERE u_id = ?";
        
        config conf = new config();
        conf.updateRecord(qry, nname, ncontact, nemail, nrole, id);
    }
 private void deleteUser(){
        
        Scanner sc= new Scanner(System.in);
        System.out.print("Enter the ID to Delete: ");
        int id = sc.nextInt();
        
        String qry = "DELETE FROM tbl_user WHERE u_id = ?";
        
        config conf = new config();
        conf.deleteRecord(qry, id);
    
    }
}