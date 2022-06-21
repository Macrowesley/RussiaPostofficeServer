package cc.mrbird.febs.test.entity;

public class TestUser {
    private String id;
    private String name;
    private Integer age;
    private Integer grade;

    // ... ignore getter and setter


    // override its toString method
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", grade=" + grade +
                '}';
    }
}
