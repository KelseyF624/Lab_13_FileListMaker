import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.nio.file.*;
import javax.swing.JFileChooser;
import java.util.ArrayList;
import java.util.Scanner;

public class FileListMaker {

    static boolean needsToBeSaved = false;
    static File currentFile = null;
    static ArrayList<String> optionList = new ArrayList<>();

    public static void main(String[] args) throws IOException {

            Scanner in = new Scanner(System.in);
            boolean done = false;
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
                        moveItem(optionList, needsToBeSaved);
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
                        break;}

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
                "A or I: add an item. \n D: Delete an item. \n P or V: Print the list. \n Q: Quit. \n M: Move an item. \n O: Open a list. \n S: Save list. \n C: Clear list. \n", "[AaIiDdPpVvQqMmOoSsCc]"); }

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

