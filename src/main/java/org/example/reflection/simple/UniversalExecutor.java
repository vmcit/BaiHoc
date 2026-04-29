package org.example.reflection.simple;


import java.lang.reflect.Method;
import java.util.Arrays;

//{
//        "targetService": "AirConditioner",
//        "action": "setTemperature",
//        "params": [24]
//        }

public class UniversalExecutor {

    public Object execute(Object target, String methodName, Object... args) throws Exception {
        // 1. Tự động lấy danh sách kiểu dữ liệu từ tham số truyền vào để tìm Method tương ứng
        Class<?>[] parameterTypes = Arrays.stream(args)
                .map(Object::getClass)
                .toArray(Class[]::new);

        // 2. Tìm đúng method (Kể cả khi Overloading)
        // Đây là chỗ khó: Phải xử lý trường hợp kiểu nguyên thủy (int vs Integer)
        Method method = target.getClass().getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);

        // 3. Thực thi và trả về kết quả (Dù kết quả là String, Integer hay List...)
        return method.invoke(target, args);
    }

    public static void main(String[] args) throws Exception {
        UniversalExecutor executor = new UniversalExecutor();

        // Giả sử có 2 lớp hoàn toàn khác nhau
        InventoryService inv = new InventoryService();
        SalaryService sal = new SalaryService();

        // Gọi hàm của InventoryService: trả về Boolean
        Object res1 = executor.execute(inv, "checkStock", "iPhone 15", 50);
        System.out.println("Kết quả kho: " + res1);

        // Gọi hàm của SalaryService: trả về Double
        Object res2 = executor.execute(sal, "calculateBonus", 5000.0, 0.1);
        System.out.println("Tiền thưởng: " + res2);
    }
}

// Lớp dịch vụ 1
class InventoryService {
    private Boolean checkStock(String item, Integer quantity) {
        return quantity < 100; // Giả lập logic
    }
}

// Lớp dịch vụ 2
class SalaryService {
    public Double calculateBonus(Double salary, Double percent) {
        return salary * percent;
    }
}
