package com.example.ahmadaidin.imgfilter;

import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 17/10/2016.
 */

public class OtsuConverter extends BinaryConverter {

    public OtsuConverter(){
        super();
    }

    public void countThreshold(ArrayList<int[]> grayscale, int[] histogram) {
        int threshold = 0;
        int total = grayscale.size() * grayscale.get(0).length;
        float sum = 0;
        for (int t=0 ; t<256 ; t++) sum += t * histogram[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        threshold = 0;

        for (int t=0 ; t<256 ; t++) {
            wB += histogram[t];               // Weight Background
            if (wB == 0) continue;

            wF = total - wB;                 // Weight Foreground
            if (wF == 0) break;

            sumB += (float) (t * histogram[t]);

            float mB = sumB / wB;            // Mean Background
            float mF = (sum - sumB) / wF;    // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float)wB * (float)wF * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }
        super.setThreshold(threshold);
    }
}
