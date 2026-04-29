package org.example.reflection.simple.anotation.dependency.mini;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MiniHibernate {

    private String url = "jdbc:mysql://localhost:3306/my_db";
    private String user = "root";
    private String password = "12345";
    public void persist(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        List<String> columnNames = new ArrayList<>();
        List<String> columnValues = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            // BỎ QUA những trường có @Id và autoIncrement = true
            if (field.isAnnotationPresent(Id.class)) {
                if (field.getAnnotation(Id.class).autoIncrement()) {
                    continue; // Không chèn ID, để DB tự làm
                }
            }

            if (field.isAnnotationPresent(Column.class)) {
                field.setAccessible(true);
                columnNames.add(field.getAnnotation(Column.class).name());
                Object value = field.get(object);
                columnValues.add(value instanceof String ? "'" + value + "'" : value.toString());
            }
        }

        // Tạo câu INSERT (Lúc này chỉ còn name, email... không có id)
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s);",
                clazz.getAnnotation(Entity.class).table(),
                String.join(", ", columnNames),
                String.join(", ", columnValues));

        System.out.println("Generated SQL: " + sql);
        // Trong thực tế, bạn sẽ dùng JDBC để thực thi câu sql này.
        // --- BƯỚC 2: Dùng JDBC để thực thi ---
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Lưu thành công vào Database!");
            }
        }
    }

    // Hàm tự động tạo bảng dựa trên Class
    public void createTable(Class<?> clazz) throws Exception {
        if (!clazz.isAnnotationPresent(Entity.class)) return;

        String tableName = clazz.getAnnotation(Entity.class).table();
        List<String> columnsQueries = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                StringBuilder colDef = new StringBuilder(column.name() + " " + column.type());

                // Kiểm tra nếu là khóa chính
                if (field.isAnnotationPresent(Id.class)) {
                    colDef.append(" PRIMARY KEY");

                    // Nếu có đánh dấu tự tăng
                    Id idAnnotation = field.getAnnotation(Id.class);
                    if (idAnnotation.autoIncrement()) {
                        colDef.append(" AUTO_INCREMENT");
                    }
                }

                columnsQueries.add(colDef.toString());
            }
        }

        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s);",
                tableName, String.join(", ", columnsQueries));

        System.out.println("Generated DDL: " + sql);

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Bảng đã sẵn sàng!");
        }
    }
}
