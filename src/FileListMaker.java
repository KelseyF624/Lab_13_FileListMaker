import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import java.io.File;
import java.nio.file.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

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
                    case "a", "i":
                    addItem(in, optionList);
                    needsToBeSaved = true;
                    break;

                case "D":
                    case "d":
                    removeItem(in, optionList);
                    needsToBeSaved = true;
                    break;

                case "Q":
                    case "q":
                    if (SafeInput.getYNConfirm(in, "Are you sure you want to quit? [Y/N] ")) {
                        done = true;
                    }
                    needsToBeSaved = true;
                    break;

                case "M":
                    case "m":
                    moveItem(optionList, needsToBeSaved);
                    needsToBeSaved = true;
                    break;

                case "O":
                    case "o":
                    if(!needsToBeSaved) {
                        openList(in, optionList, needsToBeSaved);
                    }
                    else {
                        saveConfirm = SafeInput.getYNConfirm(in, "Would you like to save?");
                    if (saveConfirm) {
                        System.out.println("Save the file from the menu.");}
                    else {
                        openList(in, optionList, needsToBeSaved);}
                    }
                    break;

                case "S":
                    case "s":
                    saveList(optionList, fileName);
                    needsToBeSaved = true;
                    break;

                case "C":
                    case "c":
                    clearList(optionList);
                    needsToBeSaved = true;
                    break;

                case "V":
                    case "v":
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

    private static void clearList(ArrayList optionList) {

        optionList.clear();
    }

    private static void moveItem(ArrayList optionList, boolean needsToBeSaved) {

        Object userMove;
        Object replaceLocation;
        int fromList;
        int toList;
        Scanner in = new Scanner(System.in);
        showItems(optionList);

        fromList = SafeInput.getRangedInt(in, "Enter the item you want to move: ", 1, optionList.size());
        userMove = optionList.get(fromList - 1);
        toList = SafeInput.getRangedInt(in, "Enter the location on the list where you want to move the item.: ", 1, optionList.size());
        replaceLocation = optionList.getLast();

        for (int x = optionList.size() - 1; x >= toList -1; x--) {
            optionList.set(x, optionList.get(x));}
        optionList.set(toList -1, userMove);
        optionList.add(replaceLocation);
        optionList.remove(fromList);
        System.out.println("Data moved.");}

    private static void saveList(ArrayList optionList, String fileName) throws FileNotFoundException,IOException {

        if (!hasAFile) {
            currentFileName = SafeInput.getNonZeroLenString(console, "Enter the name of the file: ");
        }
        outFile = new PrintWriter(currentFileName);
        for (Object ln:optionList){
            outFile.println(ln);
            outFile.close();
            System.out.println("Saved file: " + currentFileName);}
    }

    private static void openList(Scanner in, ArrayList optionList, boolean needsToBeSaved) throws FileNotFoundException,IOException {

        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("src"));
        Path target = new File(System.getProperty("user.dir") + "\\src\\main\\java\\FileInspector.java").toPath();
        target = target.resolve("src");
        File workingDirectory = new File(System.getProperty("user.dir"));

        try {

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                target = chooser.getSelectedFile().toPath();
                inFile = new Scanner(target);
                optionList.clear();
                while (inFile.hasNextLine()) {
                    optionList.add(inFile.nextLine());}
                inFile.close();
                currentFileName = chooser.getSelectedFile().getName();
                hasAFile = true;
                newUnsaved = false;
                fileSaving = false;
                System.out.println("Opened " + currentFileName);} else {
                System.out.println("No such file or directory. Terminating.");
                System.exit(0);}
        } catch (Exception e) {
            System.out.println("Error.");
            e.printStackTrace();
            System.exit(0);}
    }
}

