package org.example;

/*
 * Khai báo interface
 * Lưu ý: mỗi dạng biểu thức Lambda phải khai báo một interface
 * và bên trong chỉ có duy nhất một phương thức
 * để tránh người dùng khai báo nhiều phương thức trong giao diện
 * bạn thêm annotaion @FunctionalInterface vào nhé
 */
@FunctionalInterface
interface Hanam88 {
    void welcome();
}


public class Program {
//    static class A implements Hanam88
//    {
//
//        @Override
//        public void welcome() {
//            System.out.println("Welcome to hanam88.com");
//        }
//    }
    public static void main(String[] args) {
        //Cách sử dụng lớp nặc danh (annonymous class) và thực thi phương thức của interface
        // đang hiểu là 1 class thực thi cái Hanam88 inter face
        Hanam88 hh = new Hanam88() {
            @Override
            public void welcome() {
                System.out.println("Welcome to hanam88.com");
            }
        };
        hh.welcome();
        //Cách sử dụng biểu thức Lambda và thực thi phương thức của interface (rõ ràng code ngắn hơn)
        Hanam88 hh1 = () -> System.out.println("Welcome to hanam88.com");
        hh1.welcome();
    }

}
