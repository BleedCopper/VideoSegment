import java.io.*;

/**
 * Created by sharkscion on 11/25/2016.
 */
public class Driver {

    static String FILENAME = "uni";
    public static void main(String[] args) {

        String PATH = "C:\\Users\\sharkscion\\Desktop\\MP2-JPEG\\uni";
        //String FILENAME = "";

        File dir = new File(PATH);
        File[] dirListing = dir.listFiles();

        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new BufferedWriter(new FileWriter(FILENAME+".txt", true)));
            pw.println(PATH);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (File child : dirListing) {

            if (!child.getName().endsWith("jpg")) continue;

            Histogram.computeHistogramWriteToFile(PATH, child.getName(), FILENAME);
        }


    }
}
