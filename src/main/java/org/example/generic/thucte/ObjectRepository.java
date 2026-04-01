package org.example.generic.thucte;

import java.util.ArrayList;
import java.util.List;

public class ObjectRepository {
    private List<Object> dataList = new ArrayList<>(); // Lưu mọi thứ

    public void save(Object item) {
        dataList.add(item);
        System.out.println("-> Đã lưu: " + item);
        System.out.println("   List hiện tại: " + dataList);
    }

    public Object get(int index) {
        return dataList.get(index);
    }
}
