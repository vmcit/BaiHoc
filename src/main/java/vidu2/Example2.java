package vidu2;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@FunctionalInterface
interface FunctionExample<T, R> {
    R apply(T t); // Phương thức trừu tượng chính

    // Bổ sung phương thức static map để xử lý List
    static <T, R> List<R> map(List<T> list, FunctionExample<T, R> f) {
        List<R> result = new ArrayList<>();
        for (T t : list) {
            result.add(f.apply(t)); // Áp dụng hàm f cho từng phần tử
        }

        return result;
    }
}

class Apple {
    private int weight;
    public Apple(int weight) { this.weight = weight; }
    public int getWeight() { return weight; } // Phương thức này sẽ được tham chiếu
}

public class Example2 {
    public static void main(String[] args) {
        List<Apple> inventory = Arrays.asList(new Apple(150), new Apple(200));

        // Sử dụng Method Reference để lấy cân nặng
        // Thay vì: (Apple a) -> a.getWeight()
        List<Integer> weights = FunctionExample.map(inventory, Apple::getWeight);

        System.out.println(weights); // Kết quả: [150, 200]
    }
}
