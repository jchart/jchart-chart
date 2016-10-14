package com.jchart.model.indicator;

import java.util.Date;

import com.jchart.model.Kjchart;


public class Histogram extends DateValue {

    Histogram(Date[] dt1, float[] val1, Date[] dt2, float[] val2,
        int qListSize) {
        super(qListSize);
        calcHistogram(dt1, val1, dt2, val2, qListSize);
    }

    private void calcHistogram(Date[] dt1, float[] val1, Date[] dt2,
        float[] val2, int qListSize) {
        for (int i = 0; i < qListSize; i++) {
            dt[i] = dt1[i];
            if (val2[i] == Kjchart.IFILLER) {
                val[i] = Kjchart.IFILLER;
            } else {
                val[i] = val1[i] - val2[i];
            }
        }
    }
}
