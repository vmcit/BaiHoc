package org.example.generic.thucte;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private List<Product> dataList = new ArrayList<>();

    public void save(Product item) {
        dataList.add(item);
        System.out.println("-> Đã lưu Product: " + item);
        System.out.println("   List hiện tại: " + dataList);
    }
}
