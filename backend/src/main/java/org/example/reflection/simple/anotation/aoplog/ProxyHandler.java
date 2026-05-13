package org.example.reflection.simple.anotation.aoplog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyHandler implements InvocationHandler {
    private final Object target;

    public ProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Tìm method thực sự trong class gốc (vì proxy truyền vào method của interface)
        Method realMethod = target.getClass().getMethod(method.getName(), method.getParameterTypes());

        if (realMethod.isAnnotationPresent(LogExecutionTime.class)) {
            LogExecutionTime annotation = realMethod.getAnnotation(LogExecutionTime.class);

            long start = System.nanoTime();

            // Chạy hàm gốc
            Object result = method.invoke(target, args);

            long end = System.nanoTime();
            long duration = end - start;

            if ("ms".equals(annotation.unit())) {
                System.out.println("[Log] " + method.getName() + " chạy mất: " + (duration / 1_000_000) + " ms");
            } else {
                System.out.println("[Log] " + method.getName() + " chạy mất: " + duration + " ns");
            }

            return result;
        }

        return method.invoke(target, args);
    }
}
