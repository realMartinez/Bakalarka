// a VitDecoder is an object able to decode a sequence of bits
// that was encoded by a convolutional encoder, using the 
// maximum likelihood procedure.
// Olivier Swedor, EPFL, summer '98.
package com.viterbi.backend;

import java.lang.*;

public class VitDecoder {

    final int maxLength = 100;
    final int inf = 32000;

    int nbOfStates, n, k, l, m, step, state, pathIndex, codedIndex;
    int nextMetric0[], nextMetric1[], zeros[];
    // state table description
    int nextState0[], nextState1[], output0[][], output1[][];

    int path1[][], metric0[][], metric1[][], currentMetric[], path[][];
    int coded[][], impResp[][];

    // creates a VitDecoder to decode "codedSeq" according to
    // the impulse response (or generator poly.) "impResp".
    public VitDecoder(int impResp[][], int[][] codedSeq) {

        StateTable stateT;
        int nextMetric0[], nextMetric1[];

        // initialization
        coded = Conversion.transpose(codedSeq);
        n = impResp.length;
        k = impResp[0].length;
        m = k - 1;
        nbOfStates = (int) java.lang.Math.pow(2, m);
        l = coded.length;

        path1 = new int[nbOfStates][maxLength];
        path = new int[nbOfStates][maxLength];
        metric0 = new int[maxLength][nbOfStates];
        metric1 = new int[maxLength][nbOfStates];
        zeros = new int[n];
        for (int i = 0; i < n; i++) {
            zeros[i] = 0;
        }

        currentMetric = new int[nbOfStates];
        for (int i = 0; i < nbOfStates; i++) {
            currentMetric[i] = inf;
        }
        currentMetric[0] = 0;
        step = 0; // step 0 of the algorithm
        codedIndex = 0;
        pathIndex = 0;
        state = 0;

        // state table creation
        stateT = new StateTable(impResp);
        nextState0 = stateT.nextStates0();
        nextState1 = stateT.nextStates1();
        output0 = stateT.output0();
        output1 = stateT.output1();
    }

    // returns the matrix representing the paths.
    public int[][] path() {
        return this.path1;
    }

    // returns the array representing the current metrics.
    public int[] currentMetric() {
        return this.currentMetric;
    }

    public void nextStep(int inputBits[]) {
        // if the first entry of the array "inputBits" is bigger 
        // than 1 the this function gets the input bits in  the 
        // instance variable "coded". If not, it uses "inputBits" 
        // as input bits.

        int previous, minMetric, decodedBit;
        BinaryVect currentOutput, currentCodedBits;

        // the compiler wants these variables to be intialized
        decodedBit = 999;
        previous = 999;

        // computing the metrics to reach the next states
        // it is only done if we are in state 0, because it 
        //doesn't change for the other states, it only changes 
        //when the step changes.
        if (state == 0) {
            nextMetric0 = new int[nbOfStates];
            nextMetric1 = new int[nbOfStates];
            currentOutput = new BinaryVect(nbOfStates);
            currentCodedBits = new BinaryVect(n);
            if (inputBits[0] > 1) {
                if (codedIndex < l) {
                    currentCodedBits.set(coded[codedIndex]);
                } else {
                    currentCodedBits.set(zeros);
                }
            } else {
                currentCodedBits.set(inputBits);
            }
            for (int j = 0; j < nbOfStates; j++) {
                // zero
                currentOutput.set(output0[j]);
                metric0[step][j] = currentMetric[j] + currentOutput.hammingDist(currentCodedBits);
                nextMetric0[j] = currentMetric[j] + currentOutput.hammingDist(currentCodedBits);
                // one
                currentOutput.set(output1[j]);
                metric1[step][j] = currentMetric[j] + currentOutput.hammingDist(currentCodedBits);
                nextMetric1[j] = currentMetric[j] + currentOutput.hammingDist(currentCodedBits);
            }
        }

        // making a choice between the two paths entering each state
        // the variable "state" represents the state for wich we are
        // currently determining the best path.
        if (state < nbOfStates) {
            minMetric = inf;
            previous = inf;
            for (int a = 0; a < nbOfStates; a++) {
                if (nextState0[a] == state) {
                    if (nextMetric0[a] < minMetric) {
                        minMetric = nextMetric0[a];
                        previous = a;
                        decodedBit = 0;
                    }
                }
                if (nextState1[a] == state) {
                    if (nextMetric1[a] < minMetric) {
                        minMetric = nextMetric1[a];
                        previous = a;
                        decodedBit = 1;
                    }
                }
            }

            // updating the path1 matrix according to the selected 
            // branch, wich can be the "zero-branch" or the 
            // "one-branch".
            if ((decodedBit == 0) && (previous < inf)) {
                for (int z = 0; z < path1[state].length; z++) {
                    path1[state][z] = path[previous][z];
                }
                path1[state][step] = previous;
                currentMetric[state] = nextMetric0[previous];
            } else {
                if (previous < inf) {
                    for (int z = 0; z < path1[state].length; z++) {
                        path1[state][z] = path[previous][z];
                    }
                    path1[state][step] = previous;
                    currentMetric[state] = nextMetric1[previous];
                }
            }
            // we increase the variable state by one in order to 
            // do the same things for the next state:
            state++;
        } // if the variable "state" is equal to "nbOfStates", 
        // it means that we have achieved the computing of each 
        // state's best branch, and so we have to do the same 
        // computings again for the next step.
        else {
            state = 0;
            // updating "path" with "path1"
            for (int z = 0; z < path.length; z++) {
                System.arraycopy(path1[z], 0, path[z], 0, path[z].length);
            }
            if (inputBits[0] > 1) {
                codedIndex++;
            }
            step++;

        }
    }

    // returns the decoded sequence at a given time.
    public int[] decodedSeq() {

        int minMetric, minState, current, next;
        int decodedSeq[];
        int tempPath[][];

        tempPath = new int[nbOfStates][step + 1];
        for (int y = 0; y < nbOfStates; y++) {
            for (int z = 0; z < step; z++) {
                tempPath[y][z] = path[y][z];
            }
        }
        // adding the final column to the tempPath matrix
        for (int z = 0; z < nbOfStates; z++) {
            tempPath[z][step] = z;
        }
        // decoding according to the path found
        minMetric = inf;
        minState = 9; // for the compiler
        decodedSeq = new int[step];
        for (int j = 0; j < nbOfStates; j++) {
            if (currentMetric[j] < minMetric) {
                minMetric = currentMetric[j];
                minState = j;
            }
        }

        for (int j = 0; j < step; j++) {
            current = tempPath[minState][j];
            next = tempPath[minState][j + 1];
            if (nextState0[current] == next) {
                decodedSeq[j] = 0;
            } else {
                decodedSeq[j] = 1;
            }
        }
        return decodedSeq;
    }
}
