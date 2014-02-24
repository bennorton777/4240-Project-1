package scanner;

import java.io.*;

/**
 * Created by sjmitchell on 2/23/14.
 */
public class ScannerUtil {
    public static String getPartString(String fileName, int lineNum, int colNum) throws IOException {
        BufferedReader bReader;
        String curr;
        bReader = new BufferedReader(new FileReader(fileName));
        curr = bReader.readLine();
        int currLine = 1;
        while (currLine < lineNum) {
            curr = bReader.readLine();
            currLine++;
        }
        return curr.substring(0,colNum).trim();
    }
}
