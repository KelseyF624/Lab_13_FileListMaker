import java.util.Scanner;

public class SafeInput {

    public static String getNonZeroLenString (Scanner pipe, String prompt) {
        String retString = "";
        do {
            System.out.print("\n" + prompt + ": ");
            retString = pipe.nextLine();
        }while (retString.equals(""));
        return retString;}

    public static int getInt (Scanner pipe, String prompt) {
        boolean done = false;
        int retInt = 0;
        String trash = "";
        do {
            System.out.print(prompt + ": ");
            if (pipe.hasNextInt()) {
                retInt = pipe.nextInt();
                pipe.nextLine();
                done = true;
            } else {
                trash = pipe.nextLine();
                System.out.println("Invalid entry. Enter an integer, not " + trash);}
        }while (!done);
        return retInt;}

    public static double getDouble(Scanner pipe, String prompt) {
        boolean validDouble = false;
        double retDouble = 0;
        String trash = "";
        do {
            System.out.print(prompt + ": ");
            if (pipe.hasNextDouble()) {
                retDouble = pipe.nextDouble();
                validDouble = true;
            } else {
                trash = pipe.nextLine();
                System.out.print("Invalid entry. Enter a double, not " + trash);
            }
        }while (!validDouble);
        pipe.nextLine();
        return retDouble;}

    public static int getRangedInt (Scanner pipe, String prompt, int lo, int hi) {
        boolean done = false;
        int retInt = 0;
        String trash = "";
        do {
            System.out.print(prompt + "[" + lo + "," + hi + "]: ");
            if (pipe.hasNextInt()) {
                retInt = pipe.nextInt();
                pipe.nextLine();}
            if (retInt >= lo && retInt <= hi) {
                done = true;}
            else {System.out.println("Invalid input. Enter an integer between " + lo + " and " + hi);}
        }while (!done);
        return retInt;}
    public static double getRangedDouble (Scanner pipe, String prompt, double lo, double hi) {
        boolean done = false;
        double retDouble = 0;
        String trash = "";
        do {
            System.out.print(prompt + "[" + lo + "," + hi + "]: ");
            retDouble = pipe.nextDouble();
            if (retDouble >= lo || retDouble <= hi) {
                done = true;}
            else {
                System.out.println("Your input is not a range. Double must be between  " + lo + " " + hi + ".");}
        }while (!done) ;
        return retDouble;}
    public static boolean getYNConfirm (Scanner pipe, String prompt) {
        boolean retValue = false;
        String input = "";
        boolean done = false;
        do {
            System.out.print(prompt);
            input = pipe.nextLine();
            if (input.isEmpty()) {
                System.out.println("You must enter Y or N.");}
            else if (input.equals("Y")) {
                retValue = true;
                done = true;}
            else if (input.equals("N")) {
                retValue = false;
                done = true;}
            else {
                System.out.println("Invalid input. Enter Y or N. Not " + input);}
        } while (!done);
        return retValue;}
    public static String getRegExString (Scanner pipe, String prompt, String regEx) {
        boolean done = false;
        String retValue = "";
        do {
            System.out.print(prompt + regEx + ": ");
            retValue = pipe.nextLine();
            if (retValue.matches(regEx)) {
                done = true;}
            else {System.out.println("Invalid input. value that matches the pattern " + regEx + "not "+ retValue);}
        }while (!done);
        return retValue;
    }
}