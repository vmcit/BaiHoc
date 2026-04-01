package org.example;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
 interface Predicate<T> {
    boolean test(T t);
}

public class PredicateExample {

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> results = new ArrayList<>();
        for (T s : list) {
            if (p.test(s)) {
                results.add(s);
            }
        }
        return results;
    }

    public static void main(String[] args) {
        // Ví dụ 1: Lọc danh sách chuỗi (String) không trống
        List<String> listOfStrings = List.of("Hà Nội", "", "Quảng Ninh", " ", "Sài Gòn");

       // Sử dụng Lambda để lọc các chuỗi không rỗng
        Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
        List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);

        System.out.println(nonEmpty); // Kết quả: [Hà Nội, Quảng Ninh,  , Sài Gòn]

        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 10);

      // Lọc các số chia hết cho 2
        List<Integer> evenNumbers = filter(numbers, (Integer i) -> i % 2 == 0);

        System.out.println(evenNumbers); // Kết quả: [2, 4, 6, 10]
        System.out.println(checkAPI());
    }

    public static  boolean checkAPI() {
       return true;
    }
}
