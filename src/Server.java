import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

class Server {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static final String eor = "[EOR]"; 

    private static final String USERNAME = "Peppa";
    private static final String PASSWORD = "OINK";

    private static int balance = 0; // in cents
    private static ArrayList<Integer> transactions = new ArrayList<>();
    private static DecimalFormat df = new DecimalFormat("#0.00");
   

    private static void setup() throws IOException {
        serverSocket = new ServerSocket(0);
        toConsole("Server is running on port: " + serverSocket.getLocalPort());
        toConsole("Waiting for client connection...");

        clientSocket = serverSocket.accept();
        // get the input stream and attach to a buffered reader
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        
        // get the output stream and attach to a printwriter
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        toConsole("Accepted connection from "
                 + clientSocket.getInetAddress() + " at port "
                 + clientSocket.getPort());

        sendGreeting(); // #1) Sends the required greeting: "Welcome to PigNet!"
    }

    private static void sendGreeting()
    {
        sendOutput("Welcome to PigNet!");
    }

    // Authenticate user credentials
    private static void authenticateUser() throws IOException {
        int attempts = 0;

        // #6) Allow a maximum of 5 attempts in total to enter the correct credentials
        while (attempts < 5) {
            // #2) Ask for username 
            sendOutput("Enter username:");
            String usernameInput = in.readLine();
            toConsole("DEBUG: Received username: " + usernameInput);

            if (USERNAME.equals(usernameInput)) {
                // #3) Ask for password 
            sendOutput("Enter password:"); 
            String passwordInput = in.readLine();
            toConsole("DEBUG: Received password: " + passwordInput);

            if (PASSWORD.equals(passwordInput)) {
                // #8) Sends the welcome message and balance if the user enters the correct username and password
                sendOutput("Welcome, Peppa!\nYour current balance is: $" + inDollars(balance));
                return;

            } else {
                sendOutput("Invalid Password. Try again."); // #5) Tells the user if the password entered is incorrect
                attempts++;
                // Enter here so user does not have to click enter
            }
                return;
            } else {
                sendOutput("Invalid Username. Try again."); // #4) Tells the user if the username entered is incorrect
                attempts++;
                // Enter here so user does not have to click enter
            }

            
        }

        // #7) Server disconnects if the user fails after 5 attempts
        sendOutput("Too many failed attempts. Disconnecting...");
        disconnect();
    }

    private static void bankingMenu() throws IOException {
        while (true) {
            // #9) Diplay the options menu continuiously until case 4 is met ("EXIT" is input)
            sendOutput("""
                Please select an option:
                1. Make a deposit
                2. Make a withdrawal
                3. View a list of all transactions
                4. Exit
                """);

            try {
                String selection = in.readLine(); // Read input from client
                toConsole("DEBUG: Client selected option: " + selection);

                switch (Integer.parseInt(selection)) {
                    case 1 -> deposit();
                    case 2 -> withdraw();
                    case 3 -> viewTransactions();
                    case 4 -> {
                        exit();
                        return;
                    }
                    // #13) Menu invalid input error handling
                    default -> sendOutput("Invalid selection. Please try again.");
                }
            } catch (NumberFormatException | IOException e) {
                sendOutput("Invalid input. Please try again.");
            }
        }
    }

    // #10) Deposit into bank
    private static void deposit() throws IOException {
        sendOutput("Enter an amount to deposit in $$.cc:");

        try {
            String input = in.readLine();
            double amount = Double.parseDouble(input);
            int inCents = Math.abs((int) (amount * 100));
            balance += inCents;
            transactions.add(inCents);

            sendOutput("Deposit successful! Your new balance is: $" + inDollars(balance));
        }
        // #13) Deposit amount invalid input error handling 
        catch (NumberFormatException e) {
            sendOutput("Invalid amount format. Please enter a valid number.");
        }
    }

    // #11) Withdrawl from bank 
    private static void withdraw() throws IOException {
        sendOutput("Enter an amount to withdraw in $$.cc:");

        try {
            String input = in.readLine();
            double amount = Double.parseDouble(input);
            int inCents = Math.abs((int) (amount * 100));

            // #13) Withdrawl amount > balance error handling
            if (inCents > balance) {
                sendOutput("Insufficient funds. Your current balance is: $" + inDollars(balance));
            } else {
                balance -= inCents;
                transactions.add(-inCents);
                sendOutput("Withdrawal successful! Your new balance is: $" + inDollars(balance));
            }
            // #13) Withdrawl amount invalid input error handling 
        } catch (NumberFormatException e) {
            sendOutput("Invalid amount format. Please enter a valid number.");
        }
    }

    // #12) View bank transactions
    private static void viewTransactions() {
        StringBuilder response = new StringBuilder("Transactions Performed:\n");

        for (Integer item : transactions) {
            if (item > 0) {
                response.append("Deposit:\t$").append(inDollars(item)).append("\n");
            } else {
                response.append("Withdrawal:\t$").append(inDollars(-item)).append("\n");
            }
        }

        // #13) Deposit amount invalid input error handling 
        if (transactions.isEmpty()) {
            response.append("No transactions found.");
        }

        sendOutput(response.toString());
    }

    // Exit from program with exit message
    private static void exit() throws IOException {
        sendOutput("Thank you for using PigNet. Have a great day!");
        disconnect();
    }

    private static void disconnect() throws IOException {
        out.close();
        toConsole("Disconnected.");
        System.exit(0);
    }

    private static String inDollars(int inCents) {
        return df.format(inCents / 100.0);
    }

    private static void sendOutput(String output) {
        out.println(output);
        out.println(eor); 
        out.flush(); 
    }

    private static void toConsole(String message) {
        System.out.println(message);
    }

    public static void main(String[] args) {
        try {
            setup();
            authenticateUser();
            bankingMenu();
        } catch (IOException ioex) {
            toConsole("Error: " + ioex.getMessage());
        }
    }
}
