package org.example.generic.thucte;

import java.util.ArrayList;
import java.util.List;

//xây dựng một ứng dụng giống như Shopee hoặc Lazada.
// Trong hệ thống này, bạn có rất nhiều loại dữ liệu khác nhau cần lưu trữ và hiển thị,
// ví dụ: Sản phẩm (Product), Đơn hàng (Order), và Người dùng (User).
// T đại diện cho bất kỳ thực thể nào (Product, Order, User...)
public class BaseRepository<T> {

    // List này sẽ tự biến thành List<Product> hoặc List<Order>
    private List<T> dataList = new ArrayList<>();

    // Lưu vào list
    public void save(T item) {
        dataList.add(item);
        System.out.println("-> Đã lưu thành công: " + item);
        System.out.println("   Dữ liệu hiện tại trong List: " + dataList);
        System.out.println("   Số lượng phần tử: " + dataList.size());
        System.out.println("------------------------------------------------");
    }

    // Hàm tìm kiếm: Trả về chính đối tượng đó nếu nó có trong List
    public T find(T itemToFind) {
        for (T item : dataList) {
            if (item.equals(itemToFind)) {
                return item;
            }
        }
        return null;
    }

    // In toàn bộ để chứng minh list đã được thêm giá trị
    public void printList() {
        System.out.println("--- Dữ liệu trong danh sách ---");
        for (T item : dataList) {
            System.out.println(item);
        }
        System.out.println("-------------------------------");
    }
}