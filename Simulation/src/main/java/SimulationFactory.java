
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationFactory {
    private final ParticlesList ParticlesList;
    private final CrashList CrashList;
    private String outputFile;

    public SimulationFactory(double L, int N, double radius, double speed, double mass) {
        ParticlesList = new ParticlesList();
        CrashList = new CrashList();
        this.outputFile = "SimulationData_" + N + "_" + (int) L + ".csv";

        createParticles(N, L, radius, speed, mass);
        setTimes();
    }

    public void simulate(double maxTime){
        try{
            FileWriter writer_data = new FileWriter(this.outputFile);
            writer_data.write("id,x,y,vel,angulo,time,crash");

            for(Particle particle : ParticlesList.getParticles()){
                writer_data.write( "\n" + particle.getId() + "," + particle.getX() + "," + particle.getY() + "," + particle.getSpeed() + "," + particle.getAngle() + "," + 0 + "," + "INIT");
            }

            Crash crash = CrashList.nextCrash();
            double prevTime = 0;
            double actualTime = crash.getTime();

            while( actualTime < maxTime){
                for(Particle particle : ParticlesList.getParticles()){
                    particle.move(actualTime - prevTime);
                }

                crash.makeCrash();
                changeCrashes(crash, actualTime);

                for(Particle particle : ParticlesList.getParticles()){
                    writer_data.write( "\n" + particle.getId() + "," + particle.getX() + "," + particle.getY() + "," + particle.getSpeed() + "," + particle.getAngle() + "," + actualTime + "," + "pepe");
                }

                prevTime = actualTime;
                crash = CrashList.nextCrash();
                actualTime = crash.getTime();
            }
            writer_data.close();
        } catch(IOException e){
            System.out.println("Error al escribir en el archivo: " + e.getMessage());
        }
    }

    private void createParticles(int N, double L, double radius, double speed, double mass) {
        for (int i = 0; i < N; i++) {
            Particle auxParticle = new Particle(L, speed, radius, mass,N, i);
            while (!this.ParticlesList.insertNewParticle(auxParticle)) {
                auxParticle = new Particle(L, speed, radius, mass,N, i);
            }
        }
    }

    private void setTimes() {
        for (int i = 0; i < ParticlesList.getParticles().size(); i++) {
            for (int j = i + 1; j < ParticlesList.getParticles().size(); j++) {
                CrashList.createCrash(ParticlesList.getParticles().get(i), ParticlesList.getParticles().get(j));
            }
            for (Solid solid : Solid.values()) {
                CrashList.createCrash(ParticlesList.getParticles().get(i), solid);
            }
        }
    }

    // Cambio los choques asociados a las particulas que chocaron
    public void changeCrashes(Crash crash, double actualTime){
        for(int i=0; i<ParticlesList.getParticles().size(); i++){
            CrashList.updateCrash(crash.getParticleA(), ParticlesList.getParticles().get(i), actualTime);
        }
        for(Solid solid : Solid.values()){
            CrashList.updateCrash(crash.getParticleA(), solid, actualTime);
        }

        if(crash.getSolid() == Solid.NONE){
            for(int i=0; i<ParticlesList.getParticles().size(); i++){
                CrashList.updateCrash(crash.getParticleB() ,ParticlesList.getParticles().get(i), actualTime);
            }
            for(Solid solid : Solid.values()){
                CrashList.updateCrash(crash.getParticleB(), solid, actualTime);
            }
        }
    }
}
