package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@FunctionalInterface
 interface MyFunction<T, R> {
    R apply(T t);
}

public class FunctionExample {
    // Phương thức map để biến đổi danh sách
    public static <T, R> List<R> map(List<T> list, MyFunction<T, R> f) {
        List<R> result = new ArrayList<>();
        for (T s : list) {
            result.add(f.apply(s));
        }
        return result;
    }
// convert list string to do dai so nguyen
    public static void main(String[] args) {
        List<String> words = Arrays.asList("lambdas", "in", "action");
        // Sử dụng lambda để lấy độ dài chuỗi (String -> Integer)
        List<Integer> lengths = FunctionExample.map(
                words,
                (String s) -> s.length()
    );

        System.out.println(lengths);


        List<Software> apps = Arrays.asList(
                new Software("Server-Alpha", "v1.2"),
                new Software("Server-Beta", "v2.0")
        );

        // Trích xuất danh sách version (Software -> String)
        List<String> versions = FunctionExample.map(apps, (Software s) -> s.getVersion());
        System.out.println(versions);


    }





}

class Software {
    String hostname;
    String version;

    Software(String h, String v) { this.hostname = h; this.version = v; }
    String getVersion() { return version; }
}