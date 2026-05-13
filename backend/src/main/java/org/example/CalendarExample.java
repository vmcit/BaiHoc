package org.example;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalendarExample {

    // Biến thời gian lịch dương, được đánh dấu annotation để có thể chuyển sang lịch âm
    @LunarCalendar(format = "dd/MM/yyyy")
    private LocalDate birthDate;

    public CalendarExample(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    // Method giả lập chuyển đổi từ lịch dương sang lịch âm
    // Trong thực tế, cần thư viện chuyên biệt như lunar-calendar
    public String convertToLunar(LocalDate gregorianDate) {
        // Giả lập chuyển đổi đơn giản (không chính xác)
        // Thực tế cần tính toán dựa trên thuật toán lịch âm
        int lunarYear = gregorianDate.getYear() - 19; // Giả lập lệch 19 năm
        int lunarMonth = gregorianDate.getMonthValue();
        int lunarDay = gregorianDate.getDayOfMonth();

        return String.format("Năm âm: %d, Tháng: %d, Ngày: %d", lunarYear, lunarMonth, lunarDay);
    }

    // Method để xử lý tất cả field có annotation @LunarCalendar
    public void processLunarFields() {
        try {
            Class<?> clazz = this.getClass();
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(LunarCalendar.class)) {
                    field.setAccessible(true);
                    LocalDate date = (LocalDate) field.get(this);
                    if (date != null) {
                        LunarCalendar annotation = field.getAnnotation(LunarCalendar.class);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(annotation.format());
                        System.out.println("Ngày dương: " + date.format(formatter));
                        System.out.println("Chuyển sang lịch âm: " + convertToLunar(date));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method để lấy String lịch âm cho field annotated
    public String getLunarString() {
        if (birthDate != null) {
            return LunarCalendarUtils.toDetailedLunarString(birthDate);
        }
        return "Không có ngày để chuyển đổi";
    }

    public static void main(String[] args) {
        // Tạo instance với ngày sinh dương lịch
        LocalDate birthDate = LocalDate.of(1990, 5, 15);
        CalendarExample example = new CalendarExample(birthDate);

        // Xử lý chuyển đổi
        example.processLunarFields();

        // Sử dụng hàm tiện ích để lấy String nhanh
        System.out.println("\n--- Sử dụng hàm tiện ích ---");
        System.out.println(example.getLunarString());

        // Sử dụng trực tiếp utils
        System.out.println(LunarCalendarUtils.toLunarString(birthDate));
    }
}
