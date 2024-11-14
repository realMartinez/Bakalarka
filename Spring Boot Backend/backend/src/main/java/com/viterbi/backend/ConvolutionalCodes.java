// Prebraté z appletu
package com.viterbi.backend;

public class ConvolutionalCodes {

    public static BinaryVect getMsg(String message) {

        Integer c;
        int n, intMsg[];
        BinaryVect vect;
        Character ch;

        n = message.length();
        intMsg = new int[n];
        vect = new BinaryVect(n);
        for (int z = 0; z < n; z++) {
            ch = new Character((message).toCharArray()[z]);
            c = new Integer(ch.toString());
            intMsg[z] = c.intValue();
        }
        vect.set(intMsg);
        return vect;
    }

    public static int[][] getImpRsp(String polynomialString) {

        int imp[][];
        Integer c;
        int cols, rows, n, intMsg[], i;
        Character ch;
        String s;

        cols = 0;
        rows = 0;
        n = (polynomialString).length();
        ch = new Character((polynomialString).toCharArray()[n - 1]);
        s = ch.toString();
        if (s.compareTo(";") == 0) {
            for (int z = 0; z < n; z++) {
                ch = new Character((polynomialString).toCharArray()[z]);
                s = ch.toString();
                if (s.compareTo(";") == 0) {
                    rows++;
                } else {
                    cols++;
                }
            }
            cols = cols / rows;

            i = 0;
            imp = new int[rows][cols];
            for (int y = 0; y < rows; y++) {
                for (int z = 0; z < cols; z++) {
                    ch = new Character((polynomialString).toCharArray()[i]);
                    if ((ch.toString()).compareTo(";") == 0) {
                        i++;
                        ch = new Character((polynomialString).toCharArray()[i]);
                    }
                    c = new Integer(ch.toString());
                    imp[y][z] = c.intValue();
                    i++;
                }
            }
            return imp;
        } else {
            return new int[0][0];
        }
    }

    public static int[][] getChannel(int[][] codedMessage) {

        int[][] channel = new int[codedMessage.length][codedMessage[0].length];
        for (int z = 0; z < codedMessage.length; z++) {
            for (int y = 0; y < codedMessage[0].length; y++) {
                channel[z][y] = 0;
            }
        }
        return channel;
    }

    public static int[][] getReceivedMessage(int[][] codedMessage, int[][] channel) {

        int[][] received = new int[codedMessage.length][codedMessage[0].length];
        for (int z = 0; z < codedMessage.length; z++) {
            System.arraycopy(codedMessage[z], 0, received[z], 0, codedMessage[z].length);
        }

        for (int y = 0; y < received.length; y++) {
            for (int z = 0; z < received[0].length; z++) {
                received[y][z] = (received[y][z] + channel[y][z]) % 2;
            }
        }

        return received;
    }
}
