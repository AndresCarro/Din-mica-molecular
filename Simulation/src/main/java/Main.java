import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SimulationConfig config = readConfig("Simulation/input/input.json");
        if(config == null) {
            return;
        }

        SimulationFactory simulator = new SimulationFactory(config.getL(), config.getN(), config.getRadius(), config.getSpeed(), config.getMass(), config.getMovable());
        simulator.simulate(config.getTotalTime());
    }

    public static SimulationConfig readConfig(String path){
        Gson gson = new Gson();
        SimulationConfig sConfig = null;
        try (FileReader reader = new FileReader(path)) {
            sConfig = gson.fromJson(reader, SimulationConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sConfig;
    }
}