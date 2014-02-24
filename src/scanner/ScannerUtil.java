package scanner;

import java.io.*;

/**
 * Created by sjmitchell on 2/23/14.
 */
public class ScannerUtil {

    BufferedReader bReader;
    private int lineNum;
    private int colNum;
    private String curr;

    public ScannerUtil(String fileName, int lineNum, int colNum) throws IOException {
        bReader = new BufferedReader(new FileReader(fileName));
        this.lineNum = lineNum;
        this.colNum = colNum;
    }

    public String getPartString() throws IOException {
        curr = bReader.readLine();
        int currLine = 1;
        while (currLine < lineNum) {
            curr = bReader.readLine();
            currLine++;
        }
        return bReader.readLine().substring(0,colNum);
    }
}
