import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.io.PrintWriter;

class Planner {

    private static ArrayList<Task> taskList;
    private static ArrayList<Integer> timeStamps = new ArrayList<>();
    private static HashMap<Task, ArrayList<Integer>> edges = new HashMap<>();
    private static int totalTime;
    private static int currentStaff;

    public static void main(String[] args) {
        
        if(args.length == 0) {
            System.out.println("Usage:");
            System.out.println("java Planner <filename.txt>");
            System.exit(1);
        }
        File file = new File(args[0]);
        taskList = readFromFile(file);
        createOutEdges();
        createInEdges();
        PrintWriter f = null;
        try {
            FileWriter fw = new FileWriter("output.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            f = new PrintWriter(bw);
        } catch (Exception e) {
            System.exit(1);
        }
        f.println("Project Name: " + args[0] + "\n");
        ArrayList<Task> ts = TopologiskSortering(f);

        if(ts!=null) {
            findLatestStart();
            setSlack();
            printInfo(f);
        }

    }

    private static ArrayList<Task> readFromFile(File file) {
        ArrayList<Task> tasksFromFile = new ArrayList<>();
        Scanner scanner = null;

        try {
            scanner = new Scanner(file);
        } catch (Exception e) {
            System.out.println("File not found...");
            System.exit(1);
        }
        // int numberOfTasks = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();

        while(scanner.hasNextLine()) {
            ArrayList<Integer> numberList = new ArrayList<>();
            int predCount = 0;

            String string = scanner.nextLine();
            String[] line = string.split("\\s+");
            int taskId = Integer.parseInt(line[0]);
            String taskName = line[1];
            int taskTime = Integer.parseInt(line[2]);
            int manPower = Integer.parseInt(line[3]);

            Task task = new Task(taskId, taskTime, manPower, taskName);
            tasksFromFile.add(task);

            for (int i = 4; i < line.length; i++) {
                if(!line[i].equals("0")) {
                    numberList.add(Integer.parseInt(line[i]));
                    predCount++;
                    task.cntPredecessors++;
                }
                task.count = predCount;
            }
            edges.put(task, numberList);
        }
        return tasksFromFile;
    }

    private static Task getTaskFromId(int i) {
        for(Task t : taskList) {
            if(t.id == i) {
                return t;
            }
        }
        return null;
    }

    private static void createInEdges() {
        for (Task t : taskList) {
            for (Task x : t.getOutEdges()) {
                x.addInEdges(t);
            }
        }
    }

    private static void createOutEdges() {
        for (Task t : edges.keySet()) {
            for(int e : edges.get(t)) {
                Task task = getTaskFromId(e);
                task.addOutEdges((t));
            }
        }
    }

    private static ArrayList<Task> TopologiskSortering(PrintWriter f) {
        Stack<Task> stack = new Stack<>();
        ArrayList<Task> sortedList = new ArrayList<>();
        for(Task t : taskList) {
            if(t.cntPredecessors == 0) {
                t.earliestStart = 0;
                stack.push(t);
                sortedList.add(t);
            }
        }
        int i = 1;
        while(!stack.empty()) {
            Task v = stack.pop();
            i++;
            for(Task t : v.outEdges) {
                t.count--;
                if(t.earliestStart < (v.time + v.earliestStart)) {
                    t.setEarliestStart(v.time + v.earliestStart);
                }
                if(t.count == 0) {
                    stack.push(t);
                    sortedList.add(t);
                }
            }
        }
        for(Task t : taskList) {
            if((t.earliestStart + t.time) > totalTime) {
                totalTime = t.earliestStart + t.time;
            }
        }
        if(i > taskList.size()) {
            return sortedList;
        }
        f.println("Found a cycle, project not realizable:");
        DFS(taskList.get(0), "", f);
        return null;
    }

    private static void DFS(Task t, String cycle, PrintWriter f) {
        t.visit();

        if(cycle.length() == 0 && t.inEdges.size() != 0) {
            cycle = Integer.toString(t.getId());
        } else if (cycle.length() != 0 && t.inEdges.size() != 0){
            cycle = cycle + " -> " + t.getId();
        }

        for (Task v : t.getOutEdges()) {
            if (!v.isVisited()) {
                DFS(v, cycle, f);
            } else if (v.isVisited() && v.cntPredecessors > 1) {
                DFS(v, cycle, f);
            } else if (v.isVisited() && cycle.contains(Integer.toString(v.getId()))) {
                f.println("CYCLE: " + cycle + "\n\n------------------------");
                f.close();
                break;
            }
        }
    }

    private static void findLatestStart() {
        for(Task t : taskList) {
            if(t.earliestStart == 0) {
                int w = getLatestStart(t);
            }
        }
    }

    private static int getLatestStart(Task t) {
        int latestStart = Integer.MAX_VALUE;
        if(t.outEdges.size() == 0) {
            t.setLatestStart(totalTime - t.time);
            if(t.earliestStart == t.latestStart) t.makeCritical();
            return totalTime-t.time;
        }
        for(Task nt : t.outEdges) {
            int tempLatest = getLatestStart(nt);
            if(tempLatest < latestStart) {
                latestStart = tempLatest;
            }
        }
        t.setLatestStart(latestStart-t.time);
        if(t.earliestStart == t.latestStart) t.makeCritical();

        return latestStart-t.time;
    }

    private static void setSlack() {
        for (Task t : taskList) {
            t.slack = t.latestStart - t.earliestStart;
        }
    }

    private static void createTimestamps() {

        for(Task t : taskList) {
            int toAdd = t.earliestStart;
            if(!timeStamps.contains(toAdd)) {
                timeStamps.add(toAdd);
            }
            toAdd = t.earliestStart + t.time;
            if(!timeStamps.contains(toAdd)) {
                timeStamps.add(toAdd);
            }
        }
        Collections.sort(timeStamps);
    }

    private static String getTaskFromTime(int time) {
        String s = "";
        for(Task t : taskList) {
            if(t.earliestStart == time) {
                s += "\t\t\tStarting: " + t.id + "\n";
                currentStaff += t.staff;
            }
            if(t.earliestStart + t.time == time) {
                s+= "\t\t\tFinished: " + t.id + "\n";
                currentStaff -= t.staff;
            }
        }
        return s;
    }
    private static void printSimulation(PrintWriter f) {
        createTimestamps();
        String printOut = "";
        for(Integer i : timeStamps) {
            printOut += "Time: " + i + "\n";
            String s = getTaskFromTime(i);
            if(s.length() != 0) printOut += s;
            if(currentStaff != 0) {
                printOut += "\t\t\tCurrent staff: " + currentStaff + "\n";
            }
        }
        f.println(printOut);
        f.println("**** Shortest possible project execution is " + totalTime + " ****");
    }

    private static void printInfo(PrintWriter f) {
        printSimulation(f);
        f.println("Task details: \n");
        for(Task t : taskList) {
            f.println("ID: " + t.id);
            f.println("Name: " + t.name);
            f.println("Time: " + t.time);
            f.println("Manpower: " + t.staff);
            f.println("Earliest start: " + t.earliestStart);
            f.println("Slack: " + t.slack);
            f.print("Successors: - ");
            for(Task e : t.getOutEdges()) {
                f.print(e.id + " - ");
            }
            f.println();
            f.println("------------------------");

        }
        f.println("\n\n");
        f.close();
    }
}
