package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


interface ApplePredicate {
    boolean test(Apple apple);
}

// 2. Tạo một lớp cụ thể để lọc táo xanh
 class GreenApplePredicate implements ApplePredicate {
    public boolean test(Apple apple) {
        return "green".equals(apple.getColor());
    }
}



public class Test {

    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple : inventory) {
            if ("green".equals(apple.getColor())) {
                result.add(apple);
            }
        }
        return result;
    }

    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
        return inventory.stream()
                .filter((Predicate<? super Apple>) p) // Lambda truyền vào đây
                .collect(Collectors.toList());
    }

    // Cách gọi hàm:

    public static void main(String[] args) {



        List<Apple> inventory = new ArrayList<>();

        // Thêm dữ liệu mẫu
        inventory.add(new Apple("green", 120));
        inventory.add(new Apple("red", 150));
        inventory.add(new Apple("green", 170));
        inventory.add(new Apple("yellow", 100));

        // Bây giờ bạn có thể gọi hàm filter đã viết ở bước trước
        // Ví dụ lọc táo xanh:
        List<Apple> greenApples = filterApples(inventory, a -> "green".equals(a.getColor()));

        System.out.println("Số lượng táo xanh: " + greenApples.size());
        List<Apple> heavyApples = filterApples(inventory, a -> a.getWeight() > 150);


        // 3. Sử dụng
        filterApples(inventory, (ApplePredicate) new GreenApplePredicate());

        filterApples(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return "green".equals(apple.getColor());
            }
        });

        // Logic: (tham số) -> { trả về kết quả }
        filterApples(inventory, (Apple a) -> {
            return "green".equals(a.getColor());
        });

        // Lambda gọn nhất:
        filterApples(inventory, a -> "green".equals(a.getColor()));

    }
}
