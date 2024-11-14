// this class is useful to get the state diagram corresponding to a // given impulse response or generator polynomial.
// Olivier Swedor, EPFL, summer '98.
package com.viterbi.backend;

import java.lang.*;

public class StateTable {

    // nextStates0[] is an array giving the next state if we take 
    // the "zero branch", for each state.
    // nextStates1[] is the same for the "one branch".
    // output0[][] is a matrix containing the output bits for the 
    // "zero branch".
    // output1[][] is the same for the "one branch".
    int nextStates0[], nextStates1[], output0[][], output1[][];

    public StateTable(int impResp[][]) {

        int n, k, m, nbOfStates, nbOfCol;
        int result[][];
        BinaryVect input, state, tempState;

        n = impResp.length;
        k = impResp[0].length;
        m = k - 1;
        nbOfStates = (int) java.lang.Math.pow(2, m);
        nextStates0 = new int[nbOfStates];
        nextStates1 = new int[nbOfStates];
        output0 = new int[nbOfStates][n];
        output1 = new int[nbOfStates][n];
        input = new BinaryVect(nbOfStates);
        tempState = new BinaryVect(nbOfStates);

        for (int i = 0; i < nbOfStates; i++) {
            state = Conversion.decimal2bin(i);
            while (state.length() < (java.lang.Math.log(nbOfStates) / java.lang.Math.log(2))) {
                state.addAtBeginning(0);
            }

            // ZERO
            input.set(state.array());
            input.add(0);
            result = input.convCode(impResp);
            nbOfCol = result[0].length;
            for (int j = 0; j < n; j++) {
                output0[i][j] = result[j][nbOfCol - m - 1];
            }
            tempState.set(state.array());
            tempState.dropFirstCol();
            tempState.add(0);
            nextStates0[i] = tempState.binary2dec();

            // ONE
            input.set(state.array);
            input.add(1);
            result = input.convCode(impResp);
            nbOfCol = result[0].length;
            for (int j = 0; j < n; j++) {
                output1[i][j] = result[j][nbOfCol - m - 1];
            }
            tempState.set(state.array());
            tempState.dropFirstCol();
            tempState.add(1);
            nextStates1[i] = tempState.binary2dec();
        }
    }

    // useful for debugging or testing:
    public void display() {

        System.err.println(" ");
        System.err.println("stateTable.display()");
        System.err.println(" ");
        System.err.println("next0:");

        for (int i = 0; i < (this.nextStates0).length; i++) {
            System.err.print(this.nextStates0[i]);
        }

        System.err.println(" ");
        System.err.println("next1:");
        for (int i = 0; i < (this.nextStates1).length; i++) {
            System.err.print(this.nextStates1[i]);
        }
        System.err.println(" ");
        System.err.println("output0:");
        for (int i = 0; i < (this.output0).length; i++) {
            for (int j = 0; j < (this.output0[1]).length; j++) {
                System.err.print(this.output0[i][j]);
            }
            System.err.println(" ");
        }

        System.err.println(" ");
        System.err.println("output1:");
        for (int i = 0; i < (this.output1).length; i++) {
            for (int j = 0; j < (this.output1[1]).length; j++) {
                System.err.print(this.output1[i][j]);
            }
            System.err.println(" ");
        }
    }

    public int[] nextStates0() {
        return this.nextStates0;
    }

    public int[] nextStates1() {
        return this.nextStates1;
    }

    public int[][] output0() {
        return this.output0;
    }

    public int[][] output1() {
        return this.output1;
    }

}
