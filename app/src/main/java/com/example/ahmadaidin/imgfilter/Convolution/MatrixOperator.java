package com.example.ahmadaidin.imgfilter.Convolution;

import java.util.ArrayList;

/**
 * Created by Aidin - 2 on 26/09/2016.
 */

public class MatrixOperator {

    public MatrixOperator() {

    }

    public static ArrayList<ArrayList<Integer>> rotateLeft(ArrayList<ArrayList<Integer>> mtx) {
        ArrayList<ArrayList<Integer>> matrix = copyMatrix(mtx);
        int size = matrix.size();
        // Consider all squares one by one
        for (int x = 0; x < size / 2; x++)
        {
            // Consider elements in group of 4 in
            // current square
            for (int y = x; y < size-x-1; y++)
            {
                // store current cell in temp variable
                int temp = matrix.get(x).get(y);

                // move values from right to top
                ArrayList<Integer> row1 = matrix.get(x);
                row1.set(y,matrix.get(y).get(size-1-x));
                matrix.set(x,row1);

                // move values from bottom to right
                ArrayList<Integer> row2 = matrix.get(y);
                row2.set(size-1-x,matrix.get(size-1-x).get(y));
                matrix.set(y,row2);

                // move values from left to bottom
                ArrayList<Integer> row3 = matrix.get(size-1-x);
                row3.set(size-1-y,matrix.get(size-1-y).get(x));
                matrix.set(size-1-x,row3);

                // assign temp to left
                ArrayList<Integer> row4 = matrix.get(size-1-y);
                row4.set(x,temp);
                matrix.set(size-1-y,row4);
            }
        }
        return matrix;
    }

    public static ArrayList<ArrayList<Integer>> rotate1Right(ArrayList<ArrayList<Integer>> matrix) {
        ArrayList<ArrayList<Integer>> mat = copyMatrix(matrix);
        int m = mat.size();
        int n = m;

        int row = 0, col = 0;
        int prev, curr;

    /*
       row - Staring row index
       m - ending row index
       col - starting column index
       n - ending column index
       i - iterator
    */
        while (row < m && col < n)
        {

            if (row + 1 == m || col + 1 == n)
                break;

            // Store the first element of next row, this
            // element will replace first element of current
            // row
            prev = mat.get(row+1).get(col);

         /* Move elements of first row from the remaining rows */
            for (int i = col; i < n; i++)
            {
                curr = mat.get(row).get(i);
                ArrayList<Integer> row_n = mat.get(row);
                row_n.set(i,prev);
                mat.set(row,row_n);
                prev = curr;
            }
            row++;

        /* Move elements of last column from the remaining columns */
            for (int i = row; i < m; i++)
            {
                curr = mat.get(i).get(n-1);
                ArrayList<Integer> row_n = mat.get(i);
                row_n.set(n-1,prev);
                mat.set(i,row_n);
                prev = curr;
            }
            n--;

         /* Move elements of last row from the remaining rows */
            if (row < m)
            {
                for (int i = n-1; i >= col; i--)
                {
                    curr = mat.get(m-1).get(i);
                    ArrayList<Integer> row_n = mat.get(m-1);
                    row_n.set(i,prev);
                    mat.set(m-1,row_n);
                    prev = curr;
                }
            }
            m--;

        /* Move elements of first column from the remaining rows */
            if (col < n)
            {
                for (int i = m-1; i >= row; i--)
                {
                    curr = mat.get(i).get(col);
                    ArrayList<Integer> row_n = mat.get(i);
                    row_n.set(col,prev);
                    mat.set(i,row_n);
                    prev = curr;
                }
            }
            col++;
        }
        return mat;
    }

    private static ArrayList<ArrayList<Integer>> copyMatrix(ArrayList<ArrayList<Integer>> mtx) {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        for (int i = 0; i<mtx.size(); i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j<mtx.get(i).size();j++) {
                row.add(Integer.valueOf(mtx.get(i).get(j)));
            }
            matrix.add(row);
        }
        return matrix;
    }

}
