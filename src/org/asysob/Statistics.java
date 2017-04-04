package org.asysob;

public class Statistics {

    public Statistics () {
        n = 0;
        sum = 0.0;
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;
    }

    public void StartMeasure () {
        t_start = System.nanoTime();
    }

    public void StopMeasure () {
        t_stop = System.nanoTime();
        double d = Duration();
        sum += d;
        n++;
        if (d < min) min = d;
        if (d > max) max = d;
    }

    public double Duration () {
        return (((double) t_stop) - ((double) t_start)) / 1000.0 / 1000.0;
    }

    public String Report () {
        StringBuilder s = new StringBuilder();
        s.append("n=");
        s.append(n);
        s.append(", min=");
        s.append(String.format("%.4E",min));
        s.append(", average=");
        s.append(String.format("%.4E",sum/((double) n)));
        s.append(", max=");
        s.append(String.format("%.4E",max));
        return s.toString();
    }

    private long t_start;
    private long t_stop;

    private long n;
    private double sum;
    private double min;
    private double max;
}
