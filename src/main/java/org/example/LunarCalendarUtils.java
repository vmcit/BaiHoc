package org.example;

import java.time.LocalDate;

/**
 * Thư viện tiện ích tự viết để chuyển đổi lịch dương sang lịch âm
 */
public class LunarCalendarUtils {

    /**
     * Chuyển đổi LocalDate (lịch dương) sang chuỗi String đại diện lịch âm
     * Đây là giả lập đơn giản, trong thực tế cần thuật toán chính xác
     * @param gregorianDate Ngày dương lịch
     * @return Chuỗi String của ngày âm lịch
     */
    public static String toLunarString(LocalDate gregorianDate) {
        if (gregorianDate == null) {
            return "Ngày không hợp lệ";
        }

        // Giả lập chuyển đổi (không chính xác)
        // Thực tế cần tính toán dựa trên lịch âm Việt Nam/Trung Quốc
        int lunarYear = gregorianDate.getYear() - 19; // Lệch khoảng 19 năm
        int lunarMonth = gregorianDate.getMonthValue();
        int lunarDay = gregorianDate.getDayOfMonth();

        // Kiểm tra tháng nhuận (giả lập)
        boolean isLeapMonth = (lunarMonth == 2 && gregorianDate.getDayOfMonth() > 28);

        String leapStr = isLeapMonth ? " nhuận" : "";
        return String.format("%d/%d%s/%d", lunarDay, lunarMonth, leapStr, lunarYear);
    }

    /**
     * Chuyển đổi và trả về String chi tiết hơn
     * @param gregorianDate Ngày dương lịch
     * @return Chuỗi mô tả đầy đủ
     */
    public static String toDetailedLunarString(LocalDate gregorianDate) {
        if (gregorianDate == null) {
            return "Ngày không hợp lệ";
        }

        String lunarStr = toLunarString(gregorianDate);
        return String.format("Ngày dương: %s -> Ngày âm: %s",
                           gregorianDate.toString(), lunarStr);
    }
}
