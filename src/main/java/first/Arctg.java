package first;

public class Arctg {
    public double getResult(double x) {
        double result = 0;
        for(int n = 0; n < 100; n++) {
            result += Math.pow(-1.0, n) * Math.pow(x, 2 * n + 1) / (2 * n + 1);
        }
        return result;
    }
}
