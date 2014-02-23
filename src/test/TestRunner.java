package test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestRunner {
    static String test_dir = "test_input";
    public static void main(String[] args) {
        List<File> inputs;
        if (args.length == 0) {
            File folder = new File(test_dir);
            if (!folder.isDirectory()) {
                System.err.println(folder.getAbsolutePath() + " is not a directory.");
                return;
            }
            inputs = getTestFiles(folder);
            Collections.sort(inputs);
        } else if (args.length == 1) {
            File file = new File(args[0]);
            if (!file.exists()) {
                System.err.println(file.getAbsolutePath() + " does not exist.");
                return;
            }
            File outFile = new File(file.getAbsolutePath().replaceAll("\\.tiger$",".out"));
            if (!outFile.exists()) {
                System.err.println(outFile.getAbsolutePath() + " does not exist.");
                return;
            }
            inputs = new ArrayList<File>();
            inputs.add(file);
        } else {
            System.err.println("Usage: TestRunner [tiger_file]");
            return;
        }

        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;

        for (File input : inputs) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(os);
            System.setOut(ps);
            System.setErr(ps);
            try {
                parseFile(input);
            } catch (IOException e) {
                System.setErr(oldErr);
                System.err.println("Error reading " + input + ":");
                e.printStackTrace();
                return;
            }
            System.setOut(oldOut);
            System.setErr(oldErr);

            File outFile = new File(input.getAbsolutePath().replaceAll("\\.tiger$", ".out"));
            String expected;
            try {
                expected = readFile(outFile);
            } catch (IOException e) {
                System.err.println("Error reading " + outFile.getAbsolutePath() + ":");
                e.printStackTrace();
                return;
            }
            if (expected.equals(os.toString())) {
                System.out.println("Test "+input.getName()+" passed.");
            } else {
                System.out.println("Test "+input.getName()+" failed - expected");
                System.out.println(expected);
                System.out.println("but got");
                System.out.println(os.toString());
                return;
            }
        }
    }

    private static List<File> getTestFiles(File folder) {
        List<File> inputs = new ArrayList<File>();
        for (File file : folder.listFiles()) {
            if (!file.getName().endsWith(".tiger")) continue;
            if (!(new File(file.getAbsolutePath().replaceAll("\\.tiger$",".out")).exists())) {
                System.err.println("No .out file for "+file.getAbsolutePath()+", skipping");
                continue;
            }
            inputs.add(file);
        }
        return inputs;
    }

    private static void parseFile(File file) throws IOException {
        scanner.Scanner scanner = new scanner.Scanner(file.getAbsolutePath());
        scanner.Token token = scanner.getNextToken();
        while (token != null) {
            System.out.print(token.getType());
            token = scanner.getNextToken();
            if (token != null) System.out.print(" ");
        }
        System.out.print("\n");
        parser.Parser.main(new String[]{file.getAbsolutePath()});
    }

    private static String readFile(File file) throws IOException {
        StringBuilder sb = new StringBuilder((int)file.length());
        java.util.Scanner scanner = new java.util.Scanner(file);
        String lineSeparator = System.getProperty("line.separator");

        try {
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
                sb.append(lineSeparator);
            }
            return sb.toString();
        } finally {
            scanner.close();
        }
    }
}
