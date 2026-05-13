package org.example.reflection.simple.anotation;

import org.example.reflection.simple.Effect;
@MyPlugin(name = "Siêu trầm", priority = 10)
public class BassBoostAno implements Effect {
    @Override
    public void apply() {
        System.out.println("Đang xử lý âm thanh Bass...");
    }
}