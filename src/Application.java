public class Application {
    public Student student;
    public Course course;
    public int score;
    public int timestamp;
    public boolean isEnrolled;
    public int heapIndex;

    public Application(Student student, Course course, int timestamp) {
        this.student = student;
        this.course = course;
        this.timestamp = timestamp;
        int m = student.department.equals(course.department) ? 1 : 0;
        this.score = 325 * m + 125 * student.year + student.gpa;
        this.isEnrolled = false;
        this.heapIndex = -1;
    }

    // Checks if this application has higher priority than another application
    public boolean compare(Application other) {
        if (this.score != other.score) {
            return this.score > other.score;
        }
        return this.timestamp < other.timestamp;
    }
}
