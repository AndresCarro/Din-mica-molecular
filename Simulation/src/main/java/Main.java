public class Main {
    public static void main(String[] args) {
        int N = 200;
        double L = 20;
        double radius = 0.1;
        double mass = 1;
        double speed = 10;
        double maxTime = 10;


        SimulationFactory simulator = new SimulationFactory(L, N, radius, speed,mass);
        simulator.simulate(maxTime);
    }
}