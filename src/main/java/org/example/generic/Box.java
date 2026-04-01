package org.example.generic;

public class Box<T> {
    private T content;

    public void setContent(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }
        public static void main(String[] args) {
            // 1. Hộp đựng số nguyên (Integer)
            Box<Integer> integerBox = new Box<>();
            integerBox.setContent(100);
            System.out.println("Số trong hộp: " + integerBox.getContent());

            // 2. Hộp đựng chuỗi (String)
            Box<String> stringBox = new Box<>();
            stringBox.setContent("Chào Java Generic");
            System.out.println("Chuỗi trong hộp: " + stringBox.getContent());
    }
}
