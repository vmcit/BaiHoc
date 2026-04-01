package org.example.generic;

public class GenericMethodExample{
    // Khai báo <E> trước kiểu trả về để biến đây thành hàm Generic
    public static <E> void displayArray(E[] elements) {
        for (E element : elements) {
            System.out.print(element + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Integer[] intArray = {1, 2, 3, 4, 5};
        String[] strArray = {"A", "B", "C"};

        System.out.print("Mảng số: ");
        displayArray(intArray); // Truyền mảng Integer

        System.out.print("Mảng chữ: ");
        displayArray(strArray); // Truyền mảng String
    }
}
