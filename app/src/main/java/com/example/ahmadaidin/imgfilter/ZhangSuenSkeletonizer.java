package com.example.ahmadaidin.imgfilter;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aidin - 2 on 17/10/2016.
 */

public class ZhangSuenSkeletonizer extends Skeletonizer {

    public ZhangSuenSkeletonizer(){
        super();
    }

    public ZhangSuenSkeletonizer(ArrayList<int[]> grayscale) {
        super(grayscale);
    }

    public void setGrayscale(ArrayList<int[]> grayscale){
        super.setGrayscale(grayscale);
    }

    public ArrayList<int[]> getGrayscale() {
        return super.getGrayscale();
    }

    final static int[][] nbrs = {
            {0, -1}/*2*/, {1, -1}/*3*/, {1, 0}/*4*/,
            {1, 1}/*5*/, {0, 1}/*6*/, {-1, 1}/*7*/,
            {-1, 0}/*8*/, {-1, -1}/*9*/, {0, -1}/*2*/
    };

    final static int[][][] nbrGroups = {
            {
                    {0/*2*/, 2/*4*/, 4/*6*/},
                    {2/*4*/, 4/*6*/, 6/*8*/}
            },
            {
                    {0/*2*/, 2/*4*/, 6/*8*/},
                    {0/*2*/, 4/*6*/, 6/*8*/}
            }
    };

    static List<Point> toWhite = new ArrayList<>();
    static int background = 0;
    static int foreground = 255;

    public void skeletonize() {
        boolean firstStep = false;
        boolean hasChanged;

        do {
            hasChanged = false;
            firstStep = !firstStep;

            for (int r = 1; r < grayscale.size() - 1; r++) {
                for (int c = 1; c < grayscale.get(0).length - 1; c++) {

                    if (grayscale.get(r)[c] != foreground)
                        continue;

                    int nn = numNeighbors(r, c);
                    if (nn < 2 || nn > 6)
                        continue;

                    if (numTransitions(r, c) != 1)
                        continue;

                    if (!atLeastOneIsWhite(r, c, firstStep ? 0 : 1))
                        continue;

                    toWhite.add(new Point(c, r));
                    hasChanged = true;
                }
            }

            for (Point p : toWhite)
                grayscale.get(p.y)[p.x] = background;
            toWhite.clear();

        } while (hasChanged);
    }

    public int numNeighbors(int r, int c) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (grayscale.get(r + nbrs[i][1])[c + nbrs[i][0]] == foreground)
                count++;
        return count;
    }

    public int numTransitions(int r, int c) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (grayscale.get(r + nbrs[i][1])[c + nbrs[i][0]] == background) {
                if (grayscale.get(r + nbrs[i + 1][1])[c + nbrs[i + 1][0]] == foreground)
                    count++;
            }
        return count;
    }

    public boolean atLeastOneIsWhite(int r, int c, int step) {
        int count = 0;
        int[][] group = nbrGroups[step];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < group[i].length; j++) {
                int[] nbr = nbrs[group[i][j]];
                if (grayscale.get(r + nbr[1])[c + nbr[0]] == background ) {
                    count++;
                    break;
                }
            }
        return count > 1;
    }
}
