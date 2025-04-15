import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ArrayList<String> optionList = new ArrayList<>();
            Scanner in = new Scanner(System.in);
            boolean done = false;
            String items = "";

            do {

                items = displayMenu (in, optionList);

                switch (items) {
                    case "A", "I" :
                        addItem (in, optionList);
                        break;

                    case "D":
                        removeItem (in, optionList);
                        break;

                    case "P":
                        showItems (optionList);
                        break;

                    case "Q":
                        if (SafeInput.getYNConfirm(in, "Are you sure you want to quit? [Y/N] ")) {
                            done = true;}
                        break;
                    case "M":
                        moveItem();
                        break;
                    case "O":
                        openList();
                        break;
                    case "S":
                        saveList();
                        break;
                    case "C":
                        clearList();
                        break;
                    case "V":
                        viewList();
                        break;

                }

            }while (!done);
    }
    private static String displayMenu(Scanner in, ArrayList optionList) {

        if (optionList.isEmpty()) {
            System.out.println("The list is empty."); }

        else {
            System.out.println("The current list is: ");
            for (int i = 0; i < optionList.size(); i++) {
                System.out.printf("     %d. %s\n", i + 1,optionList.get(i));}
        }

        return SafeInput.getRegExString(in, "Type the letter you wish to select and then press enter.\n" +
                "A or I: add an item. \n D: Delete an item. \n P: Print the list. \n Q: Quit.", "[AaIiDdPpQq]"); }

    private static void removeItem(Scanner in, ArrayList optionList) {

        int removeItem = SafeInput.getRangedInt(in, "Please type the list number of the item you wish to remove.", 1, optionList.size());
        optionList.remove(removeItem - 1); }

    private static void addItem(Scanner in, ArrayList optionList){

        String addItem = SafeInput.getNonZeroLenString(in, "Enter the item you want to add: ");
        optionList.add(addItem); }

    private static void showItems(ArrayList optionList){

        for (int i = 0; i < optionList.size(); i++) {
            System.out.println(optionList.get(i));}

    private static void moveItem(Scanner in, ArrayList optionList){
            int fromIndex = SafeInput.getRangedInt(in, "Please enter the line number you want to move.", 1, list.size())-1;
        }
    }
}

