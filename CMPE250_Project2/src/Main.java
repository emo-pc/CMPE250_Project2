import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
        return "";
    }

    static String addCourse(String code, String capacity, String minGpa, String day, String start, String end,
                            String building) {
        // TODO
        return "";
    }

    static String apply(String studentId, String courseCode) {
        // TODO
        return "";
    }

    static String drop(String studentId, String courseCode) {
        // TODO
        return "";
    }

    static String getRegistrationStatus(String studentId, String courseCode) {
        // TODO
        return "";
    }

    static String getStudent(String studentId) {
        // TODO
        return "";
    }

    static String getCourse(String courseCode) {
        // TODO
        return "";
    }

    static String courseOccupancy() {
        // TODO
        return "";
    }

    static String studentRegistration() {
        // TODO
        return "";
    }

    static String route(int phase, String nodeA, String nodeB) {
        if (phase == 1) {
            return "ERROR";
        }
        // TODO
        return "";
    }
}
