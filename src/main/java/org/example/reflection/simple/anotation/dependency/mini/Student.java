package org.example.reflection.simple.anotation.dependency.mini;

@Entity(table = "students")
public class Student {
    @Id(autoIncrement = true)
    @Column(name = "id", type = "INT")
    private int id;

    @Column(name = "full_name", type = "VARCHAR(150)")
    private String name;
}
