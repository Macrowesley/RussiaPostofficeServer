package myTest;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Student> list= new ArrayList<>();
        Student stu1 = new Student(18,"wang","50");
        Student stu2 = new Student(19,"li","60");
        Student stu3 = new Student(13,"hu","55");

        list.add(stu1);
        list.add(stu2);
        list.add(stu3);
        listSortTest.sortIntMethod(list);
    }
}
