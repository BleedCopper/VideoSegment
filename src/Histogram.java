import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class Histogram {

    public static void computeHistogramWriteToFile(String PATH, String FILENAME, String textfile) {

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(textfile+".txt", true)));
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

    public static ArrayList<Data> readHistogram(String PATH) {
        ArrayList<Data> fileHistogram_list = new ArrayList<Data>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(PATH)));
            String folder = br.readLine();
            String line;
            while((line = br.readLine()) != null) {

                String filename = line;
                line = br.readLine();
//                System.out.println(line);
                String[] stringArrayOfHistogramValues = line.split(" ");
                double[] histogram = new double[159];
                int i=0;
                for(String str: stringArrayOfHistogramValues) {
                    histogram[i] = Double.parseDouble(str);
                    i++;
                }

                Data d = new Data(folder, filename, histogram);
                fileHistogram_list.add(d);
            }

            //System.out.println(line);



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileHistogram_list;
    }

    public static double[] computeHistogram(String PATH, String FILENAME) {

        File file = new File(PATH, FILENAME);
        BufferedImage bi;

        System.out.println(PATH+" "+FILENAME);
        double[] origFileHistogram = new double[159];
        try {
            bi = ImageIO.read(file);

            if(bi==null) System.out.println("wut");

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