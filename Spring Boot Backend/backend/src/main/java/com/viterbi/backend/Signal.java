package com.viterbi.backend;
//
//
//	Digital Signal Processing & Graphical Java Classes 
//
//*************************************************************************
//
//	CLASS Signal					 Version 1.0 
//
//	This class is the generic class to create signals.
//	Objects of class Signal will be given as parameters to graphic
//	classes. So, it contains all the protected data members common
//	to all kind of signals that can be generated, in order to provide
//	the graphic classes all the information they need to plot a signal.
//	It contains also a set of public methods to access the data members, 
//	to compute some characteristics of a signal or to modify some of 
//	them.It is a part of the package figure which is the package providing 
//	displaying facilities.
//

//package lcavlib.types;
import java.awt.Color;

//import lcavlib.graphic.twod.displayer.Axis;
public class Signal //implements java.io.Serializable
{

    /**
     * ***********************************************************************
     */
//
//	 Data Members 
//	--------------	
//
//	firstIndex		first real index of the signal
//
//	lastIndex		last real index of the signal
//
//	sampleFrequency		sample frequency of the signal
//
//	frequency 		frequency of the signal
//
//	complex			if true, it's a complex signal
//				if false, it's a real signal
//
//	timeDomain		if true, it's a time domain signal
//				if false, it's a frequency domain signal
//
//	signal[]		array contening the values of the signal
//
//	length			length of the array signal[]
//
//	plotStyle		Style of the signal to be plotted
//
//	plotColor		Color of the signal to be plotted
//
    /**
     * ***********************************************************************
     */
    public final static int PLOT = 1;
    public final static int SOLID = 1;
    public final static int STEM = 2;
    public final static int DASH = 3;
    public final static int DOT = 4;
    public final static int GRIDDOT = 5;
    public final static int BAR = 6;
    public final static int BARFILL = 7;
    public final static int CIRCLE = 8;
    public final static int CONSTANT = 9;
    public final static int CROSS = 10;

    public final static int COS = 0;
    public final static int SIN = 1;
    public final static int CUSTOM = 2;

    /**/	//private Axis axis = new Axis(0.0, 0.0);
    private final static int DEFAULT_SIGNAL_LENGTH = 8192;
    private final static int INCREM_SIGNAL_LENGTH = 100;

    /**
     * ***** Instance Variables *******
     */
    public int firstIndex;
    public int lastIndex;
    public double sampleFrequency;	// sampling frequency
    public double frequency;		// frequency in continous
    public boolean timeDomain;		// time
    public boolean complex;
    public double signal[];
    private int length;			// real size of the vector; 
    // (double for complex)
    private int size;			// actual size of the signal
    public int plotStyle;
    public Color plotColor;
    public int type;			// internal type;

    /**
     * ***********************************************************************
     */
//	C1
//
//	First constructor of this class. It only instanciates the data 
//	members of this class.
//
    /**
     * ***********************************************************************
     */
    public Signal(int first, int last, double freq, double sampFreq, boolean timeDom, boolean comp, int stype) {

        firstIndex = first;
        lastIndex = first - 1;
        frequency = freq;
        sampleFrequency = sampFreq;
        timeDomain = timeDom;
        complex = comp;

        if (complex == false) {
            length = last - firstIndex + 1;
        } else {
            length = 2 * (last - firstIndex + 1);
        }

        signal = new double[length];
        size = 0;

        plotStyle = PLOT;
        plotColor = Color.yellow;
        type = stype;

        switch (type) {

            case COS:
                isCos();
                break;

            case SIN:
                isSin();
                break;
        }

    }

    /**
     * ***********************************************************************
     */
//	C1b
//
//	First constructor of this class. It only instanciates the data 
//	members of this class.
//
    /**
     * ***********************************************************************
     */
    public Signal(int first, int last, double freq, double sampFreq, boolean timeDom, boolean comp) {

        this(first, last, freq, sampFreq, timeDom, comp, CUSTOM);

    }

    /**
     * ***********************************************************************
     */
//
//	C2
//
//	Second constructor of this class. It only instanciates the 
//	data members of this class, including the array of values.
//
    /**
     * ***********************************************************************
     */
    public Signal(double[] vector, double freq, double sampFreq, boolean timeDom, boolean comp) {

        clear(vector, freq, sampFreq, timeDom, comp);
    }

    /**
     * ***********************************************************************
     */
//
//
//	C3
//
//	Default constructor of this class. Initializing the array signal with
//	the default size. 
//
//	Characteristics:
//		- real time domain signal 
//		- null frequencies
//
    /**
     * ***********************************************************************
     */
    public Signal() {

        firstIndex = 0;
        lastIndex = -1;

        frequency = 0;
        sampleFrequency = 0;

        timeDomain = true;
        complex = false;

        length = DEFAULT_SIGNAL_LENGTH;
        size = 0;
        signal = new double[length];

        plotStyle = PLOT;
        plotColor = Color.yellow;

        type = CUSTOM;
    }

    /**
     * ***********************************************************************
     */
//
//	C4
//
//	Fourth constructor of this class. Initializing the array signal with
//	the specified size (lengthS).
//
    /**
     * ***********************************************************************
     */
    public Signal(int l) {

        firstIndex = 0;
        lastIndex = -1;

        frequency = 0;
        sampleFrequency = 0;

        timeDomain = true;
        complex = false;

        length = l;
        size = 0;
        signal = new double[length];

        plotStyle = PLOT;
        plotColor = Color.yellow;

        type = CUSTOM;

    }

    /**
     * ***********************************************************************
     */
//
//	public void copy()
//
//	This method return a copy of the signal.
//
//
    /**
     * ***********************************************************************
     */
    public Signal copy() {

        Signal ret = new Signal(firstIndex, lastIndex, frequency,
                sampleFrequency, timeDomain, complex, CUSTOM);

        ret.insert(signal);
        ret.plotStyle = plotStyle;
        ret.plotColor = plotColor;

        return ret;

    }

    /**
     * ***********************************************************************
     */
//
//	public void clear()
//
//	This method resets the signal.
//
//	WARNING: UNTESTED METHODS
//
//
    /**
     * ***********************************************************************
     */
    public void clear() {
        clear(new double[0], 1, 1, true, false);
    }

    public void clear(double[] vector, double freq, double sampFreq,
            boolean timeDom, boolean comp) {

        complex = comp;
        firstIndex = 0;
        if (complex == false) {
            lastIndex = vector.length - 1;
        } else {
            lastIndex = (int) Math.round(vector.length / 2.0) - 1;
        }
        frequency = freq;
        sampleFrequency = sampFreq;
        timeDomain = timeDom;

        if (complex == false) {
            length = lastIndex - firstIndex + 1;
        } else {
            length = 2 * (lastIndex - firstIndex + 1);
        }

        signal = new double[length];

        for (int i = 0; i <= length - 1; i++) {
            signal[i] = vector[i];
        }

    }

    /**
     * ***********************************************************************
     */
//
//	public void setLength( int l )
//
// 	Allows to modify the length of a vector
//
//	Tested: C1,C3,C4
//
    /**
     * ***********************************************************************
     */
    public void setLength(int l) {

        double nsignal[];

        if (l > length) {
            nsignal = new double[l];
            System.arraycopy(signal, 0, nsignal, 0, length);
            signal = nsignal;
            length = l;
        } else {
            length = l;
        }
    }

    /**
     * ***********************************************************************
     */
//
//	public void getLength()
//
//
    /**
     * ***********************************************************************
     */
    public int getLength() {

        return length;
    }

    /**
     * ***********************************************************************
     */
//
//	public void getSize()
//
//
    /**
     * ***********************************************************************
     */
    public int getSize() {

        return size;
    }

    /**
     * ***********************************************************************
     */
//
//	public void insert(double d[])
//
//	insert a array of double 
//
    /**
     * ***********************************************************************
     */
    public void insert(double d[]) {

        int add = d.length;
        double nsignal[];

        if ((size + add) > length) {

            System.err.println("Warning: signal too short for range! Size increased.");

            if (complex) {
                length = 2 * (size + add);
            } else {
                length = size + add;
            }

            nsignal = new double[length];

            if (complex) {
                System.arraycopy(signal, 0, nsignal, 0, 2 * size);
            } else {
                System.arraycopy(signal, 0, nsignal, 0, size);
            }

            signal = nsignal;
        }

        if (complex) {

            for (int i = 0; i < add; i++) {

                signal[2 * (size + i)] = d[i];
                signal[2 * (size + i) + 1] = 0.0;
            }

        } else {
            System.arraycopy(d, 0, signal, size, add);
        }

        size += add;
        lastIndex += add;

    }

    /**
     * ***********************************************************************
     */
//
//	public void insert(double d)
//
//
    /**
     * ***********************************************************************
     */
    public void insert(double d) {

        double nsignal[];

        if (size == length) {

            System.err.println("Warning: signal too short for range! Size increased.");
            length = 2 * length;
            nsignal = new double[length];

            if (complex) {
                System.arraycopy(signal, 0, nsignal, 0, 2 * size);
            } else {
                System.arraycopy(signal, 0, nsignal, 0, size);
            }

            signal = nsignal;

        }

        if (complex) {
            signal[2 * size] = d;
            signal[2 * size + 1] = 0.0;
            size++;
            lastIndex++;

        } else {
            signal[size++] = d;
            lastIndex++;
        }
    }

    public void insert(double r, double c) {

        double nsignal[];

        if (size == length) {

            System.err.println("Warning: signal too short for range! Size increased.");

            nsignal = new double[2 * length];
            System.arraycopy(signal, 0, nsignal, 0, 2 * size);

            signal = nsignal;

        }

        signal[2 * size] = r;
        signal[2 * size + 1] = c;
        size++;
        lastIndex++;

    }

    /**
     * ***********************************************************************
     */
//
//	public void insertAt(int at, double d)
//
//
//
//	WARNING: The problem of delayed signal is not cleanly solved	
//	question: does 'at' mean a absolute position of the vector or
//		  a relative position (according to the shift)?
//	now: absolute
    /**
     * ***********************************************************************
     */
    public void insertAt(int at, double d) {

        double nsignal[];

        if (at > (length - 1)) {
            System.err.println("Warning: signal too short for range! Size increased.");

            if (complex) {
                nsignal = new double[2 * (at + 1)];
                System.arraycopy(signal, 0, nsignal, 0, 2 * size);
            } else {
                nsignal = new double[at + 1];
                System.arraycopy(signal, 0, nsignal, 0, size);
            }

            signal = nsignal;
            length = 2 * (at + 1);
            lastIndex = at;
        }

        if (complex) {
            signal[2 * at] = d;
            signal[2 * at + 1] = 0.0;
        } else {
            signal[at] = d;
        }

        if (at > size) {
            for (int i = size; i < at; i++) {
                signal[i] = 0.0;
            }

            size = at + 1;
            if (firstIndex != 0) {
                lastIndex = firstIndex + at;
            } else {
                lastIndex = at;
            }
        } else {
            size++;
            lastIndex++;
        }
    }

    public void insertAt(int at, double r, double c) {

        double nsignal[];

        if (!complex) {
            System.err.println("Error: this signal is not complex, please use 'insertAt(int at, double d)' instead.");
            return;
        }

        if (at > (length - 1)) {
            System.err.println("Warning: signal too short for range! Size increased.");

            nsignal = new double[2 * (at + 1)];
            System.arraycopy(signal, 0, nsignal, 0, 2 * size);

            signal = nsignal;
            length = 2 * (at + 1);
            lastIndex = at;
        }

        signal[2 * at] = r;
        signal[2 * at + 1] = c;

        if (at > size) {
            for (int i = size; i < at; i++) {
                signal[2 * i] = 0.0;
                signal[2 * i + 1] = 0.0;
            }
            size = at + 1;

            if (firstIndex != 0) {
                lastIndex = firstIndex + at;
            } else {
                lastIndex = at;
            }
        } else {
            size++;
            lastIndex++;
        }
    }

    /**
     * ***********************************************************************
     */
//
//	public void padd(paddsize)
//
//	This method padd a signal to paddsize
//
//	Tested: C1,C3,C4	
//
    /**
     * ***********************************************************************
     */
    public void padd(int paddsize) {

        int psize,
                tsize,
                nlength;
        double nsignal[];

        if (complex) {
            psize = 2 * paddsize;
            tsize = 2 * size;
        } else {
            psize = paddsize;
            tsize = size;
        }

        nlength = tsize + psize;

        if ((tsize + psize) > length) {

            nsignal = new double[nlength];

            System.arraycopy(signal, 0, nsignal, 0, tsize);

            lastIndex = size + paddsize - 1;
            length = nlength;
            signal = nsignal;
        }

        for (int i = tsize; i < nlength; i++) {
            signal[i] = 0.0;
        }

        size += paddsize;

        if (firstIndex != 0) {
            lastIndex = firstIndex + size - 1;
        } else {
            lastIndex = size - 1;
        }

    }

    /**
     * ***********************************************************************
     */
//
//	public double minElementReal()
//
//	This method returns the minimal element of a real signal.
//
    /**
     * ***********************************************************************
     */
    public double minElementReal() {

        double currentmin;
        currentmin = signal[0];
        for (int i = 1; i < length; i++) {
            currentmin = Math.min(currentmin, signal[i]);
        }

        return currentmin;

    }

    /**
     * ***********************************************************************
     */
//
//	public double[] minElement()
//
//	This method returns the minimal element of a complex or real signal.
//	It returns an array of length 2 contening real and complex part of 
//	the min.
//
    /**
     * ***********************************************************************
     */
    public double[] minElement() {

        double[] currentmin = new double[2];
        double temp;
        if (complex == true) {
            temp = Math.pow(signal[0], 2) + Math.pow(signal[1], 2);
            currentmin[0] = signal[0];
            currentmin[1] = signal[1];
            for (int i = 2; i < length; i++) {
                if ((Math.pow(signal[i], 2) + Math.pow(signal[i + 1], 2)) < temp) {
                    currentmin[0] = signal[i];
                    currentmin[1] = signal[i + 1];
                    temp = Math.pow(signal[i], 2) + Math.pow(signal[i + 1], 2);
                }
                i++;
            }
        } else {
            currentmin[0] = minElementReal();
            currentmin[1] = 0;
        }
        return currentmin;
    }

    /**
     * ***********************************************************************
     */
//
//	public double maxElementReal()
//
//	This method returns the maximal element of a real signal.
//
    /**
     * ***********************************************************************
     */
    public double maxElementReal() {

        double currentmax;
        currentmax = signal[0];
        for (int i = 1; i < length; i++) {
            currentmax = Math.max(currentmax, signal[i]);
        }

        return currentmax;
    }

    /**
     * ***********************************************************************
     */
//
//	public double[] maxElement()
//
//	This method returns the maximal element of a complex or real signal.
//	It returns an array of length 2 contening real and complex part of 
//	the max.
//
    /**
     * ***********************************************************************
     */
    public double[] maxElement() {

        double[] currentmax = new double[2];
        double temp;
        if (complex == true) {
            temp = Math.pow(signal[0], 2) + Math.pow(signal[1], 2);
            currentmax[0] = signal[0];
            currentmax[1] = signal[1];
            for (int i = 2; i < length; i++) {
                if ((Math.pow(signal[i], 2) + Math.pow(signal[i + 1], 2)) > temp) {
                    currentmax[0] = signal[i];
                    currentmax[1] = signal[i + 1];
                    temp = Math.pow(signal[i], 2) + Math.pow(signal[i + 1], 2);
                }
                i++;
            }
        } else {
            currentmax[0] = maxElementReal();
            currentmax[1] = 0;
        }
        return currentmax;
    }

    /**
     * ***********************************************************************
     */
//
//	public double meanValueReal()
//
//	This method returns the mean value of a real signal.
//
    /**
     * ***********************************************************************
     */
    public double meanValueReal() {

        double currentmean = 0;
        for (int i = 0; i < length; i++) {
            currentmean = currentmean + signal[i];
        }
        currentmean = currentmean / length;
        return currentmean;
    }

    /**
     * ***********************************************************************
     */
//
//	public double[] meanValue()
//
//	This method returns the mean value of a real or complex signal.
//	It returns an array of length 2 contening real and complex part of 
//	the mean value.
//
    /**
     * ***********************************************************************
     */
    public double[] meanValue() {

        double[] currentmean = new double[2];
        if (complex == true) {
            for (int i = 0; i < length; i++) {
                currentmean[0] = currentmean[0] + signal[i++];
                currentmean[1] = currentmean[1] + signal[i];
            }
            currentmean[0] = currentmean[0] / (length / 2);
            currentmean[1] = currentmean[1] / (length / 2);
        } else {
            currentmean[0] = meanValueReal();
            currentmean[1] = 0;
        }
        return currentmean;
    }

    /**
     * ***********************************************************************
     */
//
//	public double varValueReal()
//
//	This method returns the variation value of a real signal.
//
    /**
     * ***********************************************************************
     */
    public double varValueReal() {

        double mean = this.meanValueReal();
        double currentvar = 0;

        for (int i = 0; i < length; i++) {
            currentvar = currentvar + Math.pow((signal[i] - mean), 2);
        }
        return (currentvar / length);
    }

    /**
     * ***********************************************************************
     */
//
//	public double[] varValue()
//
//	This method returns the variation value of a real or complex signal.
//	It returns an array of length 2 contening real and complex part of 
//	the variance.
//
    /**
     * ***********************************************************************
     */
    public double[] varValue() {

        double[] mean = this.meanValue();
        double[] currentvar = {0, 0};

        if (complex == true) {
            for (int i = 0; i < length; i++) {
                currentvar[0] = currentvar[0] + ((signal[i] - mean[0]) * (signal[i] - mean[0]) - (signal[i + 1] - mean[1]) * (signal[i + 1] - mean[1]));
                currentvar[1] = currentvar[1] + (2 * (signal[i] - mean[0]) * (signal[i + 1] - mean[1]));
                i++;
            }
            currentvar[0] = currentvar[0] / (length / 2);
            currentvar[1] = currentvar[1] / (length / 2);
        } else {
            currentvar[0] = varValueReal();
            currentvar[1] = 0;
        }
        return currentvar;
    }

    /**
     * ***********************************************************************
     */
//
//	public double sumOfRealElements()
//
//	This method returns the sum of the real elements of the signal that
//	can be real or complex.
//
    /**
     * ***********************************************************************
     */
    public double sumOfRealElements() {

        double currentsum = 0;
        for (int i = 0; i < length; i++) {
            currentsum = currentsum + signal[i];
            if (complex == true) {
                i++;
            }
        }
        return currentsum;
    }

    /**
     * ***********************************************************************
     */
//
//	public double sumOfComplexElements()
//
//	This method returns the sum of the complex elements of a complex
//	signal, and zero if it's a real signal.
//
    /**
     * ***********************************************************************
     */
    public double sumOfComplexElements() {

        double currentsum = 0;
        if (complex == true) {
            for (int i = 1; i < length; i++) {
                currentsum = currentsum + signal[i];
                i++;
            }
        }
        return currentsum;
    }

    /**
     * ***********************************************************************
     */
//
//	public void delay(int delay)
//
//	This method delays the signal of the specified value delay.
//
    /**
     * ***********************************************************************
     */
    public void delay(int delay) {

        firstIndex = firstIndex - delay;
        lastIndex = lastIndex - delay;

    }

    /**
     * ***********************************************************************
     */
//
//	public void delayToZero()
//
//	This method delays the signal so that its first index becomes zero.
//
    /**
     * ***********************************************************************
     */
    public void delayToZero() {

        lastIndex = lastIndex - firstIndex;
        firstIndex = 0;

    }

    /**
     * ***********************************************************************
     */
//
//	public void multiplyBy(double constant)
//
//	This method mutiply the array signal[] by the specified
//	value constant.
//
    /**
     * ***********************************************************************
     */
    public void multiplyBy(double constant) {

        for (int i = 0; i < length; i++) {
            signal[i] = signal[i] * constant;
        }

    }

    /**
     * ***********************************************************************
     */
//
//	public void multiplyBy(double real, double comp)
//
//	This method multiplyes the array signal[] of a complex signal by
//	the complex number specifiedto. If it's a real signal, then nothing
//	is done.
//
    /**
     * ***********************************************************************
     */
    public void multiplyBy(double real, double comp) {

        if (complex == true) {
            for (int i = 0; i < length; i++) {

                double temp = signal[i], temp2 = signal[i + 1];
                signal[i] = temp * real - temp2 * comp;
                signal[i + 1] = temp * comp + temp2 * real;
                i++;
            }
        }

    }

    /**
     * ***********************************************************************
     */
//
//	public Signal abs()
//	
//	This method compute the absolute value of each element of a signal
//	Complex signal become real after this operation.		
//
    /**
     * ***********************************************************************
     */
    public Signal abs() {

        Signal out;

        out = new Signal(firstIndex, lastIndex, frequency, sampleFrequency, timeDomain, false, CUSTOM);

        if (complex) {

            for (int i = 0; i < size; i++) {
                out.insert(Math.sqrt(signal[2 * i] * signal[2 * i] + signal[2 * i + 1] * signal[2 * i + 1]));
            }

        } else {
            for (int i = 0; i < size; i++) {
                out.insert(Math.abs(signal[i]));
            }
        }

        return out;
    }

    /**
     * ***********************************************************************
     */
//
//	public Signal real()
//	
//	Take the real part of the signal.		
//
    /**
     * ***********************************************************************
     */
    public Signal real() {

        Signal out;

        if (complex) {

            out = new Signal(firstIndex, lastIndex, frequency, sampleFrequency, timeDomain, false, CUSTOM);

            for (int i = 0; i < size; i++) {
                out.insert(signal[2 * i]);
            }

            return out;
        }

        return this;
    }

    /**
     * ***********************************************************************
     */
//
//	public double img(Signal s)
//	
// 	Only take the complex part of the signal, returned as a real signal		
//
    /**
     * ***********************************************************************
     */
    public Signal img() {

        Signal out;

        out = new Signal(firstIndex, lastIndex, frequency, sampleFrequency, timeDomain, false, CUSTOM);

        if (complex) {

            for (int i = 0; i < size; i++) {
                out.insert(signal[2 * i + 1]);
            }

        } else {

            for (int i = 0; i < size; i++) {
                out.insert(0.0);
            }

        }

        return out;

    }

    /**
     * ***********************************************************************
     */
//
//	public void additionOf(double real)
//
//	This method adds a real number to the real part of a complex or
//	real signal.
//
    /**
     * ***********************************************************************
     */
    public void add(double real) {
        additionOf(real);
    }

    public void additionOf(double real) {

        error(DEPRECATED_METHOD, "additionOf(double real)", "Use add(double real) instead.");

        for (int i = 0; i < size; i++) {

            signal[i] = signal[i] + real;

            if (complex == true) {
                i++;
            }
        }

    }

    /**
     * ***********************************************************************
     */
//
//	public void additionOf(double real, double comp)
//
//	This method adds a complex number to a complex signal. If the signal
//	is real, then nothing is done. 
//
    /**
     * ***********************************************************************
     */
    public void add(double real, double comp) {
        additionOf(real, comp);
    }

    public void additionOf(double real, double comp) {

        error(DEPRECATED_METHOD, "additionOf(double real, double comp)", "Use add(double real, double comp) instead.");

        if (complex == true) {

            for (int i = 0; i < size; i++) {

                signal[i] = signal[i] + real;
                i++;
                signal[i] = signal[i] + comp;
            }
        }

    }

    /**
     * ***********************************************************************
     */
//
//	protected int power2Length(int len) 
//
//	This method computes the closest and higher power of two
//	of the given input.  
//
    /**
     * ***********************************************************************
     */
    protected int power2Length(int len) {

        int compteur = 0, len2 = 0;
        while (len > 2) {

            len = len / 2;
            compteur++;
        }

        len2 = (int) Math.pow(2, compteur + 1);

        return len2;
    }

    /**
     * ***********************************************************************
     */
//
//	protected boolean isLengthPower2(int len) 
//
//	This method checks if the length of a signal is a power
//	of two or not.
//
    /**
     * ***********************************************************************
     */
    protected boolean isLengthPower2(int len2) {

        double len = (double) len2;
        boolean is2;
        while (len > 2) {

            len = len / 2;
        }

        if (len == 2) {
            is2 = true;
        } else {
            is2 = false;
        }

        return is2;
    }

    /**
     * ************************************************************************
     *
     *      *METHODS FOR CREATING SIGNALS

          *****************************************************************************
     */
    /**
     * ***********************************************************************
     */
//
//	public void range(double a, double b)
//
//	This method fills array signal starting from signal[0]=a, by step 
//	of 1, to signal[n]=b.
//
//	Tests: C1,C3,C4
    /**
     * ***********************************************************************
     */
    public void range(double a, double b) {

        // checking if signal length is sufficient
        int sl = (int) (Math.abs(b - a) + 1);

        if (sl > length) {
            System.err.println("Warning: signal too short for range! Size increased.");
            signal = new double[sl];
            length = sl;
        }

        size = sl;
        lastIndex = sl - 1;

        signal[0] = a;

        if (a < b) {
            for (int i = 1; i < sl; i++) {
                signal[i] = signal[i - 1] + 1.0;
            }
        } else {

            for (int i = 1; i < sl; i++) {
                signal[i] = signal[i - 1] - 1.0;
            }
        }

    }

    /**
     * ***********************************************************************
     */
//
//	public void range(double a, double step,  double b)
//
//	This method fills array signal starting from signal[0]=a, by step 
//	of step, to signal[n]=b.
//
//	Tests: C1,C3,C4
    /**
     * ***********************************************************************
     */
    public void range(double a, double step, double b) {

        // checking if signal length is sufficient
        int sl = (int) (Math.ceil(Math.abs((b - a) / step))) + 1;

        if (sl > length) {
            System.err.println("Warning: signal too short for range! Size increased.");
            signal = new double[sl];

            length = sl;
        }

        size = sl;
        lastIndex = sl - 1;

        signal[0] = a;

        if (a < b) {

            for (int i = 1; i < sl; i++) {
                signal[i] = signal[i - 1] + step;
            }
        } else {

            for (int i = 1; i < sl; i++) {
                signal[i] = signal[i - 1] - step;
            }
        }

    }

    /**
     * ***********************************************************************
     */
//
//	public void isCos()
//
//	This method create a cosine with the internal parameters
//
//	Test: C1,C3.C4
    /**
     * ***********************************************************************
     */
    public void isCos() {

        type = COS;

        if ((frequency == 0) || (sampleFrequency == 0)) {

            System.err.println("Error: the frequency of the continous or the sampling frequency are set to 0, aborting operation.");
            return;
        }

        double T = 1 / sampleFrequency,
                f = 2 * Math.PI * frequency;

        double fact = T * f;

        for (int i = 0; i < length; i++) {
            signal[i] = Math.cos(fact * i);
        }

        size = length;
        lastIndex = length - 1;

    }

    /**
     * ***********************************************************************
     */
//
//	public void isSin()
//
//	This method create a sine with the internal parameters
//
//	Test: C1,C3.C4
    /**
     * ***********************************************************************
     */
    public void isSin() {

        type = SIN;

        if ((frequency == 0) || (sampleFrequency == 0)) {

            System.err.println("Error: the frequency of the continous or the sampling frequency are set to 0, aborting operation");
            return;

        }

        double T = 1 / sampleFrequency,
                f = 2 * Math.PI * frequency;

        double fact = T * f;

        for (int i = 0; i < length; i++) {
            signal[i] = Math.sin(fact * i);
        }

        size = length;
        lastIndex = length - 1;
    }

    /**
     * ***********************************************************************
     */
//
//	public void Cos(Signal s)
//
//	This method takes the cosinus of each element of array signal.
//
// Test: C3,C4
    /**
     * ***********************************************************************
     */
    public void cos(Signal s) {

        if (s.getSize() > length) {
            System.err.println("Warning: signal too short for range! Size increased.");
            signal = new double[s.getSize()];
            length = s.getSize();
        }

        size = s.getSize();
        lastIndex = size - 1;

        for (int i = 0; i < s.getSize(); i++) {
            signal[i] = Math.cos(s.signal[i]);
        }
    }

    /**
     * ***********************************************************************
     */
//
//	public void Sin(Signal s)
//
//	This method takes the sinus of each element of array signal.
//
// Test: C3,C4
    /**
     * ***********************************************************************
     */
    public void sin(Signal s) {

        if (s.getSize() > length) {
            System.err.println("Warning: signal too short for range! Size increased.");
            signal = new double[s.getSize()];
            length = s.getSize();
        }

        size = s.getSize();
        lastIndex = size - 1;

        for (int i = 0; i < s.getSize(); i++) {
            signal[i] = Math.sin(s.signal[i]);
        }
    }

    /**
     * ***********************************************************************
     */
//
//	public void Mult(Signal s)
//
//	This method makes the multiplication of the current signal with
//	the signal s in parameter.
//
    /**
     * ***********************************************************************
     */
    public void multiplyBy(Signal s) {
        int loop = s.length,
                i;

        if ((!complex) && (!s.complex)) {

            if (length != loop) {
                System.err.println("Error: signals size differ");
                System.exit(0);
            }

            for (i = 0; i < loop; i++) {
                signal[i] = signal[i] * s.signal[i];
            }
        } else if ((complex) && (!s.complex)) {

            if ((length / 2) != loop) {
                System.err.println("Error: signals size differ");
                System.exit(0);
            }

            for (i = 0; i < loop; i++) {
                signal[2 * i] = signal[2 * i] * s.signal[i];
                signal[2 * i + 1] = signal[2 * i + 1] * s.signal[i];
            }
        } else if ((!complex) && (s.complex)) {

            if (length != (loop / 2)) {
                System.err.println("Error: signals size differ");
                System.exit(0);
            }

            double nsignal[] = new double[loop];

            for (i = 0; i < length; i++) {
                nsignal[2 * i] = signal[i] * s.signal[2 * i];
                nsignal[2 * i + 1] = signal[i] * s.signal[2 * i + 1];
            }

            lastIndex = lastIndex + length;
            length = loop;
            complex = true;
            signal = nsignal;
        } else if ((complex) && (s.complex)) {

            if (length != loop) {
                System.err.println("Error: signals size differ");
                System.exit(0);
            }

            int size = length / 2;
            double real,
                    comp;

            for (i = 0; i < size; i++) {
                real = signal[2 * i] * s.signal[2 * i]
                        - // real 
                        signal[2 * i + 1] * s.signal[2 * i + 1];	// component
                comp = signal[2 * i] * s.signal[2 * i + 1]
                        + // complex
                        signal[2 * i + 1] * s.signal[2 * i];	// component

                signal[2 * i] = real;
                signal[2 * i + 1] = comp;
            }
        }
    }

    /**
     * ***********************************************************************
     */
//
//	public double data(int i)
//
//	This method returns the element signal[i].
//
    /**
     * ***********************************************************************
     */
    public double data(int i) {

        System.err.println("Warning: this method is obsolete, please access the array 'signal[]' instead.");

        if (i >= length) {
            return Double.MAX_VALUE;
        } else {
            return signal[i];
        }
    }

    /**
     * ***********************************************************************
     */
//
//	public double max() 
//
//	This method returns the maximum value of the array signal[i].
//
    /**
     * ***********************************************************************
     */
    public double max() {
        double tmp = -Double.MAX_VALUE;

        if (size == 0) {
            System.err.println("Warning: trying to get the max of a null signal");
            return (0.0);
        }
        for (int n = 0; n < size; n++) {
            if (signal[n] > tmp) {
                tmp = signal[n];
            }
        }
        return (tmp);
    }

    /**
     * ***********************************************************************
     */
//
//	public double maxabs()
//
//	This method returns the maximum value of the array signal[i] in 
//	absolute value. 
//
    /**
     * ***********************************************************************
     */
    public double maxabs() {
        double tmp = -Double.MAX_VALUE;

        if (size == 0) {
            System.err.println("Warning: trying to get the max of a null signal");
            return (0.0);
        }
        for (int n = 0; n < size; n++) {
            if (Math.abs(signal[n]) > tmp) {
                tmp = signal[n];
            }
        }
        return (tmp);
    }

    /**
     * ***********************************************************************
     */
//
//	public double min()
//
//	This method returns the minimum value of the array signal[i].
//
    /**
     * ***********************************************************************
     */
    public double min() {
        double tmp = Double.MAX_VALUE;

        if (size == 0) {
            System.err.println("Warning: trying to get the min of a null signal");
            return (0.0);
        }
        for (int n = 0; n < size; n++) {
            if (signal[n] < tmp) {
                tmp = signal[n];
            }
        }
        return (tmp);
    }

    /**
     * ***********************************************************************
     */
//
//	public double minabs()
//
//	This method returns the minimum value of the array signal[i] in
//	absolute value.
//
    /**
     * ***********************************************************************
     */
    public double minabs() {
        double tmp = Double.MAX_VALUE;

        if (size == 0) {
            System.err.println("Warning: trying to get the min of a null signal");
            return (0.0);
        }
        for (int n = 0; n < size; n++) {
            if (Math.abs(signal[n]) < tmp) {
                tmp = signal[n];
            }
        }
        return (tmp);
    }

    //public Axis getAxis() {
    //	
    //	System.err.println("Warning: this method is obsolete, please use <instance>.firstIndex and <instance>.lastIndex"); 
    //	return(new Axis((double)firstIndex, (double)(firstIndex+size-1)));
    //}
    /**
     * ***********************************************************************
     */
//
//	Debugging routines
//
//
    /**
     * ***********************************************************************
     */
    public void printState() {

        System.err.println("Signal Properties:");
        System.err.println("First index: " + firstIndex);
        System.err.println("Last index: " + lastIndex);
        System.err.println("Frequency: " + frequency);
        System.err.println("Sampling Frequency: " + sampleFrequency);
        System.err.println("Time Domain: " + timeDomain);
        System.err.println("Complexe: " + complex);
        System.err.println("Length: " + length);
        System.err.println("Size: " + size);

    }

    public void print() {

        System.err.println("Signal values:");

        if (!complex) {
            for (int i = 0; i < size; i++) {
                System.err.print(signal[i] + " ");
            }
        } else {
            for (int i = 0; i < size; i++) {
                System.err.print(signal[2 * i] + "+i" + signal[2 * i + 1] + " ");
            }
        }

        System.err.println("end");
    }

    /**
     * ***********************************************************************
     */
//
//	Error handling
//
    /**
     * ***********************************************************************
     */
    public final static int DEPRECATED_METHOD = 0;

    public void error(int type) {

        switch (type) {

        }
    }

    public void error(int type, String where, String todo) {

        switch (type) {

            case DEPRECATED_METHOD:
                System.err.println("Warning: usage of " + where + " is deprecated. " + todo);
                break;

        }
    }
}
