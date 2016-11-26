/**
 * Created by sharkscion on 11/26/2016.
 */
public class Data {

    private String folder_path;
    private String filename;
    private double[] Histogram;

    public Data(String folder_path, String filename, double[] histogram){
        setFolder_path(folder_path);
        setFilename(filename);
        setHistogram(histogram);
    }

    public String getFolder_path() {
        return folder_path;
    }

    public void setFolder_path(String folder_path) {
        this.folder_path = folder_path;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public double[] getHistogram() {
        return Histogram;
    }

    public void setHistogram(double[] histogram) {
        Histogram = histogram;
    }

}
