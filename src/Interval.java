import java.util.Comparator;

/**
 * Created by sharkscion on 11/25/2016.
 */
public class Interval{

    private int start;
    private int end;

    public Interval(int start, int end){
        setStart(start);
        setEnd(end);
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String toString(){
        return String.valueOf(getStart() + "_"+ getEnd());
    }

//    @Override
//    public int compare(Object o1, Object o2) {
//        Integer p1 = ((Interval) o1).getStart();
//        Integer p2 = ((Interval) o2).getStart();
//
//        if (p1 > p2) {
//            return p1;
//        } else if (p1 < p2){
//            return p2;
//        } else {
//            return p1;
//        }
//    }

//    @Override
//    public int compareTo(Object o) {
//
//
//        return 0;
//    }
}
