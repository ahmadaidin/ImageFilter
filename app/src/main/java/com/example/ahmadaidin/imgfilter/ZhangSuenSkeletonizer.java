package com.example.ahmadaidin.imgfilter;

import android.graphics.Bitmap;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aidin - 2 on 17/10/2016.
 */

public class ZhangSuenSkeletonizer extends Skeletonizer {
    final static int[][] nbrs = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1},
            {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};

    final static int[][][] nbrGroups = {{{0, 2, 4}, {2, 4, 6}}, {{0, 2, 6},
            {0, 4, 6}}};

    static List<Point> toWhite = new ArrayList<>();

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

    public void skeletonize(){
        ArrayList<int[]> result = new ArrayList<>();
        boolean firstStep = false;
        boolean hasChanged;

        do {
            hasChanged = false;
            firstStep = !firstStep;

            for (int r = 1; r < grayscale.size() - 1; r++) {
                for (int c = 1; c < grayscale.get(0).length - 1; c++) {

                    if (grayscale.get(r)[c] != 255)
                        continue;

                    int nn = numNeighbors(r, c, grayscale);
                    if (nn < 2 || nn > 6)
                        continue;

                    if (numTransitions(r, c, grayscale) != 1)
                        continue;

                    if (!atLeastOneIsWhite(r, c, firstStep ? 0 : 1, grayscale))
                        continue;

                    toWhite.add(new Point(c, r));
                    hasChanged = true;
                }
            }

            for (Point p : toWhite)
                grayscale.get(p.y)[p.x] = 0;
            toWhite.clear();

        } while (firstStep || hasChanged);
    }

    static int numNeighbors(int r, int c, ArrayList<int[]> grayscale) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (grayscale.get(r + nbrs[i][1])[c + nbrs[i][0]] == 255)
                count++;
        return count;
    }

    static int numTransitions(int r, int c, ArrayList<int[]> grayscale) {
        int count = 0;
        for (int i = 0; i < nbrs.length - 1; i++)
            if (grayscale.get(r + nbrs[i][1])[c + nbrs[i][0]] == 0) {
                if (grayscale.get(r + nbrs[i + 1][1])[c + nbrs[i + 1][0]] == 255)
                    count++;
            }
        return count;
    }

    static boolean atLeastOneIsWhite(int r, int c, int step, ArrayList<int[]> grayscale) {
        int count = 0;
        int[][] group = nbrGroups[step];
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < group[i].length; j++) {
                int[] nbr = nbrs[group[i][j]];
                if (grayscale.get(r + nbr[1])[c + nbr[0]] == 0) {
                    count++;
                    break;
                }
            }
        return count > 1;
    }
}
