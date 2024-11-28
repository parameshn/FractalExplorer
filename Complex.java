public class Complex {
    private double real;
    private double imag;

    public Complex(double real, double imag) {
        this.real = real;
        this.imag = imag;
    }

    public Complex add(Complex other) {
        return new Complex(this.real + other.real, this.imag + other.imag);
    }

    public Complex subtract(Complex other) {
        return new Complex(this.real - other.real, this.imag - other.imag);
    }

    // public Complex multiply(Complex other) {
    //     double realPart = this.real * other.real - this.imag * other.imag;
    //     double imagPart = this.real * other.imag + this.imag * other.real;
    //     return new Complex(realPart, imagPart);
    // }
    public Complex multiply(double scalar) {
        return new Complex(this.real * scalar, this.imag * scalar);
    }

    public Complex divide(Complex other) {
        double denominator = other.real * other.real + other.imag * other.imag;
        double realPart = (this.real * other.real + this.imag * other.imag) / denominator;
        double imagPart = (this.imag * other.real - this.real * other.imag) / denominator;
        return new Complex(realPart, imagPart);
    }

    public double abs() {
        return Math.sqrt(real * real + imag * imag);
    }

    public Complex pow(int power) {
        Complex result = new Complex(1, 0); // Start with 1 + 0i
        for (int i = 0; i < power; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    public Complex negate() {
        return new Complex(-real, -imag);
    }

    @Override
    public String toString() {
        return real + " + " + imag + "i";
    }
}
