package org.example.reflection.simple.anotation;

public class Employee {
    private String name;

    @Range(min = 18, max = 60, message = "Tuổi lao động phải từ 18 đến 60")
    @JsonField("employee_age")
    private int age;

    @Range(min = 1, max = 10, message = "Bậc lương chỉ từ 1 đến 10")
    @JsonField("employee_grade")
    private int grade;

    public Employee(String name, int age, int grade) {
        this.name = name;
        this.age = age;
        this.grade = grade;
    }
}
