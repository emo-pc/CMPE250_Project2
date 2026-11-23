import java.util.ArrayList;

public class Student implements Comparable<Student> {
    public String id;
    public String name;
    public String surname;
    public String department;
    public int year;
    public int gpa;
    public ArrayList<Application> schedule;

    Student(String id, String name, String surname, String department, String year, String gpa) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.department = department;
        this.year = Integer.parseInt(year);
        this.gpa = Integer.parseInt(gpa.replace(".", ""));
        this.schedule = new ArrayList<>();
    }

    // Finds existing application for a course in the student's active schedule
    public Application findApplication(String courseCode) {
        for (int i = 0; i < schedule.size(); i++) {
            Application app = schedule.get(i);
            if (app.course.code.equals(courseCode)) {
                return app;
            }
        }
        return null;
    }

    // Inserts an application into the schedule keeping chronological order (dayIndex, then startMin)
    public void addToSchedule(Application app) {
        int i = 0;
        while (i < schedule.size()) {
            Course c = schedule.get(i).course;
            if (c.dayIndex > app.course.dayIndex ||
                (c.dayIndex == app.course.dayIndex && c.startMin > app.course.startMin)) {
                break;
            }
            i++;
        }
        schedule.add(i, app);
    }

    public void removeFromSchedule(Application app) {
        schedule.remove(app);
    }

    @Override
    public int compareTo(Student other) {
        return this.id.compareTo(other.id);
    }
}
