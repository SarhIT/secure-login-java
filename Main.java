package securelogin;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();

        while (true) {
            System.out.println("\n1) Register  2) Login  3) Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine();

            if (choice.equals("3")) break;
            if (!choice.equals("1") && !choice.equals("2")) {
                System.out.println("Invalid choice.");
                continue;
            }
            System.out.print("Username: ");
            String user = sc.nextLine();
            System.out.print("Password: ");
            String pass = sc.nextLine();

            if (choice.equals("1")) System.out.println(auth.register(user, pass));
            else System.out.println(auth.login(user, pass));
        }
        sc.close();
    }
}