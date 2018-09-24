class MathUtil {
    static double divide(int a, int b) {
        return (double) a / (double) b;
    }

    static double log2(double number) {
        return logb(number, 2);
    }

    private static double logb(double number, double base) {
        return Math.log(number) / Math.log(base);
    }
}
