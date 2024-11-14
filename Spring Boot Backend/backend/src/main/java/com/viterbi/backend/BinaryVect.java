// BinaryVect objects represent sequences of bits. This class allows
// us to perform some operations on them.
// Olivier Swedor, EPFL, summer '98.
package com.viterbi.backend;

import java.io.*;
import java.util.*;

public class BinaryVect
{
    int[] array;

    public BinaryVect(int length) {
	this.array = new int[length];
	for (int i=0; i<length; i++) {
	    this.array[i] = 0;
		}
    }

    // sets the bit at position index in the array representing
    //the sequence of bits.
    public void set(int index, int number) {
	this.array[index] = number;
    }

    // sets the array representing the sequence of bits.
    public void set(int[] array) {
	this.array = array;
    }

    // adds a bit at the end.
    public void add(int newBit) {

	int n;
	int newArray[];

	n = (this.array).length;
	newArray = new int[n+1];
	for (int i=0; i<n; i++) {
	    newArray[i] = this.array[i];
	}
	newArray[n] = newBit;
	this.array = newArray;
    }

    // adds a bit at the beginning.
    public void addAtBeginning(int newBit) {
	
	int n;
	int newArray[];
	
	n = (this.array).length;
	newArray = new int[n+1];
	for (int i=0; i<n; i++) {
	    newArray[i+1] = this.array[i];
	}
	newArray[0] = newBit;
	this.array = newArray;
    }


    // drops the first bit of a BinaryVect.
    public void  dropFirstCol() {
	
	int n;
	int newArray[];
	
	n = (this.array).length;
	newArray = new int[n-1];
	for (int i=1; i<n; i++) {
	    newArray[i-1] = this.array[i];
	}
	this.array = newArray;
    }

    // returns the bit at position index.
    public int get(int index) {
	return this.array[index];
    }
    
    // returns the array
    public int[] array() {
	return this.array;
    }

    // returns the length of the BinaryVect.
    public int length() {
	return (this.array).length;
    }
    
    // sets all the bits of a BinaryVect to zero.
    public void setToZero() {
  
	for (int i=0; i<this.length(); i++) {
	    this.array[i] = 0;
		}
    }

    // returns the hamming distance between two BinaryVect.
    public int hammingDist(BinaryVect v) {
	
	int dist = 0;

	if (this.length() == v.length()) {
	    for (int i=0; i<this.length(); i++) {
		if (this.array[i] != v.array[i]) {
		    dist++;
		}
	    }
	} 
	else {
	    System.err.print("error hamming dist!");
	}
	return dist;
    }
    
    // converts a BinaryVect into the decimal number it represents.
    public int binary2dec() {

	int dec = 0;
	int nbOfBits;

	nbOfBits = this.length();
	for (int i=0; i<nbOfBits-1; i++) {
	    dec = dec + this.array[i]*(int)(java.lang.Math.pow(2, nbOfBits-i-1));
	}
	dec = dec + this.array[nbOfBits-1];
	return dec;
    }

    // displays a BinaryVect (for debugging purpose). 
    public void display() {
  
	for (int i=0; i<this.length(); i++) {
	    System.err.print(this.array[i]);
	}
	System.err.print(" ");
    }

    // performs the modulo-2 multiplication between two BinaryVects.
    public BinaryVect multiplicate(BinaryVect v2) {
  
	int n1, n2;
	BinaryVect v1, r;
  
	v1 = this;
	n1 = v1.length();
	n2 = v2.length();
	r = new BinaryVect(n1+n2-1);
	r.setToZero();
	for (int i=0; i<n1; i++) {
	    for (int j=0; j<n2; j++) {
		r.array[i+j] = r.array[i+j]+(v1.array[i]*v2.array[j]);
	    }
	}
	for (int i=0; i<n1+n2-1; i++) {
	    r.array[i] = r.array[i] % 2;
	}
	return r;
    }
    
    // encodes a BinaryVect convolutionally according to the 
    // given impulse response (or generator polynomial).
    public int[][] convCode(int [][] impResp) {
	
	int n, k;
	int codedMessage[][];
	BinaryVect impRespRow;
	n = impResp.length;
	k = impResp[0].length;
	codedMessage = new int[n][this.length()+k-1];
	impRespRow = new BinaryVect(k);
	for (int i=0; i<n; i++) {
	    impRespRow.set(impResp[i]);
	    codedMessage[i] = (impRespRow.multiplicate(this)).array;
	}
	return codedMessage;
    }
}
