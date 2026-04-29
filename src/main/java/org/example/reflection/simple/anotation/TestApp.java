package org.example.reflection.simple.anotation;

public class TestApp {
    @MyLog("Chào mừng bạn đến với Java!")
    public void hello() {
        System.out.println("Hàm đang chạy...");
    }

    public static void main(String[] args) throws Exception {
        TestApp app = new TestApp();
        MyLogProcessor.process(app);
    }
}
