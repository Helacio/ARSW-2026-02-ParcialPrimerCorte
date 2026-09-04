package edu.eci.arsw.math.concurrency;

import edu.eci.arsw.math.PiDigits;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.stream.*;



public class FindPiDigits extends Thread {
    private int start;
    private int count;

    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;
    //private List<Double> myDigits=  new ArrayList<Double>();
    byte[] digits;

    /**
     * Returns a range of hexadecimal digits of pi.
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public FindPiDigits(int start, int count) {
        this.start = start;
        this.count  =count;
    }


    @Override
    public void run() {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        digits = new byte[count];
        double sum = 0;

        for (int i = 0; i < count; i++) {
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);

                start += DigitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;

        }

    }


    public byte[] getDigits() {
        return digits;
    }


    private static double sum(int m, int n) {
            double sum = 0;
            int d = m;
            int power = n;

            while (true) {
                double term;

                if (power > 0) {
                    term = (double) hexExponentModulo(power, d) / d;
                } else {
                    term = Math.pow(16, power) / d;
                    if (term < Epsilon) {
                        break;
                    }
                }

                sum += term;
                power--;
                d += 8;
            }

            return sum;
        }

        /// <summary>
        /// Return 16^p mod m.
        /// </summary>
        /// <param name="p"></param>
        /// <param name="m"></param>
        /// <returns></returns>
        private static int hexExponentModulo(int p, int m) {
            int power = 1;
            while (power * 2 <= p) {
                power *= 2;
            }

            int result = 1;

            while (power > 0) {
                if (p >= power) {
                    result *= 16;
                    result %= m;
                    p -= power;
                }

                power /= 2;

                if (power > 0) {
                    result *= result;
                    result %= m;
                }
            }

            return result;
        }

}
