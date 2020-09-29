import java.util.ArrayList;
import java.util.List;

public class Task {

    protected int id, time, staff;
    protected String name;
    protected int earliestStart, latestStart;
    protected ArrayList<Task> outEdges = new ArrayList<>();
    protected ArrayList<Task> inEdges = new ArrayList<>();
    protected int cntPredecessors;
    protected int count;
    protected boolean isVisited = false;
    protected boolean critical;
    protected int slack;

    public Task(int id, int time, int staff, String name) {
        this.id = id;
        this.time = time;
        this.staff = staff;
        this.name = name;
    }

    public void setEarliestStart(int e) {
        this.earliestStart = e;
    }

    public void setLatestStart(int l) {
        this.latestStart = l;
    }

    public void addInEdges (Task t) {
        inEdges.add(t);
    }

    public void addOutEdges(Task t) {
        this.outEdges.add(t);
    }

    public List<Task> getOutEdges() {
        return outEdges;
    }
    public List<Task> getInEdges() {
        return inEdges;
    }

    public int getId() {
        return id;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void visit() {
        isVisited = true;
    }

    public void makeCritical() {
        critical = true;
    }

}
