package org.example.generic;

public class Result<T> {
    private String subject;
    private T value; // Giá trị kết quả có thể là Double, String, hoặc Integer...

    public Result(String subject, T value) {
        this.subject = subject;
        this.value = value;
    }

    public void showInfo() {
        System.out.println("Môn: " + subject + " | Kết quả: " + value +
                " (Kiểu dữ liệu: " + value.getClass().getSimpleName() + ")");
    }

    public static void main(String[] args) {
        // Hệ thống điểm 10 (Dùng Double)
        Result<Double> mathResult = new Result<>("Toán học", 9.5);
        mathResult.showInfo();

        // Hệ thống điểm chữ (Dùng String)
        Result<String> englishResult = new Result<>("Tiếng Anh", "A+");
        englishResult.showInfo();

        // Hệ thống đạt/không đạt (Dùng Boolean)
        Result<Boolean> physicalEd = new Result<>("Thể dục", true);
        physicalEd.showInfo();
    }
}
