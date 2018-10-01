package genetic;

/**
 * parameters for linear function y = ax + b
 */
public class FuncParam {
    private final double a;
    private final double b;
    
    public double getA() {
        return a;
    }
    
    public double getB() {
        return b;
    }
    
    
    
    public FuncParam(double a, double b) {
        this.a = a;
        this.b = b;
    }
}
