package org.example.reflection.simple.anotation.dependency.mini;

public class MainMini {
    public static void main(String[] args) {
        try {
            User user = new User(1, "Nguyen Van A", "aaa@example.com");




            MiniHibernate hibernate = new MiniHibernate();
            // 1. Tự động tạo bảng nếu chưa có (Chỉ cần truyền Class vào)
            hibernate.createTable(User.class);
            hibernate.persist(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
