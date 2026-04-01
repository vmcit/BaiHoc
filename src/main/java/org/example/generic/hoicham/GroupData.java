package org.example.generic.hoicham;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupData<K, V> {
    // Một Map chứa Key và giá trị là một Danh sách các đối tượng V
    private Map<K, List<V>> groups = new HashMap<>();

    // Hàm thêm một phần tử vào nhóm tương ứng
    public void addElement(K key, V value) {
        // Nếu Key chưa tồn tại, tạo một List mới
        groups.computeIfAbsent(key, k -> new ArrayList<>()).add(value);

//        List<V> list = groups.get(key);
//
//        if (list == null) {
//            // Nếu null nghĩa là Key này lần đầu tiên xuất hiện
//            System.out.println("-> Nhóm [" + key + "] chưa có, đang tạo mới List...");
//
//            list = new ArrayList<>(); // Tạo mới một danh sách trống
//            groups.put(key, list);    // Đưa danh sách trống này vào Map với Key tương ứng
//        }
//        list.add(value); // Thêm phần tử vào danh sách
        System.out.println("-> Đã thêm thành công vào nhóm [" + key + "]");
    }

    // Hàm lấy ra danh sách phần tử của một nhóm
    public List<V> getGroup(K key) {
        return groups.get(key);
    }

    // In toàn bộ dữ liệu để chứng minh
    public void showAll() {
        System.out.println("\n--- BÁO CÁO TỔNG HỢP DỮ LIỆU ---");
        groups.forEach((key, list) -> {
            System.out.println("Nhóm: " + key);
            for (V item : list) {
                System.out.println("   + " + item);
            }
        });
        System.out.println("--------------------------------\n");


//        // Bước 1: Lấy ra danh sách tất cả các "Tên nhóm" (Key)
//        // Ví dụ: ["Lớp Java", "Lớp Python"]
//        Set<K> tatCaTenNhom = groups.keySet();
//
//        // Bước 2: Duyệt qua từng tên nhóm một
//        for (K tenNhom : tatCaTenNhom) {
//            System.out.println("Nhóm: " + tenNhom);
//
//            // Bước 3: Dùng cái tên đó để "get" danh sách sinh viên tương ứng từ Map
//            List<V> danhSachSinhVien = groups.get(tenNhom);
//
//            // Bước 4: Duyệt qua danh sách sinh viên này để in ra
//            for (V sinhVien : danhSachSinhVien) {
//                System.out.println("   + " + sinhVien);
//            }
//        }
    }
}
