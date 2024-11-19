//import java.util.Scanner;
import java.util.*;
import java.text.DecimalFormat;

// banking made too simple! 
// Mark H October 2024
class PiggyBank {
    
    private static int balance = 0;	// in cents
    private static ArrayList<Integer> transactions = new ArrayList<Integer>();
    private static DecimalFormat df = new DecimalFormat("#0.00"); 
    
    
    public static void main( String args[])
    {	
    	System.out.println("Welcome to PigNet!");
    	
    	int selection = 0;
    	while (selection != 4)
    	{
    		System.out.println("Your current balance is:\t" + inDollars(balance));
    		selection = menu();
    		switch ( selection )
    		{
    			case 1: deposit(); break;
    			case 2: withdraw(); break;
    			case 3: viewTransactions(); break;
    			case 4: exit(); break;
    			default: tryAgain();
    		}
    	}
    }
    
    private static int menu()
    {
        System.out.println("\nPlease select an option:");
        System.out.println("1. Make a deposit");
        System.out.println("2. Make a withdrawal");
        System.out.println("3. View a list of all transactions");
        System.out.println("4. Exit");
    	System.out.print("\nYour selection:\t");
        
        Scanner input = new Scanner(System.in);
        int selection = 0;
        try
        {
        	selection = input.nextInt(); 
        }
        catch ( Exception ex )
        {
        	// can ignore; main() will handle since selection = 0;
        }
        return selection;
    }
    
    private static void deposit()
    {
    	System.out.print("\nEnter an amount to deposit in $$.cc: ");
    	
    	try
    	{
    		Scanner input = new Scanner(System.in);
    		double amount = input.nextDouble();
    		int inCents = Math.abs((int) (amount * 100));
    		balance += inCents;
    		transactions.add(inCents);
    	}
    	catch ( Exception ex )
        {
        	wrongFormat();
        }
    }
    
    private static void withdraw()
    {
    	System.out.print("\nEnter an amount to withdraw in $$.cc: ");
    	
    	try
    	{
    		Scanner input = new Scanner(System.in);
    		double amount = input.nextDouble();
    		int inCents = Math.abs((int) (amount * 100));
    		balance -= inCents;
    		transactions.add(0 - inCents);
    	}
    	catch ( Exception ex )
        {
        	wrongFormat();
        }
    }
    
    private static void viewTransactions()
    {
    	System.out.println("\nTransactions Performed:");
    	for (Integer item : transactions)
    	{
    		if (item > 0)
    		{
    			System.out.println("deposit:\t" + inDollars(item));
    		}
        	else
        	{
        		item = 0 - item;
        		System.out.println("withdrawal:\t" + inDollars(item)); }
    	}
    }
    
    private static void exit()
    {
    	System.out.println("Thank you for using PigNet.  Have a nice day!\n");
    	System.exit(0);
    }
    
    private static void tryAgain()
    {
    	System.out.println("\nPlease make a valid selection!\n");
    }
    
    private static void wrongFormat()
    {
    	System.out.println("\nNot a valid amount in $$.cc\n");
    }
    
    private static String inDollars(int inCents)
    {
        return "$" + df.format(inCents/100.00);
    }
}
