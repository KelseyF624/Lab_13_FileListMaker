import javax.swing.*;
import java.io.File;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class FileListMaker {
    public static void main(String[] args) {

        ArrayList<String> optionList = new ArrayList<>();
            Scanner in = new Scanner(System.in);
            boolean done = false;
            boolean needsToBeSaved = false;
            String fileName = "";
            String items = "";

            do {

                items = displayMenu (in, optionList);

                switch (items) {
                    case "A", "I" :
                        addItem (in, optionList);
                        needsToBeSaved = true;
                        break;

                    case "D":
                        removeItem (in, optionList);
                        needsToBeSaved = true;
                        break;

                    case "Q":
                        if (SafeInput.getYNConfirm(in, "Are you sure you want to quit? [Y/N] ")) {
                            done = true;
                        }
                        needsToBeSaved = true;
                        break;
                    case "M":
                        moveItem(optionList);
                        needsToBeSaved = true;
                        break;
                    case "O":
                        openList(in, optionList, needsToBeSaved);
                        needsToBeSaved = true;
                        break;
                    case "S":
                        saveList(optionList, fileName);
                        needsToBeSaved = true;
                        break;
                    case "C":
                        clearList(optionList);
                        needsToBeSaved = true;
                        break;
                    case "V":
                        showItems (optionList);
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
    }

    private static void clearList(ArrayList optionList) {

        optionList.clear();}

    private static void moveItem(ArrayList optionList, boolean needsToBeSaved) {

        Scanner in = new Scanner(System.in);
        if (optionList.isEmpty()) {
            System.out.println("The list is empty.");
            return;}
        showItems(optionList);
        int selectItem = SafeInput.getRangedInt(in, "Enter the item you want to move: ", 1, optionList.size());
        int newLocation = SafeInput.getRangedInt(in, "Enter the location on the list where you want to move the item.: ", 1, optionList.size())
        String moved = optionList.remove(selectItem);
        optionList.add(newLocation, moved);
        needsToBeSaved = true;
        System.out.println("Moved " + moved + " to " + newLocation);
    }

    private static void saveList(ArrayList optionList, String fileName) {

        PrintWriter outFile;
        Path target = new File(System.getProperty("user.dir")).toPath();

        if (fileName.equals("")) {
            target = target.resolve("src\\list.txt");}
        else {
            target = target.resolve(fileName);}
        try {
            outFile = new PrintWriter(target.toString());
            for (int i = 0; i < optionList.size(); i ++){
            outFile.println(optionList.get(i));}
            outFile.close();
            System.out.println("List written to " + target);
        }catch (IOException e){
            System.out.println("Error writing to " + target);}
    }

    private static void openList(Scanner in, ArrayList optionList, boolean needsToBeSaved) {

        boolean deleteList = false;

        if (needsToBeSaved) {
            String delete = ("List not saved. If you open a new list now your current list will be lost.");
            deleteList = SafeInput.getYNConfirm(in, delete);
        }
        if (!deleteList) {
            return;
        }

        clearList(optionList);
        Scanner inFile;
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File", "txt");
        fileChooser.setFileFilter(filter);
        String line;
        Path target = new File(System.getProperty("user.dir")).toPath();
        target = target.resolve("src");
        fileChooser.setCurrentDirectory(target.toFile());
        try {
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                target = fileChooser.getSelectedFile().toPath();
                inFile = new Scanner(target);
                System.out.println("Opening list: " + target);
                while (inFile.hasNextLine()) {
                    line = inFile.nextLine();
                    optionList.add(line);}
                inFile.close();}
            else {
                System.out.println("Please select a list. Returning to menu.");}
        }
        catch (IOException e) {
            System.out.println("Error writing to " + target);}
    }
}

