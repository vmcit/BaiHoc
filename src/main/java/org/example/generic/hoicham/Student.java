package org.example.generic.hoicham;

import java.util.List;

public class Student {
    String name;
    int age;

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return name + " (" + age + " tuổi)";
    }

    public static void main(String[] args) {
        // Khai báo: Key là String (Tên lớp), Value là Student
        GroupData<String, Student> classManager = new GroupData<>();

        // Thêm sinh viên vào các lớp khác nhau
        classManager.addElement("Lớp Java", new Student("An", 20));
        classManager.addElement("Lớp Java", new Student("Bình", 21));
        classManager.addElement("Lớp Python", new Student("Chi", 19));
        classManager.addElement("Lớp Java", new Student("Dũng", 22));
        classManager.addElement("Lớp Python", new Student("Đông", 20));

        // In toàn bộ để xem kết quả nhóm
        classManager.showAll();

        // Thử lấy riêng danh sách lớp Java
        List<Student> javaStudents = classManager.getGroup("Lớp Java");
        System.out.println("Số sinh viên lớp Java: " + javaStudents.size());
    }
}
