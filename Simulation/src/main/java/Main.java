import com.google.gson.Gson;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SimulationConfig config = readConfig("Simulation/input/input.json");
        if(config == null) {
            return;
        }

        SimulationFactory simulator = new SimulationFactory(config.getL(), config.getN(), config.getRadius(), config.getSpeed(), config.getMass(), config.getMovable());
        simulator.simulate(config.getTotalTime(), config.getFrameSize());
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

    public void writeStatus(double noise, double L, double speed, int N, double interactionRadius, boolean boundaryConditions, int totalTime){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("N", N);
            jsonObject.put("L", L);
            jsonObject.put("speed",speed);
            jsonObject.put("totalTime", totalTime);
            jsonObject.put("noise", noise);
            jsonObject.put("radius", interactionRadius);
            jsonObject.put("boundary", boundaryConditions);

            FileWriter writer_status = new FileWriter("output/StateData_" + N + "_" + (int) L + "_" + noise + ".json");
            writer_status.write(jsonObject.toString());
            writer_status.close();

        } catch(IOException e){
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

}