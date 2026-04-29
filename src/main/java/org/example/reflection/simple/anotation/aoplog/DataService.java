package org.example.reflection.simple.anotation.aoplog;

public class DataService implements IDataService{
    @LogExecutionTime(unit = "ms")
    public void processHeavyTask() throws InterruptedException {
        // Giả lập công việc nặng tốn 5 giây
        Thread.sleep(5000);
        System.out.println("Đã xử lý xong dữ liệu!");
    }
    @LogExecutionTime(unit = "ms")
    public void normalTask() {
        System.out.println("Tác vụ bình thường không đo thời gian.");
    }
}
