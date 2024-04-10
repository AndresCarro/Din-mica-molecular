public class SimulationConfig {
    private double L;
    private double speed;
    private int N;
    private double radius;
    private int totalTime;
    private double mass;

    public SimulationConfig(){

    }

    public double getL() {
        return L;
    }

    public double getSpeed() {
        return speed;
    }

    public int getN() {
        return N;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setL(double l) {
        L = l;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setN(int n) {
        N = n;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }


    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }
}
