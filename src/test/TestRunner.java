package test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        String test_dir;
        if (args.length == 0) {
            test_dir = "test_input";
        } else if (args.length == 1) {
            test_dir = args[0];
        } else {
            System.err.println("Usage: TestRunner [test_dir]");
            return;
        }

        File folder = new File(test_dir);
        if (!folder.isDirectory()) {
            System.err.println(folder.getAbsolutePath() + " is not a directory.");
            return;
        }

        List<File> inputs = getTestFiles(folder);

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

            File outFile = new File(input.getAbsolutePath().replaceAll("\\.tiger$",".out"));
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
        test.FakeParser.main(new String[]{file.getAbsolutePath()});
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
