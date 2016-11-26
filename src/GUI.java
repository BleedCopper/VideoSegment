import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by rissa on 11/16/2016.
 */
public class GUI {
    private JPanel panel1;
    private JButton btnselect;
    private JPanel panContent;
    private JButton btnGenerate;

    private File fdir;
    private static final int ALPHA = 5;

    private ArrayList<double[]> frame_list;

    //    private static final int OHM = 4000;
    public GUI() {
        panContent.setLayout(new GridLayout(0, 6));
        frame_list = new ArrayList<double[]>();

        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int num = f.showOpenDialog(panel1);
                if (num == JFileChooser.APPROVE_OPTION) {
                    File dir = f.getSelectedFile();
                    File[] dirListing = dir.listFiles();

                    PrintWriter pw = null;
                    try {
                        pw = new PrintWriter(new BufferedWriter(new FileWriter(dir.getName()+".txt", true)));
                        pw.println(dir.getPath());
                        pw.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    for (File child : dirListing) {

                        if (!child.getName().endsWith("jpg")) continue;

                        Histogram.computeHistogramWriteToFile(dir.getPath(), child.getName(), dir.getName());
                    }

                }

            }
        });
        btnselect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser();

                int num = f.showOpenDialog(panel1);
                if (num == JFileChooser.APPROVE_OPTION) {
                    File file = f.getSelectedFile();
                    fdir = file;
                    //This is where a real application would open the file.
                    btnselect.setText(file.getName());

//                    int framenum=file.listFiles().length;
//                    if(framenum>1) {

                    //get Histogram of all frames
                    ArrayList<double[]> hisList = new ArrayList<double[]>();
                    hisList = Histogram.readHistogram(fdir.getPath());

                    double data[] = new double[hisList.size() - 1];
                    //compute histogram difference between frames
                    for (int i = 0; i < hisList.size() - 1; i++) {
                        double[] prevHis = hisList.get(i);
                        double[] nextHis = hisList.get(i + 1);

                        double sd = 0;
                        for (int j = 0; j < 159; j++) {
                            sd += (Math.abs(prevHis[j] - nextHis[j]));
                        }
                        //store histogram difference
                        data[i] = sd;
                    }

                    //compute mean, sd, upper threshold (threshold) and lower threshold (OHM)
                    Statistics stat = new Statistics(data);
                    double mean = stat.getMean();
                    double standev = stat.getStdDev();
                    double threshold = mean + (ALPHA * standev);
                    double OHM = threshold / 2;

                    System.out.println(hisList.size() + " " + mean + " " + standev + " " + threshold);
//                        for (int i=0; i<framenum-1;i++) System.out.println(data[i]);

                    ArrayList<Integer> abrupt = new ArrayList<Integer>();
                    ArrayList<int[]> gradual = new ArrayList<int[]>();
                    for (int i = 0; i < hisList.size() - 1; i++) {

                        //use upper threshold for abrupt transitions
                        if (data[i] > threshold)
                            abrupt.add(i + 1);

                        //use lower threshold to detect gradual transitions
                        else if (data[i] > OHM && i < hisList.size() - 1) {

                            //check if current frame belongs to an existing gradual transition, otherwise, test if it can be the start of a gradual transition
                            if (gradual.size() == 0 || gradual.get(gradual.size() - 1)[1] < i) {

                                int seg = 0;
                                int j = i + 1;
                                double sum = data[i];
                                //next frames must remain above the lower threshold. Leeway of three frames
                                while (j < hisList.size() - 1 && (data[j] > OHM || seg < 3)) {
                                    //three frame leeway
                                    if (data[j] <= OHM)
                                        seg++;

                                    //accumulate the histogram difference
                                    if (seg < 4) sum += data[j];
                                    j++;
                                }

                                //accumulated difference must be above the upper threshold to be considered as gradual transition
                                if (sum > threshold) {
                                    gradual.add(new int[]{i, j});
                                }
                            }
                        }
                    }

                    //contains the intervals for both abrupt and gradual
                    ArrayList<Interval> interval_list = new ArrayList<Interval>();

                    for(int i=0; i<abrupt.size(); i++){
                        Interval interval = new Interval(abrupt.get(i), 0);
                        interval_list.add(interval);
                    }

                    for(int i=0; i<gradual.size(); i++){
                        Interval interval = new Interval(gradual.get(i)[0], gradual.get(i)[1]);
                        interval_list.add(interval);
                    }

                    interval_list.add(new Interval(hisList.size(),0));

                    Collections.sort(interval_list, new Comparator<Interval>() {
                        @Override
                        public int compare(Interval o1, Interval o2) {
                            if (o1.getStart() > o2.getStart())
                                return 1;
                            else if (o1.getStart() < o2.getStart())
                                return -1;
                            else
                                return 0;
                        }
                    });

                    //print abrupt and gradual transitions
                    System.out.println(interval_list);
//                    for (int i = 0; i < gradual.size(); i++) {
//                        System.out.println(gradual.get(i)[0] + " " + gradual.get(i)[1]);
//                    }
//
                    int start = 0;
                    int end = 0;
                    //start looking for the keyframes of the shots before abrupt
                    for (int i = 0; i < interval_list.size(); i++) {


                        if(interval_list.get(i).getEnd() == 0) {
                            end = interval_list.get(i).getStart();
                            int kframe = computeAverageHistogram(start, end, hisList);
                            System.out.println("Keyframe Abrupt: " + kframe);
//                            panContent.add(new JLabel());
                            start = interval_list.get(i).getStart()+1;
                        }
                        else {
                            end = interval_list.get(i).getStart();
                            int kframe = computeAverageHistogram(start, end, hisList);
                            System.out.println("Keyframe Gradual: " + kframe);
                            start = interval_list.get(i).getEnd()+1;
                        }






                    }
                }
            }
        });
    }

    public int computeAverageHistogram(int start, int end, ArrayList<double[]> hsList) {
        int keyframe_file = 0;
        double average_histogram[] = new double[159];

        //get the average histogram of the shot
        for (int i = 0; i < 159; i++) {

            for (int j = start; j < end; j++)
                average_histogram[i] += hsList.get(j)[i];

            average_histogram[i] /= 159;
          //  System.out.println("avg histo: "+ average_histogram[i]);
        }


        double min_distance = -1;

        //find the keyframe of the shot
        for (int i = start; i < end; i++) {
            double temp_distance = 0;

            for (int j = 0; j < 159; j++)
                temp_distance += Math.abs(hsList.get(i)[j] - average_histogram[j]);

        //    System.out.println("temp distance: "+ temp_distance);

            if ( min_distance==-1 || temp_distance <= min_distance) {
                min_distance = temp_distance;
                keyframe_file = i;
            }

        }
        System.out.println("Min-distance: "+ min_distance);
        System.out.println("keyframe-index: "+ keyframe_file);
        return keyframe_file;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
        frame.setSize(new Dimension(1000, 800));
        frame.setVisible(true);
    }
}
