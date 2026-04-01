package org.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@FunctionalInterface
interface Calculation {
    int operation(int x, int y);
}
class nhan implements Calculation {

    @Override
    public int operation(int x, int y) {
        return x*y;
    }
}
// tru , chia .


public class Program2 {
    public static void main(String[] args) {
        int a = 10, b = 2;
        //sử dụng biểu thức lambda để thực thi operation tính tổng 2 số
        Calculation cal1 = (x, y) -> {
            return x + y;
        }; // hoặc (x,y)->x+y;
        int tong = cal1.operation(a, b);
        System.out.printf("Tổng %d+%d=%d\n", a, b, tong);
        //sử dụng biểu thức lambda để thực thi operation tính tích 2 số
        Calculation cal2 = (x, y) -> {
            return x * y;
        };
        int tich = cal2.operation(a, b);
        System.out.printf("Tích %d+%d=%d\n", a, b, tich);
        //sử dụng biểu thức lambda để thực thi operation tính hiệu 2 số
        Calculation cal3 = (x, y) -> {
            return x - y;
        };
        int hieu = cal3.operation(a, b);
        System.out.printf("Hiệu %d+%d=%d\n", a, b, hieu);
        //sử dụng biểu thức lambda để thực thi operation tính thương 2 số
        Calculation cal4 = (x, y) -> {
            return x / y;
        };
        int thuong = cal4.operation(a, b);
        System.out.printf("Thương %d+%d=%d\n", a, b, thuong);


        List<String> names = Arrays.asList("Nam", "An", "Nguyen", "Binh");

        // tự tìm hiểu cách viết 1 class vô danh riêng để sắp xếp 1 list

        // sắp xếp sort ....

        // Dùng Anonymous Inner Class
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return Integer.compare(s1.length(), s2.length());
            }
        });

        System.out.println(names);


        // Dùng Lambda Expression
        Collections.sort(names, (s1, s2) -> Integer.compare(s1.length(), s2.length()));

        System.out.println(names);
    }
}