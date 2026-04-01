package org.example.generic.hoicham;

import java.util.Map;

public class MyKeyValue<K, V> {
    private K key;
    private V value;

    public MyKeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void display() {
        System.out.println("Key [" + key.getClass().getSimpleName() + "]: " + key);
        System.out.println("Value [" + value.getClass().getSimpleName() + "]: " + value);
        System.out.println("----------------------------");
    }
    public static void main(String[] args) {
        Map<String,Integer> data = Map.of("a",1);

        // Ví dụ 1: Key là Integer (Mã số), Value là String (Tên)
        MyKeyValue<Integer, String> student = new MyKeyValue<>(101, "Nguyễn Văn A");
        student.display();

        // Ví dụ 2: Key là String (Mã vùng), Value là Integer (Số điện thoại)
        MyKeyValue<String, Integer> phoneEntry = new MyKeyValue<>("Hà Nội", 123456789);
        phoneEntry.display();

        // Ví dụ 3: Key là String (Mã sản phẩm), Value là Double (Giá tiền)
        MyKeyValue<String, Double> productPrice = new MyKeyValue<>("IPHONE15", 29900.0);
        productPrice.display();
    }
}
