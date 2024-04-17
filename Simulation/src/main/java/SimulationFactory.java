import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class SimulationFactory {
    private final ParticlesList ParticlesList;
    private final CrashList CrashList;
    private final String outputFile;
    private final boolean movable;
    private final double maxCrash;
    private final double frameCrash;

    public SimulationFactory(double L, int N, double speed, boolean movable, double maxCrash, double frameCrash) {
        ParticlesList = new ParticlesList();
        CrashList = new CrashList();

        // revisar la asignacion en algun momento
        this.maxCrash =maxCrash;
        this.frameCrash =frameCrash;
        this.movable = movable;
        this.outputFile = "Simulation/output/SimulationData_" + N + "_" + (int) L + "_" + (int) speed + ".csv";
        String outputStatus = "Simulation/output/StateData_" + N + "_" + (int) L + "_" + (int) speed + ".json";
        writeStatus(outputStatus, L,  N, speed, movable, maxCrash, this.frameCrash);

        createParticles(N, L, speed);
        setTimes();
    }

    public void simulate(){
        try{
            FileWriter writer_data = new FileWriter(this.outputFile);
            writer_data.write("id,x,y,vel,angulo,time,crash,ParticleA,ParticleB");

            Crash crash = CrashList.nextCrash();
            double prevTime = 0;
            double actualTime = crash.getTime();
            int counterCrash = 0;

            while( counterCrash < this.maxCrash){
                counterCrash++;
                if(counterCrash % this.frameCrash == 0){
                    for(Particle particle : ParticlesList.getParticles()){
                        particle.move(actualTime - prevTime);
                        writer_data.write("\n" + particle.getId() + "," + particle.getX() + "," + particle.getY() + "," + particle.getSpeed() + "," + particle.getAngle() + "," + actualTime + "," + crash.getSolid().name() + "," + crash.getParticleA().getId() + "," + crash.getParticleB().getId());
                    }
                }else{
                    for(Particle particle : ParticlesList.getParticles()){
                        particle.move(actualTime - prevTime);
                    }
                }


                crash.makeCrash();
                changeCrashes(crash, actualTime);

                prevTime = actualTime;
                crash = CrashList.nextCrash();
                actualTime = crash.getTime();
            }

            writer_data.close();
        } catch(IOException e){
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    private void createParticles(int N, double L, double speed) {
        int NewN = N;
        if(this.movable){
            NewN += 2;
        }
        Particle centerParticle = new Particle(L, N);
        this.ParticlesList.insertNewParticle(centerParticle);
        for (int i = 0; i < N; i++) {
            Particle auxParticle = new Particle(L, speed, NewN, i);
            while (!this.ParticlesList.insertNewParticle(auxParticle)) {
                auxParticle = new Particle(L, speed, NewN, i);
            }
        }
        if(!this.movable){
            this.ParticlesList.removeParticle(centerParticle);
        }
    }

    private void setTimes() {
        for (int i = 0; i < ParticlesList.getParticles().size(); i++) {
            for (int j = i + 1; j < ParticlesList.getParticles().size(); j++) {
                CrashList.createCrash(ParticlesList.getParticles().get(i), ParticlesList.getParticles().get(j));
            }
            for (Solid solid : Solid.values()) {
                if(!this.movable || ! (solid == Solid.CENTER)){
                    CrashList.createCrash(ParticlesList.getParticles().get(i), solid);
                }
            }
        }
    }

    public void changeCrashes(Crash crash, double actualTime){
        for(int i=0; i<ParticlesList.getParticles().size(); i++){
            if(ParticlesList.getParticles().get(i).getId()  == crash.getParticleB().getId() && crash.getSolid() == Solid.NONE){
                CrashList.updateCrash(crash.getParticleA(), crash.getParticleB(), Double.POSITIVE_INFINITY);
            }else{
                CrashList.updateCrash(crash.getParticleA(), ParticlesList.getParticles().get(i), actualTime);
            }
        }
        for(Solid solid : Solid.values()){
            if(!this.movable || ! (solid == Solid.CENTER)) {
                CrashList.updateCrash(crash.getParticleA(), solid, actualTime);
            }
        }

        if(crash.getSolid() == Solid.NONE){
            for(int i=0; i<ParticlesList.getParticles().size(); i++){
                if(ParticlesList.getParticles().get(i).getId()  == crash.getParticleA().getId() && crash.getSolid() == Solid.NONE){
                    CrashList.updateCrash(crash.getParticleB(), crash.getParticleA(), Double.POSITIVE_INFINITY);
                }else{
                    CrashList.updateCrash(crash.getParticleB() ,ParticlesList.getParticles().get(i), actualTime);
                }
            }
            for(Solid solid : Solid.values()){
                if(!this.movable || ! (solid == Solid.CENTER)) {
                    CrashList.updateCrash(crash.getParticleB(), solid, actualTime);
                }
            }
        }
    }

    public void writeStatus(String file, double L, int N, double speed, boolean movable, double maxTime, double frameSize){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("L", L);
            jsonObject.put("N", N);
            jsonObject.put("speed",speed);
            jsonObject.put("totalTime", maxTime);
            jsonObject.put("frameSize", frameSize);
            jsonObject.put("movable", movable);
            jsonObject.put("radius_particle", Utils.PARTICLE_RADIUS);
            jsonObject.put("radius_solid", Utils.CENTER_RADIUS);
            jsonObject.put("mass_particle", Utils.PARTICLE_MASS);
            jsonObject.put("mass_solid", Utils.CENTER_MASS);

            FileWriter writer_status = new FileWriter(file);
            writer_status.write(jsonObject.toString());
            writer_status.close();

        } catch(IOException e){
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

}
