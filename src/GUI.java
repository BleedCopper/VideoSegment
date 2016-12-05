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
    private JPanel panMainContent;
    private JButton btnselect;
    private JPanel panContent;
    private JButton btnGenerate;
    private JScrollPane spImages;
    private JLabel lblImage;
    private JPanel panInfo;
    private JPanel panLegend;
    private JLabel lblKeyframe;
    private JPanel panKeyFrame;
    private JPanel panGradual;
    private JLabel lblGradual;
    private JPanel panAbrupt;
    private JLabel lblAbrupt;
    private JPanel panResult;
    private JLabel lblAbruptResults;
    private JLabel lblGradualResults;
    private JLabel lblKeyframeResults;
    private JPanel panSpaneContent;

    private File fdir;
    private static final double ALPHA = 5.5;

    //contains the computed distance value from compute average histogram
    private ArrayList<Integer> gradual_list;
    //contains the intervals for both abrupt and gradual
    private ArrayList<Interval> interval_list;
    private ArrayList<Integer> keyframe_index;
    private String dir_path;

    //    private static final int OHM = 4000;
    public GUI() {

        panContent.setLayout(new BorderLayout());
        panContent.add(panInfo, BorderLayout.NORTH);

        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int num = f.showOpenDialog(panMainContent);
                if (num == JFileChooser.APPROVE_OPTION) {
                    File dir = f.getSelectedFile();
                    File[] dirListing = dir.listFiles();

                    PrintWriter pw = null;
                    try {
                        pw = new PrintWriter(new BufferedWriter(new FileWriter(dir.getName()+".txt", true)));
                        pw.println(dir.getPath());
                        dir_path = dir.getPath();
                       // System.out.println("DIR GET PATH: "+ dir.getPath());
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

                gradual_list = new ArrayList<Integer>();
                interval_list = new ArrayList<Interval>();
                keyframe_index = new ArrayList<Integer>();

                JFileChooser f = new JFileChooser();

                int num = f.showOpenDialog(panMainContent);
                if (num == JFileChooser.APPROVE_OPTION) {
                    File file = f.getSelectedFile();
                    fdir = file;
                    //This is where a real application would open the file.
                    btnselect.setText(file.getName());

//                    int framenum=file.listFiles().length;
//                    if(framenum>1) {

                    //get Histogram of all frames
                    ArrayList<Data> hisList = new ArrayList<Data>();
                    hisList = Histogram.readHistogram(fdir.getPath());

                    double data[] = new double[hisList.size() - 1];
                    //compute histogram difference between frames
                    for (int i = 0; i < hisList.size() - 1; i++) {
                        double[] prevHis = hisList.get(i).getHistogram();
                        double[] nextHis = hisList.get(i + 1).getHistogram();

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
                    double OHM = threshold * 0.35;

                    System.out.println(hisList.size() + " " + mean + " " + standev + " " + threshold);

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
                                double tempsum = 0;
                                int tolerance = 3;
                                //next frames must remain above the lower threshold. Leeway of three frames
                                while (j < hisList.size() - 1 && (data[j] > OHM || seg < tolerance)) {
                                    //three frame leeway
                                    if (data[j] <= OHM) {
                                        seg++;
                                        tempsum+=seg;
                                    }else {
//                                        seg=0;
//                                        tempsum=0;
                                        if(seg>0){
                                            seg=tolerance;
                                            tempsum=0;
                                        }
                                    }
                                    //accumulate the histogram difference
                                    if (seg < tolerance+1) sum += data[j];
                                    j++;
                                }
                                sum-=tempsum;

                                //accumulated difference must be above the upper threshold to be considered as gradual transition
                                if (sum > threshold) {
                                    if(tempsum>0) gradual.add(new int[]{i, j-tolerance});
                                    else gradual.add(new int[]{i, j});
                                }
                            }
                        }
                    }



                    String temp = "Abrupt Frames: ";
                    for(int i=0; i<abrupt.size(); i++){
                        Interval interval = new Interval(abrupt.get(i), 0);
                        temp += abrupt.get(i);

                        if(i != abrupt.size()-1)
                            temp += " , ";

                        interval_list.add(interval);
                    }

                    lblAbruptResults.setText(temp);
                    lblAbruptResults.revalidate();
                    lblAbruptResults.updateUI();


                    temp = "Gradual Boundaries: ";
                    for(int i=0; i<gradual.size(); i++){
                        Interval interval = new Interval(gradual.get(i)[0], gradual.get(i)[1]);

                        temp += gradual.get(i)[0]+"-"+gradual.get(i)[1];

                        for(int j = gradual.get(i)[0]; j<= gradual.get(i)[1]; j++)
                            gradual_list.add(j);

                        if(i != gradual.size()-1)
                            temp += " , ";

                        interval_list.add(interval);
                    }

                    lblGradualResults.setText(temp);
                    lblGradualResults.revalidate();
                    lblGradualResults.updateUI();

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

                    int start = 0;
                    int end = 0;

                    temp = "Keyframes: ";
                    int kframe;
                    //start looking for the keyframes of the shots before abrupt
                    for (int i = 0; i < interval_list.size(); i++) {


                        if(interval_list.get(i).getEnd() == 0) {
                            end = interval_list.get(i).getStart();
                            kframe = computeAverageHistogram(start, end, hisList);
                            keyframe_index.add(kframe);
                            System.out.println("Keyframe Abrupt: " + kframe);
//                            panContent.add(new JLabel());
                            start = interval_list.get(i).getStart()+1;
                        }
                        else {
                            end = interval_list.get(i).getStart();
                            kframe = computeAverageHistogram(start, end, hisList);
                            keyframe_index.add(kframe);
                            System.out.println("Keyframe Gradual: " + kframe);
                            start = interval_list.get(i).getEnd()+1;
                        }

                        temp+= kframe;

                        if(i != interval_list.size()-1)
                            temp += " , ";

                    }

                    lblKeyframeResults.setText(temp);
                    lblKeyframeResults.revalidate();
                    lblKeyframeResults.updateUI();

                    panSpaneContent = new JPanel();
                    panSpaneContent.setSize(300,300);
                    panSpaneContent.setLayout(new GridLayout(0,4));
                    //panSpaneContent.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

                   // panSpaneContent.setLayout(new BoxLayout(panSpaneContent,BoxLayout.X_AXIS));

                    int width = 0;
                    for(int i=0; i<hisList.size(); i++){
                        String filePath = hisList.get(i).getFolder_path()+"\\"+ hisList.get(i).getFilename();

                        JPanel imgPanel = new JPanel();
                        imgPanel.setLayout(new BorderLayout());

                        JLabel lblImageName =  new JLabel(hisList.get(i).getFilename());
                        if(keyframe_index.contains(i)) {
                            lblImageName.setBackground(Color.BLUE);
                            lblImageName.setForeground(Color.WHITE);
                        }else if(abrupt.contains(i)){
                            lblImageName.setBackground(Color.RED);
                            lblImageName.setForeground(Color.WHITE);
                        }else if(gradual_list.contains(i)){
                            lblImageName.setBackground(Color.GREEN);
                            lblImageName.setForeground(Color.WHITE);
                        }

                        lblImageName.setOpaque(true);
                        lblImageName.revalidate();
                        lblImageName.updateUI();

                        JButton btnImageIcon =  new JButton(new ImageIcon(filePath));
                        btnImageIcon.setBackground(Color.WHITE);
                        width += btnImageIcon.getIcon().getIconWidth();

                        imgPanel.add(btnImageIcon, BorderLayout.CENTER);
                        imgPanel.add(lblImageName, BorderLayout.SOUTH);

                        panSpaneContent.add(imgPanel);
                    }

                    panSpaneContent.updateUI();
                   // panSpaneContent.setPreferredSize(new Dimension(width, 120));
                   // spImages.setViewportView(panSpaneContent);

                    spImages = new JScrollPane(panSpaneContent);
                    spImages.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                    spImages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    spImages.revalidate();
                    spImages.updateUI();
                    panContent.add(spImages,BorderLayout.CENTER);
                    panContent.revalidate();
                    panContent.updateUI();
                }
            }
        });
    }

    public int computeAverageHistogram(int start, int end, ArrayList<Data> hsList) {
        int keyframe_file = 0;
        double average_histogram[] = new double[159];

        //get the average histogram of the shot
        for (int i = 0; i < 159; i++) {

            for (int j = start; j < end; j++)
                average_histogram[i] += hsList.get(j).getHistogram()[i];

            average_histogram[i] /= 159;
          //  System.out.println("avg histo: "+ average_histogram[i]);
        }


        double min_distance = -1;

        //find the keyframe of the shot
        for (int i = start; i < end; i++) {
            double temp_distance = 0;

            for (int j = 0; j < 159; j++)
                temp_distance += Math.abs(hsList.get(i).getHistogram()[j] - average_histogram[j]);

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
        frame.setContentPane(new GUI().panMainContent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
        frame.setSize(new Dimension(1000, 800));
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
