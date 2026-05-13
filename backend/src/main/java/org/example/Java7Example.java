package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Dish {
    private String name;
    private int calories;

    public Dish(String name, int calories) {
        this.name = name;
        this.calories = calories;
    }

    public String getName() { return name; }
    public int getCalories() { return calories; }
}

public class Java7Example {
    public static void main(String[] args) {
        List<Dish> menu = new ArrayList<>();
        menu.add(new Dish("pork", 800));
        menu.add(new Dish("beef", 700));
        menu.add(new Dish("chicken", 400));
        menu.add(new Dish("french fries", 530));
        menu.add(new Dish("rice", 350));
        menu.add(new Dish("season fruit", 120));

        // 1. Filter elements using an accumulator
        List<Dish> lowCaloricDishes = new ArrayList<>();
        for (Dish d : menu) {
            if (d.getCalories() < 400) {
                lowCaloricDishes.add(d);
            }
        }

        // 2. Sort the dishes with an anonymous class
        Collections.sort(lowCaloricDishes, new Comparator<Dish>() {
            public int compare(Dish d1, Dish d2) {
                return Integer.compare(d1.getCalories(), d2.getCalories());
            }
        });

        // 3. Process the sorted list to select the names of dishes
        List<String> lowCaloricDishesName = new ArrayList<>();
        for (Dish d : lowCaloricDishes) {
            lowCaloricDishesName.add(d.getName());
        }

        // In kết quả
        System.out.println(lowCaloricDishesName); // [season fruit, rice]
    }
}
