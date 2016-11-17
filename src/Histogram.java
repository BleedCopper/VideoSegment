import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class Histogram {

    public static void computeHistogramWriteToFile(String PATH, String FILENAME) {

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("Histogram.txt", true)));
            pw.println(FILENAME);

            double[] fileHistogram = computeHistogram(PATH, FILENAME);
            for(int i=0; i<fileHistogram.length; i++) {
                pw.print(fileHistogram[i] + " ");
            }

            pw.println();

            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[] readHistogram(String PATH, String FILENAME) {
        double[] fileHistogram = new double[159];

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("Histogram.txt")));
            String line;
            while((line = br.readLine()) != null) {
                if(line.contains(FILENAME)) {
                    line = br.readLine();
                    break;
                }
            }

            //System.out.println(line);

            String[] stringArrayOfHistogramValues = line.split(" ");

            int i=0;
            for(String str: stringArrayOfHistogramValues) {
                fileHistogram[i] = Double.parseDouble(str);
                i++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileHistogram;
    }

    public static double[] computeHistogram(String PATH, String FILENAME) {

        File file = new File(PATH, FILENAME);
        BufferedImage bi;

        double[] origFileHistogram = new double[159];
        try {
            bi = ImageIO.read(file);

            int totalPixels = bi.getHeight() * bi.getWidth();

            for (int i = 0; i < bi.getWidth(); i++) {
                for (int j = 0; j < bi.getHeight(); j++) {

                    double R, G, B;

                    Color c = new Color(bi.getRGB(i, j));

                    R = c.getRed();   //get the 8-bit values of RGB (0-255)
                    G = c.getGreen();
                    B = c.getBlue();

                    cieConvert ColorCIE = new cieConvert();
                    ColorCIE.setValues(R / 255.0, G / 255.0, B / 255.0);

                    origFileHistogram[ColorCIE.IndexOf()]++;

                }
            }

//            for (int i = 0; i < origFileHistogram.length; i++) {
//                origFileHistogram[i] = origFileHistogram[i] / totalPixels;
//                //System.out.print(origFileHistogram[i] + ", ");
//            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return origFileHistogram;
    }


}