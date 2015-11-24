package com.john.touristvoucherentity;

import java.util.List;

public class TouristVoucher {
    private List<Voucher> vouchers;

    public String toSourceString() {
        StringBuilder result = new StringBuilder();
        for (Voucher voucher : vouchers) {
            result.append(voucher.toSourceString() + "\n");
        }
        return result.toString();
    }
}
