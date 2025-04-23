import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
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

        Scanner in = new Scanner(System.in);
        showItems(optionList);

        if (optionList.isEmpty()) {
            System.out.println("The list is empty.");
            return;
        }

        int fromList = SafeInput.getRangedInt(in, "Enter the item you want to move: ", 1, optionList.size());
        int toList = SafeInput.getRangedInt(in, "Enter the location on the list where you want to move the item.: ", 1, optionList.size());

        String item = (String) optionList.remove(fromList);
        optionList.add(toList, item);
        needsToBeSaved = true;
        System.out.println("Moved " + item + " to " + toList);
    }

    private static void saveList(ArrayList optionList, String fileName) throws IOException{

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
        if (currentFile == null) {
            JFileChooser chooser = new JFileChooser();
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);
            if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                currentFile = chooser.getSelectedFile();
                if (!currentFile.getName().endsWith(".txt")) {
                    currentFile = new File(currentFile.getAbsolutePath() + ".txt");}
            }
        }
        else
        {System.out.println("Save failed.");
            return;}
        Path file = currentFile.toPath();
        OutputStream out = new BufferedOutputStream(new FileOutputStream(file.toString()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        for (Object rec: optionList) {
            writer.newLine();}
        writer.close();
        needsToBeSaved = true;
        System.out.println("Saved " + currentFile + " to " + file.toString());
    }


//    private static void saveList(ArrayList optionList, String fileName) throws IOException {
//
//        if (!hasAFile) {
//            currentFileName = SafeInput.getNonZeroLenString(console, "Enter the file name: ");
//        }
//        outFile = new PrintWriter(new File(currentFileName));
//        for (Object ln : optionList) ;
//        outFile.println(inFile);
//        outFile.close();
//        System.out.println("List saved to: " + fileName);}

    private static void openList(Scanner in, ArrayList optionList, boolean needsToBeSaved) throws FileNotFoundException {

        JFileChooser chooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);
        String readInfo = "";
        File selectedFile = null;
        File subFolder = new File(workingDirectory, "src");

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            inFile = new Scanner(chooser.getSelectedFile());
            Path file = selectedFile.toPath();
            optionList.clear();
            BufferedReader reader = new BufferedReader(new FileReader(file.toFile()));
            while (inFile.hasNextLine()) {
                optionList.add(inFile.nextLine());}
            inFile.close();
            currentFileName = chooser.getSelectedFile().getName();
            hasAFile = true;
            newUnsaved = false;
            fileSaving = false;
            System.out.println("Opened " + currentFileName);
        }
        else {
            System.out.println("You didn't choose a file.");
            return;}
    }
}

