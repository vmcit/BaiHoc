package org.example;
import java.util.function.Function;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        // Hàm f: cộng thêm 1
        Function<Integer, Integer> f = x -> x + 1;

        // Hàm g: nhân với 2
        Function<Integer, Integer> g = x -> x * 2;

        // Kết hợp f và g: thực hiện f trước, sau đó lấy kết quả truyền vào g
        // h(x) = g(f(x)) = (x + 1) * 2
        Function<Integer, Integer> h = f.andThen(g);

        // Áp dụng hàm với giá trị đầu vào là 1
        // Kết quả: (1 + 1) * 2 = 4
        int result = h.apply(1);

        System.out.println("Result: " + result); // Sẽ in ra 4
    }
}