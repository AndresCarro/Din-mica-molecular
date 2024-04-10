public class Main {
    public static void main(String[] args) {
        int N = 2;
        double L = 20;
        double radius = 0.1;
        double mass = 1;
        double speed = 1;
        double maxTime = 20;


        SimulationFactory simulator = new SimulationFactory(L, N, radius, speed,mass);
        simulator.simulate(maxTime);
    }
}