package myTest;

public class Student {
    private int age;

    private String name;

    private String weight;

    public Student(int age, String name, String weight) {
        this.age = age;
        this.name = name;
        this.weight = weight;

    }

    public String getWeight() {
        return weight;
    }
    public void setWeight(String weight) {
        this.weight = weight;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}