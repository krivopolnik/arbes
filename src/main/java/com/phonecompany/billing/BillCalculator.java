package com.phonecompany.billing;



import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class BillCalculator implements TelephoneBillCalculator {

    @Override
    public BigDecimal calculate(String phoneLog) throws ParseException {


        String[] calls = phoneLog.split("\n");
        Map<String, Integer> callsPerPhone = new HashMap<>();
        Map<String, BigDecimal> numberAndAmountOFCalls = new HashMap<>();

        for(int i = 0; i < calls.length; i++) {
            String[] callInfo = calls[i].split(",");
            int countOfCallsToNumber = 0;
            if (callsPerPhone.get(callInfo[0]) != null) {
                countOfCallsToNumber = callsPerPhone.get(callInfo[0]);
            }
            callsPerPhone.put(callInfo[0], countOfCallsToNumber + 1);


            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date startConversation = sdf.parse(callInfo[1]);
            Date endConversation = sdf.parse(callInfo[2]);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startConversation); //for add 1 minute in loop

            long diff = endConversation.getTime() - startConversation.getTime();
            float seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
            float minutes = seconds / 60;
            float callDuration = (float) Math.ceil(minutes); // to more, i.e. 121 s = 3 min


            double priceForCall = 0;

            for (int j = 0; j < callDuration; j++) {

                if (j > 4) {
                    priceForCall += 0.2;
                } else {
                    String timeStartConversation = sdf.format(calendar.getTime());
                    Date convertedStartConversation = sdf.parse(timeStartConversation);
                    int conversationStarts = convertedStartConversation.getHours();


                    if (!(conversationStarts < 8 || conversationStarts > 15)) {
                        priceForCall += 1;
                    } else {
                        priceForCall += 0.5;
                    }

                }

                calendar.add(Calendar.MINUTE, 1);

            }
            BigDecimal resultPriceForCall = new BigDecimal(priceForCall);
            resultPriceForCall = resultPriceForCall.setScale(2, RoundingMode.HALF_UP);

            if (numberAndAmountOFCalls.get(callInfo[0]) != null) {
                numberAndAmountOFCalls.put(callInfo[0], numberAndAmountOFCalls.get(callInfo[0]).add(resultPriceForCall));
            } else {
                numberAndAmountOFCalls.put(callInfo[0], resultPriceForCall);
            }

        }
        String mostCalled = Collections.max(callsPerPhone.entrySet(), Map.Entry.comparingByValue()).getKey();
        numberAndAmountOFCalls.remove(mostCalled);


        BigDecimal sum = numberAndAmountOFCalls.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum;
    }

}