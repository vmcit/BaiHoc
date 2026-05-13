package org.example;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import java.util.ArrayList;
import java.util.List;

public class Java8Example {
    public static void main(String[] args) {
        List<Dish> menu = new ArrayList<>();
        menu.add(new Dish("pork", 800));
        menu.add(new Dish("beef", 700));
        menu.add(new Dish("chicken", 400));
        menu.add(new Dish("french fries", 530));
        menu.add(new Dish("rice", 350));
        menu.add(new Dish("season fruit", 120));
        menu.add(new Dish("apple", 350));
        menu.add(new Dish("lemon", 120));
//        List<String> lowCaloricDishesName = menu.stream()
//                .filter(d -> d.getCalories() < 400) // Chọn món dưới 400 calories
//                .sorted(comparing(Dish::getCalories)) // Sắp xếp theo lượng calories
//                .map(Dish::getName) // Chỉ lấy tên của các món đó
//                .collect(toList()); // Lưu tất cả tên vào một List
//
//        System.out.println(lowCaloricDishesName);


        List<String> lowCaloricDishesName2 =
                menu.parallelStream() // 2 cpu
                        .filter(d -> d.getCalories() < 400)
                        .sorted(comparing(Dish::getCalories))
                        .map(Dish::getName)
                        .collect(toList());
        System.out.println(lowCaloricDishesName2);
    }


}
