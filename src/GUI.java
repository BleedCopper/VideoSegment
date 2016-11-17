import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by rissa on 11/16/2016.
 */
public class GUI {
    private JPanel panel1;
    private JButton btnselect;
    private JPanel panContent;

    private File fdir;
    private static final int ALPHA = 5;
//    private static final int OHM = 4000;
    public GUI() {

        btnselect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int num = f.showOpenDialog(panel1);
                if (num == JFileChooser.APPROVE_OPTION) {
                    File file = f.getSelectedFile();
                    fdir = file;
                    //This is where a real application would open the file.
                    btnselect.setText(file.getName());

                    int framenum=file.listFiles().length;
                    double data[] = new double[framenum-1];
                    if(framenum>1) {

                        //get Histogram of all frames
                        ArrayList<double[]> hisList=new ArrayList<double[]>();
                        for (int i = 0; i < framenum; i++) {
                            File temp = file.listFiles()[i];
                            hisList.add(Histogram.computeHistogram(fdir.getPath(), temp.getName()));
//                            System.out.println(temp.getName());
                        }

                        //compute histogram difference between frames
                        for (int i = 0; i < framenum - 1; i++) {
                            double[] prevHis = hisList.get(i);
                            double[] nextHis = hisList.get(i+1);

                            double sd=0;
                            for (int j=0; j<159; j++){
                                sd+=(Math.abs(prevHis[j]-nextHis[j]));
                            }
                            //store histogram difference
                            data[i]=sd;
                        }

                        //compute mean, sd, upper threshold (threshold) and lower threshold (OHM)
                        Statistics stat = new Statistics(data);
                        double mean = stat.getMean();
                        double standev = stat.getStdDev();
                        double threshold = mean + (ALPHA*standev);
                        double OHM = threshold/2;

                        System.out.println(framenum+" "+mean+" "+standev+" "+threshold);
//                        for (int i=0; i<framenum-1;i++) System.out.println(data[i]);


                        ArrayList<Integer> abrupt = new ArrayList<Integer>();
                        ArrayList<int[]> gradual = new ArrayList<int[]>();
                        for (int i = 0; i < framenum - 1; i++) {

                            //use upper threshold for abrupt transitions
                            if(data[i]>threshold){
                                abrupt.add(i+1);
                            }

                            //use lower threshold to detect gradual transitions
                            else if(data[i]>OHM && i<framenum-1) {

                                //check if current frame belongs to an existing gradual transition, otherwise, test if it can be the start of a gradual transition
                                if(gradual.size()==0||gradual.get(gradual.size()-1)[1]<i) {

                                    int seg = 0;
                                    int j = i + 1;
                                    double sum = data[i];
                                    //next frames must remain above the lower threshold. Leeway of three frames
                                    while (j < framenum - 1 && (data[j] > OHM || seg < 3)) {
                                        //three frame leeway
                                        if (data[j] <= OHM) {
                                            seg++;
                                        }
                                        //accumulate the histogram difference
                                        if (seg < 4) sum += data[j];
                                        j++;
                                    }

                                    //accumulated difference must be above the upper threshold to be considered as gradual transition
                                    if (sum>threshold){
                                        gradual.add(new int[]{i, j});
                                    }
                                }
                            }
                        }

                        //print abrupt and gradual transitions
                        System.out.println(abrupt);
                        for (int i=0; i<gradual.size(); i++){
                            System.out.println(gradual.get(i)[0]+" "+gradual.get(i)[1]);
                        }


                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("GUI");
        frame.setContentPane(new GUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.pack();
        frame.setSize(new Dimension(1000,800));
        frame.setVisible(true);


    }
}
