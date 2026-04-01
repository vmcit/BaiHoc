package org.example;

import java.util.Arrays;
import java.util.List;

public class ParallelOrderExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        System.out.println("--- Chạy với Stream tuần tự (Thứ tự chuẩn) ---");
        numbers.stream()
                .forEach(n -> System.out.print(n + " "));

        System.out.println("\n\n--- Chạy với Parallel Stream (Lộn xộn) ---");
        numbers.parallelStream()
                .forEach(n -> System.out.print(n + " "));

        System.out.println("\n\n(Chạy lại Parallel lần nữa để thấy sự khác biệt)");
        numbers.parallelStream()
                .forEach(n -> System.out.print(n + " "));
    }
}
