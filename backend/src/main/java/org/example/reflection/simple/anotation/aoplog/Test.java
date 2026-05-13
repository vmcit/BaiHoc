package org.example.reflection.simple.anotation.aoplog;

import java.lang.reflect.Proxy;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        IDataService service = new DataService();

        // Tạo Proxy cho service
        IDataService proxyService = (IDataService) Proxy.newProxyInstance(
                IDataService.class.getClassLoader(),
                new Class[]{IDataService.class},
                new ProxyHandler(service)
        );

        proxyService.processHeavyTask(); // Sẽ tự động in thêm log thời gian
        proxyService.normalTask();      // Chạy bình thường
    }
}
