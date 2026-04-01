package org.example;

public class Function {


        public static void main(String[] args) {

            // 1. Using a lambda
            Runnable r1 = () -> System.out.println("Hello World 1");

            // 2. Using an anonymous class
            Runnable r2 = new Runnable() {
                public void run() {
                    System.out.println("Hello World 2");
                }
            };

            // Thực thi các phương thức
            process(r1); // Prints "Hello World 1"
            process(r2); // Prints "Hello World 2"

            // 3. Prints "Hello World 3" with a lambda passed directly
            process(() -> System.out.println("Hello World 3"));
        }

        // Phương thức nhận tham số là một Runnable và thực thi nó
        public static void process(Runnable r) {
            r.run();
        }
}

// Functional Interface phổ biến trong Java:
//public interface Comparator<T> {
//    int compare(T o1, T o2);
//}
//
//public interface Runnable {
//    void run();
//}
//
//public interface ActionListener extends EventListener {
//    void actionPerformed(ActionEvent e);
//}
//
//public interface Callable<V> {
//    V call();
//}
//
//public interface PrivilegedAction<V> {
//    T run();
//}

//1. Comparator<T>
//Mục đích: Dùng để so sánh hai đối tượng cùng kiểu T.
//
//Cách hoạt động: Phương thức compare(o1, o2) trả về:
//
//Số âm: Nếu o1 < o2.
//
//Số 0: Nếu o1 == o2.
//
//Số dương: Nếu o1 > o2.
//
//Ứng dụng: Thường dùng khi bạn muốn sắp xếp (sort) một danh sách các đối tượng (ví dụ: sắp xếp danh sách Batch theo ID hoặc theo ngày tạo trong Ephesoft).
//
//        2. Runnable
//Mục đích: Đại diện cho một tác vụ (task) cần được thực thi nhưng không trả về kết quả và không ném ra exception có kiểm soát.
//
//Cách hoạt động: Chỉ có phương thức run().
//
//Ứng dụng: Dùng rất nhiều trong đa luồng (Multi-threading). Khi bạn muốn chạy một đoạn code ở luồng phụ (background thread) để không làm treo giao diện GWT, bạn sẽ bỏ code vào Runnable.
//
//        3. ActionListener
//Mục đích: Dùng để lắng nghe và xử lý sự kiện người dùng (Event Handling).
//
//Cách hoạt động: Khi người dùng tương tác (như click chuột vào nút "Scan" hoặc "Upload"), phương thức actionPerformed(e) sẽ được gọi.
//
//Ứng dụng: Đây là "xương sống" của lập trình giao diện (Swing/AWT và cả GXT/GWT). Hầu hết các nút bấm trong ứng dụng của bạn đều dùng cái này để thực hiện lệnh.
//
//        4. Callable<V>
//Mục đích: Tương tự như Runnable (đại diện cho một task), nhưng có trả về kết quả kiểu V và có thể ném ra Exception.
//
//Cách hoạt động: Phương thức call() thực hiện logic và trả về dữ liệu.
//
//Ứng dụng: Dùng khi bạn chạy một tác vụ tốn thời gian ở server và đợi nó trả về kết quả (ví dụ: yêu cầu server tính toán số lượng trang trong một Batch và trả về con số đó).
//
//        5. PrivilegedAction<V>
//Mục đích: Dùng trong bảo mật Java (Security). Nó cho phép một đoạn code được thực thi với quyền ưu tiên (privileged) mà không cần kiểm tra quyền của các lớp gọi nó.
//
//Cách hoạt động: Phương thức run() thực thi hành động nhạy cảm và trả về kết quả.
//
//Ứng dụng: Thường gặp trong các hệ thống xử lý file hoặc kết nối mạng ở mức thấp (như khi Ephesoft cần truy cập vào thư mục hệ thống mà người dùng thông thường không có quyền).
