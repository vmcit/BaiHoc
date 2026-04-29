package org.example.reflection.simple.anotation.dependency;

public class Main {
    public static void main(String[] args) {
        try {
            // Nhờ Container khởi tạo UserController
            // Nó sẽ tự động nhận diện EmailService bị thiếu và khởi tạo/bơm vào luôn
            UserController controller = MyDIContainer.getBean(UserController.class);

            // Kiểm tra kết quả
            controller.registerUser();
            controller.sendMessage();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
