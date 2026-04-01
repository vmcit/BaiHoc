package org.example.generic.thucte;

import java.util.ArrayList;
import java.util.List;

class OrderRepository {
    private List<Order> dataList = new ArrayList<>();

    public void save(Order item) {
        dataList.add(item);
        System.out.println("-> Đã lưu Order: " + item);
        System.out.println("   List hiện tại: " + dataList);
    }
}
