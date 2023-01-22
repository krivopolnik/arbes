package com.phonecompany.billing;

import java.math.BigDecimal;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class BillCalculatorTest {

    @org.junit.jupiter.api.Test
    void calculate() throws ParseException {
        String phoneLog = "42073711111111, 01-01-2021 09:00:00, 01-01-2021 09:06:00\n" +
                "420777777777, 01-01-2021 16:10:00, 01-01-2021 16:15:00\n" +
                "420777777777, 01-01-2021 16:10:00, 01-01-2021 16:20:00";
        BigDecimal result = new BillCalculator().calculate(phoneLog);
        assertEquals(new BigDecimal("5.20"), result);
    }

}
