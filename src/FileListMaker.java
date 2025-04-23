import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import java.nio.file.Path;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
                        fileName = openList(in, optionList, needsToBeSaved);
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

        PrintWriter outFile;
        Path target = new File(System.getProperty("user.dir")).toPath();
        currentFileName = SafeInput.getNonZeroLenString(console, "Enter the name of the file: ");

        if (fileName.equals("")) {
            target = target.resolve("src");}
        else {
            target = target.resolve(fileName);}
        try {
            outFile = new PrintWriter(currentFileName);
            for (int i = 0; i < optionList.size(); i++) {
                outFile.println(optionList.get(i));}
            outFile.close();
            System.out.printf("File \"%s\" saved!\n", target.getFileName());}
        catch (IOException e) {
            System.out.println("IOException Error");}
    }

    private static String openList(Scanner in, ArrayList optionList, boolean needsToBeSaved) throws FileNotFoundException,IOException {

        if (needsToBeSaved) {
            String prompt = "Opening a new list will result in the loss of your current one. Are you sure you want to do this?";
            boolean byeListYN = SafeInput.getYNConfirm(in, prompt);
            if (!byeListYN) {
                return prompt;
            }
        }
        clearList(optionList);
        Scanner inFile;
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        chooser.setFileFilter(filter);
        String line = "";
        Path target = new File(System.getProperty("user.dir")).toPath();
        target = target.resolve("src");
        chooser.setCurrentDirectory(target.toFile());

        try {
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                target = chooser.getSelectedFile().toPath();
                inFile = new Scanner(target);
                System.out.println("Opening File: " + target.getFileName());
                while (inFile.hasNextLine()) {
                    line = inFile.nextLine();
                    optionList.add(line);}
                inFile.close();}
            else {System.out.println("You must select a file! Returning to menu...");}
        }
        catch (IOException e) {
            System.out.println("IOException Error");}
        return line;}
}

