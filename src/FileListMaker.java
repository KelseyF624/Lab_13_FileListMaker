import java.io.*;
import javax.swing.JFileChooser;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class FileListMaker {

    static boolean needsToBeSaved = false;
    static boolean running = true;
    static boolean hasAFile = false;
    static boolean newUnsaved = false;
    static boolean fileSaving = false;
    static File currentFile = null;
    static ArrayList<String> optionList = new ArrayList<>();
    static Scanner console = new Scanner(System.in);
    static Scanner inFile = new Scanner(System.in);
    static String currentFileName;
    static PrintWriter outFile;

    public static void main(String[] args) throws IOException {

        Scanner in = new Scanner(System.in);
        boolean done = false;
        boolean saveConfirm = true;
        String fileName = "";
        String items = "";

        do {

            items = displayMenu(in, optionList);
            items = items.toUpperCase();

            switch (items) {
                case "A", "I":
                    addItem(in, optionList);
                    needsToBeSaved = true;
                    break;

                case "D":
                    removeItem(in, optionList);
                    needsToBeSaved = true;
                    break;

                case "Q":
                    if (SafeInput.getYNConfirm(in, "Are you sure you want to quit? [Y/N] ")) {
                        done = true;
                    }
                    needsToBeSaved = true;
                    break;

                case "M":
                    moveItem(optionList, needsToBeSaved);
                    needsToBeSaved = true;
                    break;

                case "O":
                    openList(in, optionList, needsToBeSaved);
                    break;

                case "S":
                    saveList();
                    needsToBeSaved = true;
                    break;

                case "C":
                    clearList(in, optionList);
                    needsToBeSaved = true;
                    break;

                case "V":
                    showItems(optionList);
                    break;
            }

        } while (!done);
    }

    private static String displayMenu(Scanner in, ArrayList optionList) {

        if (optionList.isEmpty()) {
            System.out.println("The list is empty.");
        } else {
            System.out.println("The current list is: ");
            for (int i = 0; i < optionList.size(); i++) {
                System.out.printf("     %d. %s\n", i + 1, optionList.get(i));
            }
        }

        return SafeInput.getRegExString(in, "Type the letter you wish to select and then press enter.\n" +
                "A or I: add an item. \n D: Delete an item. \n P or V: Print the list. \n Q: Quit. \n M: Move an item. \n O: Open a list. \n S: Save list. \n C: Clear list. \n", "[AaIiDdPpVvQqMmOoSsCc]");
    }

    private static void removeItem(Scanner in, ArrayList optionList) {

        int removeItem = SafeInput.getRangedInt(in, "Please type the list number of the item you wish to remove.", 1, optionList.size());
        optionList.remove(removeItem - 1);
    }

    private static void addItem(Scanner in, ArrayList optionList) {

        String addItem = SafeInput.getNonZeroLenString(in, "Enter the item you want to add: ");
        optionList.add(addItem);
    }

    private static void showItems(ArrayList optionList) {

        for (int i = 0; i < optionList.size(); i++) {
            System.out.println(optionList.get(i));
        }
    }

    private static void moveItem(ArrayList optionList, boolean needsToBeSaved) {

        Scanner in = new Scanner(System.in);
        if (optionList.isEmpty()) {
            System.out.println("The list is empty. Nothing to move.");
            return;}
        showItems(optionList);
        int fromList = SafeInput.getRangedInt(in, "Enter the item you want to move: ", 1, optionList.size()) - 1;
        int toList = SafeInput.getRangedInt(in, "Enter the location on the list where you want to move the item.: ", 1, optionList.size()) - 1;
        Object item = optionList.remove(fromList);
        optionList.add(toList, item);
        needsToBeSaved = true;
        System.out.println("Moved item to position " + (toList + 1));}

    private static void saveList() throws FileNotFoundException, IOException {

        PrintWriter outFile;
        Path target = new File(System.getProperty("user.dir")).toPath();
        currentFileName = SafeInput.getNonZeroLenString(console, "Enter the name of the file: ");

        if (currentFileName.equals("")) {
            target = target.resolve("src");}
        else {
            target = target.resolve(currentFileName);}
        try {
            outFile = new PrintWriter(currentFileName);
            for (int i = 0; i < optionList.size(); i++) {
                outFile.println(optionList.get(i));}
            outFile.close();
            System.out.printf("File \"%s\" saved!\n", target.getFileName());}
        catch (IOException e) {
            System.out.println("IOException Error");}
    }

    private static void openList(Scanner in, ArrayList optionList, boolean needsToBeSaved) throws FileNotFoundException, IOException {

        if (needsToBeSaved) {
            boolean saveFirst = SafeInput.getYNConfirm(in, "You have unsaved changes. Save before opening a new list?[Y/N");
            if (saveFirst) {
                saveList();}
        }

        JFileChooser chooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            inFile = new Scanner(chooser.getSelectedFile());
            optionList.clear();
            while (inFile.hasNextLine()) {
                optionList.add(inFile.nextLine());}
            inFile.close();
            hasAFile = true;
            newUnsaved = false;
            fileSaving = false;
            System.out.println("Opened file \"" + currentFileName + "\".");}
        else {
            System.out.println("You didn't choose a file.");}
    }
    private static void clearList(Scanner in, ArrayList optionList) throws IOException {

            boolean saveFirst = SafeInput.getYNConfirm(in, "You have unsaved changes. Save before opening a new list?[Y/N]");
            if (saveFirst) {
                saveList();}
            optionList.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.");}
    }






