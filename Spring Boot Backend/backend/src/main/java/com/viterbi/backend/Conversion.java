// this class contains some useful static methods for transposing a
// matrix and other things like that.
// Olivier Swedor, EPFL, summer '98.
package com.viterbi.backend;

public class Conversion {

    // converts a decimal integer into a BinaryVect representing
    // its binary value.
    public static BinaryVect decimal2bin(int dec) {

        int biggest, k;
        BinaryVect v;

        if (dec > 0) {
            biggest = (int) (java.lang.Math.log(dec) / java.lang.Math.log(2));
            v = new BinaryVect(biggest + 1);
            for (int i = biggest; i >= 0; i--) {
                k = (int) (java.lang.Math.pow(2, i));
                if ((int) (dec / k) >= 1) {
                    v.set(biggest - i, 1);
                    dec = dec - k;
                } else {
                    v.set(biggest - i, 0);
                }
            }
            return v;
        } else {
            return new BinaryVect(1);
        }
    }

    // returns the transpose of a matrix.
    public static int[][] transpose(int[][] matrix) {

        int n, k;
        int resultArray[][];

        n = matrix.length;
        k = matrix[0].length;
        resultArray = new int[k][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                resultArray[j][i] = matrix[i][j];
            }
        }
        return resultArray;
    }

    public static int[][] stringTo2DIntArray(String data) {

        String[] pairs = data.split(",");

        int[] firstNumbers = new int[pairs.length];
        int[] secondNumbers = new int[pairs.length];

        for (int i = 0; i < pairs.length; i++) {
            String pair = pairs[i];
            firstNumbers[i] = Character.getNumericValue(pair.charAt(0));
            secondNumbers[i] = Character.getNumericValue(pair.charAt(1));
        }

        return new int[][]{firstNumbers, secondNumbers};

    }

    // converts 2D array into string with a format of XX,XX,XX...XX,
    public static String format2DArrayToString(int[][] array) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < array[0].length; i++) {
            sb.append(array[0][i]).append(array[1][i]);

            if (i < array[0].length - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }
}
