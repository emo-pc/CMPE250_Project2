public class Course implements Comparable<Course> {
    public String code;
    public String department;
    public String day;
    public int dayIndex;
    public String start;
    public String end;
    public int startMin;
    public int endMin;
    public String building;
    public int capacity;
    public int minGpa;
    public CourseHeap enrolled;
    public CourseHeap waiting;

    private String toDept(String code){
        int i=0;
        while(i<code.length()&&!Character.isDigit(code.charAt(i))) {
            i++;
        }
        return code.substring(0, i);
    }

    private int parseTime(String time){
        int hour=Integer.parseInt(time.substring(0,2));
        int minute=Integer.parseInt(time.substring(3,5));
        return hour*60+minute;
    }
    Course(String code,String capacity,String minGpa,String day,String start,String end,String building){
        this.code = code;
        this.department = toDept(code);
        this.capacity = Integer.parseInt(capacity);
        this.minGpa = Integer.parseInt(minGpa.replace(".",""));
        this.day = day;
        this.dayIndex=switch (day){
            case "Mon" -> 0;
            case "Tue" -> 1;
            case "Wed" -> 2;
            case "Thu" -> 3;
            case "Fri" -> 4;
            default -> -1;
        };
        this.start = start;
        this.end = end;
        this.building = building;
        this.startMin = parseTime(start);
        this.endMin = parseTime(end);
        this.enrolled = new CourseHeap(false); // Min-heap: weakest enrolled at top
        this.waiting = new CourseHeap(true);   // Max-heap: strongest waiting at top
    }

    @Override
    public int compareTo(Course other) {
        long lhs = (long) this.enrolled.size * other.capacity;
        long rhs = (long) other.enrolled.size * this.capacity;
        if (lhs != rhs) {
            return lhs > rhs ? -1 : 1;
        }
        return this.code.compareTo(other.code);
    }
}
