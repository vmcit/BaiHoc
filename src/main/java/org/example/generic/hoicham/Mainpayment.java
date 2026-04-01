package org.example.generic.hoicham;

import java.util.ArrayList;
import java.util.List;

public class Mainpayment {
    // Hàm này chấp nhận List của Payment hoặc bất kỳ lớp con nào của nó
    public static void processAllPayments(List<? extends Payment> payments) {
        for (Payment p : payments) {
            p.process(); // Chắc chắn gọi được hàm của lớp cha Payment
        }
    }

    public static void printInvoices(List<Payment> list) {
        for (Payment p : list) {
            // 1. Kiểm tra nếu là Visa
            if (p instanceof VisaPayment) {
                VisaPayment visa = (VisaPayment) p; // Ép kiểu xuống Visa
                System.out.println("Đây là thẻ Visa. Đang check hạn mức...");
                visa.process();
            }
            // 2. Kiểm tra nếu là Momo
            else if (p instanceof MomoPayment) {
                MomoPayment momo = (MomoPayment) p; // Ép kiểu xuống Momo
                System.out.println("Đây là ví Momo. Đang check số dư điện thoại...");
                momo.process();
            }
            // 3. Trường hợp còn lại (Payment chung)
            else {
                p.process();
            }
        }
    }

    public static void main(String[] args) {
        List<Payment> paymentList = new ArrayList<>();
        paymentList.add(new MomoPayment(500));
        System.out.println("--- Xử lý Visa paymentList ---");
        processAllPayments(paymentList);
        // Danh sách các thẻ Visa
        List<VisaPayment> visaList = new ArrayList<>();
        visaList.add(new VisaPayment(1000));
        visaList.add(new VisaPayment(2000));

        // Danh sách các ví Momo
        List<MomoPayment> momoList = new ArrayList<>();
        momoList.add(new MomoPayment(500));

        // Nhờ có <? extends Payment>, ta dùng chung một hàm cho cả 2 loại list khác nhau
        System.out.println("--- Xử lý Visa ---");
        processAllPayments(visaList);

        System.out.println("\n--- Xử lý Momo ---");
        processAllPayments(momoList);
    }
}
