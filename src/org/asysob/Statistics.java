package org.asysob;

public class Statistics {

    public Statistics(int skip) {
        n = 0;
        sum = 0.0;
        sum_squares = 0.0;
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;
        this.skip = skip;
        startHappened = false;
    }

    public void StartMeasure() {
        t_start = System.nanoTime();
        startHappened = true;
    }

    public void StopMeasure() {
        assert (startHappened);
        startHappened = false;
        if (skip-- > 0) return;
        t_stop = System.nanoTime();
        double d = Duration();
        sum += d;
        sum_squares += Math.pow(d,2);
        n++;
        if (d < min) min = d;
        if (d > max) max = d;
    }

    public double Duration() {
        assert (startHappened);
        return (((double) t_stop) - ((double) t_start)) / 1000.0 / 1000.0;
    }

    public long NumberOfMeasures() {
        return n;
    }

    public double AverageDuration() {
        return sum / ((double) n);
    }

    public double StandardDeviation() {
        double result = sum_squares - 2 * AverageDuration() * sum + n * Math.pow(AverageDuration(), 2);
        return Math.sqrt(result) / ((double) n);
    }

    public double MinimumDuration() {
        return min;
    }

    public double MaximumDuration() {
        return max;
    }

    public String Report() {
        StringBuilder s = new StringBuilder();
        s.append("n=");
        s.append(n);
        s.append(", min=");
        s.append(String.format("%.4E", min));
        s.append(", average=");
        s.append(String.format("%.4E", AverageDuration()));
        s.append(", sdeviation=");
        s.append(String.format("%.4E", StandardDeviation()));
        s.append(", max=");
        s.append(String.format("%.4E", max));
        return s.toString();
    }

    private long t_start;
    private long t_stop;

    private long n;
    private double sum;
    private double sum_squares;
    private double min;
    private double max;

    private int skip;
    private boolean startHappened;
}
