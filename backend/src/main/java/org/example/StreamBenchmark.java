package org.example;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamBenchmark {

    // Hàm kiểm tra số nguyên tố (Tốn CPU)
    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {

        int cores = Runtime.getRuntime().availableProcessors();
        System.out.println("Số nhân CPU khả dụng (Logical Cores): " + cores);
        // Tạo danh sách 1 triệu số từ 1 đến 1.000.000
        List<Integer> numbers = IntStream.rangeClosed(1, 1_000_000)
                .boxed()
                .collect(Collectors.toList());

        // --- THỨ NHẤT: SEQUENTIAL STREAM (TUẦN TỰ) ---
        long startSeq = System.currentTimeMillis();
        long countSeq = numbers.stream()
                .filter(StreamBenchmark::isPrime)
                .count();
        long endSeq = System.currentTimeMillis();
        System.out.println("Sequential: Tìm thấy " + countSeq + " số nguyên tố trong " + (endSeq - startSeq) + "ms");

        // --- THỨ HAI: PARALLEL STREAM (SONG SONG) ---
        long startPar = System.currentTimeMillis();
        long countPar = numbers.parallelStream()
                .filter(StreamBenchmark::isPrime)
                .count();
        long endPar = System.currentTimeMillis();
        System.out.println("Parallel:   Tìm thấy " + countPar + " số nguyên tố trong " + (endPar - startPar) + "ms");
    }
}