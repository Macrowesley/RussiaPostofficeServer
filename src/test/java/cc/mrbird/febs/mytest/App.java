package cc.mrbird.febs.mytest;

public class App {
    public static void main(String[] args) {
        MyCalculator vehical = (MyCalculator)CalculatorProxy.getInstance(new MyCalculatorImpl());
        int res = vehical.add(1,5);
        System.out.println(res);
    }
}
