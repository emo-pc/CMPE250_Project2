import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * CMPE250 Project 2 - Course Registration System
 *
 * Starter code. Reading the input, splitting each line into words and writing the
 * output are already done for you. Your job is to fill in the TODO parts; you are
 * free to add any number of new classes and to change this file as you like.
 *
 * Usage:
 *   Phase 1: java -cp src Main 1 <input_file> <output_file>
 *   Phase 2: java -cp src Main 2 <graph_file> <input_file> <output_file>
 *
 * Reminder: java.util.ArrayList is the only class you may use from java.util.
 */
public class Main {
    static HashTable<Student> students = new HashTable<>(100003);
    static HashTable<Course> courses = new HashTable<>(10007);
    static ArrayList<Student> allStudents = new ArrayList<>();
    static ArrayList<Course> allCourses = new ArrayList<>();
    static int appCount = 0;

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Usage: java Main 1 <input> <output>  |  java Main 2 <graph> <input> <output>");
            return;
        }

        int phase = Integer.parseInt(args[0]);
        String inputFile;
        String outputFile;

        if (phase == 2) {
            String graphFile = args[1];
            inputFile = args[2];
            outputFile = args[3];
            readGraph(graphFile);
        } else {
            inputFile = args[1];
            outputFile = args[2];
        }

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] p = line.split("\\s+");

            String result;
            switch (p[0]) {
                case "AddStudent":            // AddStudent <ID> <Name> <Surname> <Department> <Year> <GPA>
                    result = addStudent(p[1], p[2], p[3], p[4], p[5], p[6]);
                    break;
                case "AddCourse":             // AddCourse <Code> <Capacity> <MinGPA> <Day> <Start> <End> <Building>
                    result = addCourse(p[1], p[2], p[3], p[4], p[5], p[6], p[7]);
                    break;
                case "Apply":                 // Apply <StudentID> <CourseCode>
                    result = apply(p[1], p[2]);
                    break;
                case "Drop":                  // Drop <StudentID> <CourseCode>
                    result = drop(p[1], p[2]);
                    break;
                case "GetRegistrationStatus": // GetRegistrationStatus <StudentID> <CourseCode>
                    result = getRegistrationStatus(p[1], p[2]);
                    break;
                case "GetStudent":            // GetStudent <StudentID>
                    result = getStudent(p[1]);
                    break;
                case "GetCourse":             // GetCourse <CourseCode>
                    result = getCourse(p[1]);
                    break;
                case "CourseOccupancy":       // CourseOccupancy
                    result = courseOccupancy();
                    break;
                case "StudentRegistration":   // StudentRegistration
                    result = studentRegistration();
                    break;
                case "Route":                 // Route <NodeA> <NodeB>
                    result = route(phase, p[1], p[2]);
                    break;
                default:
                    result = "ERROR";
            }

            // A result may span several lines; separate them with '\n' inside the string.
            writer.write(result);
            writer.write('\n');
        }

        reader.close();
        writer.close();
    }

    // ------------------------------------------------------------------ Phase 2 input

    /** Reads the campus map. Every line of the file is "<NodeA> <NodeB> <Minutes>". */
    static void readGraph(String graphFile) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(graphFile));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            String[] p = line.split("\\s+");
            String nodeA = p[0];
            String nodeB = p[1];
            int minutes = Integer.parseInt(p[2]);
            // TODO: store the path between nodeA and nodeB
        }
        reader.close();
    }

    // ------------------------------------------------------------------ commands
    // Each method returns exactly the text that must be printed for the command
    // (see the description for the formats). All arguments are given as they appear
    // in the input; convert them (numbers, GPA, times) as you need.

    static String addStudent(String id, String name, String surname, String department, String year, String gpa) {
        // TODO
        if (students.get(id)!=null) {
            return "Student "+id+" already exists.";
        }
        Student student = new Student(id, name, surname, department, year, gpa);
        students.put(id, student);
        allStudents.add(student);
        return "Student "+name+" "+surname+" ("+id+") added.";
    }

    static String addCourse(String code, String capacity, String minGpa, String day, String start, String end,
                            String building) {
        // TODO
        if (courses.get(code)!=null) {
            return "Course "+code+" already exists.";
        }
        Course course=new Course(code,capacity,minGpa,day,start,end,building);
        courses.put(code, course);
        allCourses.add(course);
        return "Course "+code+" added.";
    }

    static String apply(String studentId, String courseCode) {
        // TODO
        Student student = students.get(studentId);
        Course course = courses.get(courseCode);
        if (student == null||course == null) {return "ERROR";}
        if (student.findApplication(courseCode)!=null) {
            return studentId+" rejected from "+courseCode+": already applied.";
        }
        if (student.gpa<course.minGpa){
            return studentId+" rejected from "+courseCode+": insufficient GPA.";
        }
        Course previous=null;
        Course next=null;
        for (int i=0;i<student.schedule.size();i++) {
            Course other=student.schedule.get(i).course;
            if (other.dayIndex==course.dayIndex) {
                if (other.startMin<= course.startMin){
                    previous=other;
                }
                else if (next==null) {
                    next=other;
                }
            }
        }
        if (previous!=null&&previous.endMin>course.startMin) {
            return studentId+" rejected from "+courseCode+": time conflict with "+previous.code+".";
        }
        if (next!=null&&next.startMin<course.endMin) {
            return studentId+" rejected from "+courseCode+": time conflict with "+next.code+".";
        }
        appCount++;
        Application app=new Application(student,course,appCount);
        student.addToSchedule(app);
        if (course.enrolled.size<course.capacity){
            app.isEnrolled=true;
            course.enrolled.add(app);
            return studentId+" enrolled in "+courseCode+".";
        }
        else{
            Application weakest=course.enrolled.peek();
            if (app.compare(weakest)){
                course.enrolled.poll();
                weakest.isEnrolled=false;
                course.waiting.add(weakest);
                app.isEnrolled=true;
                course.enrolled.add(app);
                return studentId + " enrolled in " + courseCode + ", " + weakest.student.id + " moved to waiting list.";
            }
            else{
                app.isEnrolled=false;
                course.waiting.add(app);
                return studentId + " added to waiting list of " + courseCode + ".";
            }
        }
    }

    static String drop(String studentId, String courseCode) {
        // TODO
        Student student = students.get(studentId);
        Course course = courses.get(courseCode);
        if (student == null||course == null) {return "ERROR";}
        Application app=student.findApplication(courseCode);
        if (app==null) {
            return studentId + " is not registered or waiting for " + courseCode + ".";
        }
        student.removeFromSchedule(app);
        if (app.isEnrolled==true){
            app.isEnrolled=false;
            course.enrolled.remove(app);
            if (!course.waiting.isEmpty()) {
                Application strongest = course.waiting.poll();
                strongest.isEnrolled=true;
                course.enrolled.add(strongest);
                return studentId + " dropped " + courseCode + ", " + strongest.student.id + " promoted from waiting list.";
            }
            return studentId + " dropped " + courseCode + ".";
        }
        course.waiting.remove(app);
        return studentId + " removed from waiting list of " + courseCode + ".";
    }

    static String getRegistrationStatus(String studentId, String courseCode) {
        // TODO
        Student student = students.get(studentId);
        Course course = courses.get(courseCode);
        if (student == null||course == null) {return "ERROR";}
        Application app=student.findApplication(courseCode);
        if (app==null) {return studentId+" has no application for "+courseCode+".";}
        if (app.isEnrolled==true){
            return studentId+" is enrolled in "+courseCode+".";
        }
        return studentId+" is on the waiting list of "+courseCode+".";
    }

    static String getStudent(String studentId) {
        // TODO
        Student student = students.get(studentId);
        if (student == null) {return "ERROR";}
        if (student.schedule.isEmpty()){
            return "Schedule of " + student.id + " (" + student.name + " " + student.surname + "): empty.";
        }
        StringBuilder sb=new StringBuilder();
        sb.append("Schedule of ");
        sb.append(student.id);
        sb.append(" ("+student.name+" ");
        sb.append(student.surname+"):");
        for (Application app:student.schedule){
            Course course=app.course;
            String status;
            if (app.isEnrolled==true){status="ENROLLED";}
            else {status="WAITING";}
            sb.append("\n- ");
            sb.append(course.code);
            sb.append(" (").append(course.day+" ");
            sb.append(course.start+"-"+course.end);
            sb.append(", "+course.building+"): ");
            sb.append(status);
        }
        return sb.toString();
    }

    static String getCourse(String courseCode) {
        // TODO
        Course course=courses.get(courseCode);
        if (course == null) {return "ERROR";}
        StringBuilder sb=new StringBuilder();
        sb.append("Roster for "+course.code+" ("+course.enrolled.size);
        sb.append("/"+course.capacity+"):\n");
        sb.append("Enrolled:");
        if (course.enrolled.isEmpty()){sb.append(" none");}
        else {
            Application[] enr=course.enrolled.getSortedArray();
            for (int i=0;i<enr.length;i++){
                Student student=enr[i].student;
                sb.append(" "+student.id+" ("+enr[i].score+")");
                if (i!=enr.length-1){
                    sb.append(",");
                }
            }
        }
        sb.append("\n");
        sb.append("Waiting:");
        if (course.waiting.isEmpty()){sb.append(" none");}
        else {
            Application[] wai=course.waiting.getSortedArray();
            for (int i=0;i<wai.length;i++){
                Student student=wai[i].student;
                sb.append(" "+student.id+" ("+wai[i].score+")");
                if (i!=wai.length-1){
                    sb.append(",");
                }
            }
        }
        return sb.toString();
    }

    static <T extends Comparable<T>> void quickSort(ArrayList<T> list, int left, int right) {
        if (left >= right) return;
        T pivot = list.get((left + right) / 2);
        int i = left;
        int j = right;
        while (i <= j) {
            while (list.get(i).compareTo(pivot) < 0) i++;
            while (pivot.compareTo(list.get(j)) < 0) j--;
            if (i <= j) {
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
                i++;
                j--;
            }
        }
        if (left < j) quickSort(list, left, j);
        if (i < right) quickSort(list, i, right);
    }

    static String courseOccupancy() {
        if (allCourses.size() > 1) {
            quickSort(allCourses, 0, allCourses.size() - 1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Course occupancy:");
        for (int i = 0; i < allCourses.size(); i++) {
            Course c = allCourses.get(i);
            sb.append("\n- ").append(c.code).append(": ")
              .append(c.enrolled.size).append("/").append(c.capacity)
              .append(" (waiting: ").append(c.waiting.size).append(")");
        }
        return sb.toString();
    }

    static String studentRegistration() {
        if (allStudents.size() > 1) {
            quickSort(allStudents, 0, allStudents.size() - 1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Student registrations:");
        for (int i = 0; i < allStudents.size(); i++) {
            Student s = allStudents.get(i);
            sb.append("\n- ").append(s.id).append(": ");
            if (s.schedule.isEmpty()) {
                sb.append("none");
            } else {
                for (int j = 0; j < s.schedule.size(); j++) {
                    if (j > 0) sb.append(", ");
                    Application app = s.schedule.get(j);
                    String status = app.isEnrolled ? "ENROLLED" : "WAITING";
                    sb.append(app.course.code).append(" (").append(status).append(")");
                }
            }
        }
        return sb.toString();
    }

    static String route(int phase, String nodeA, String nodeB) {
        if (phase == 1) {
            return "ERROR";
        }
        // TODO
        return "";
    }
}
