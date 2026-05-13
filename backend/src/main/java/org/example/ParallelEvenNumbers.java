package org.example;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParallelEvenNumbers {
    public static void main(String[] args) {
        // Tạo danh sách từ 1 đến 1000
        List<Integer> sourceNumbers = IntStream.rangeClosed(1, 1000)
                .boxed()
                .collect(Collectors.toList());

        // --- TRƯỜNG HỢP 1: Dùng forEach (In trực tiếp - Dễ bị lộn xộn) ---
        System.out.println("--- In trực tiếp bằng Parallel Stream (Thứ tự sẽ loạn xạ) ---");
        sourceNumbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .limit(20) // Chỉ in 20 số đầu tiên tìm được để dễ nhìn
                .forEach(n -> System.out.print(n + " "));

        System.out.println("\n\n--- Thu thập vào List rồi mới in (Thứ tự được bảo toàn) ---");

        // --- TRƯỜNG HỢP 2: Dùng collect (Gom về danh sách - Thứ tự chuẩn) ---
        List<Integer> evenNumbers = sourceNumbers.parallelStream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        // In 20 số đầu tiên trong List kết quả
        for (int i = 0; i < 20; i++) {
            System.out.print(evenNumbers.get(i) + " ");
        }
    }
}
