package com.example.rentconnect.utils;

import java.util.Locale;

public class VietQRHelper {
    public static String generateQrPayload(int invoiceId, double amount, String bankAccount) {
        return String.format(Locale.US, "VIETQR|RentConnect|INV%04d|AMT:%.0f|ACC:%s", invoiceId, amount, bankAccount);
    }
}
