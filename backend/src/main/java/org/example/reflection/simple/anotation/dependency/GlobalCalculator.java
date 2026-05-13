package org.example.reflection.simple.anotation.dependency;

public class GlobalCalculator {
    private static GlobalCalculator instance;
    public int total = 0;

    private GlobalCalculator() {
    } // Chặn không cho 'new'

    public static GlobalCalculator getInstance() {
        if (instance == null) instance = new GlobalCalculator();
        return instance;
    }

    public void add(int value) {
        total += value;
    }

    public static void main(String[] args) {
        // Người thứ nhất lấy máy tính ra cộng 5
        GlobalCalculator cal1 = GlobalCalculator.getInstance();
        cal1.add(5);

        // Người thứ hai (ở một chỗ rất xa trong code) lấy máy tính ra cộng 10
        GlobalCalculator cal2 = GlobalCalculator.getInstance();
        cal2.add(10);

        // Kết quả cuối cùng
        System.out.println("Tổng chung: " + cal2.total); // Kết quả: 15 (Chính xác!)
    }
}

