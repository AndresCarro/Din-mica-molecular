import java.io.FileWriter;
import java.io.IOException;

public class SimulationFactory {
    private final ParticlesList ParticlesList;
    private final CrashList CrashList;
    private final String outputFile;
    private final boolean movable;

    public SimulationFactory(double L, int N, double radius, double speed, double mass, boolean movable) {
        ParticlesList = new ParticlesList();
        CrashList = new CrashList();

        this.movable = movable;
        this.outputFile = "Simulation/output/SimulationData_" + N + "_" + (int) L + ".csv";

        createParticles(N, L, radius, speed, mass);
        setTimes();
    }

    public void simulate(double maxTime){
        try{
            FileWriter writer_data = new FileWriter(this.outputFile);
            writer_data.write("id,x,y,vel,angulo,time,crash,ParticleA,ParticleB");

            for(Particle particle : ParticlesList.getParticles()){
                writer_data.write( "\n" + particle.getId() + "," + particle.getX() + "," + particle.getY() + "," + particle.getSpeed() + "," + particle.getAngle() + "," + 0 + "," + "INIT");
            }

            Crash crash = CrashList.nextCrash();
            double prevTime = 0;
            double actualTime = crash.getTime();

            while( actualTime < maxTime){
                for(Particle particle : ParticlesList.getParticles()){
                    particle.move(actualTime - prevTime);
                    writer_data.write("\n" + particle.getId() + "," + particle.getX() + "," + particle.getY() + "," + particle.getSpeed() + "," + particle.getAngle() + "," + actualTime + "," + crash.getSolid().name() + "," + crash.getParticleA().getId() + "," + crash.getParticleB().getId());
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

    private void createParticles(int N, double L, double radius, double speed, double mass) {
        int NewN = N;
        if(this.movable){
            NewN += 1;
        }
        Particle centerParticle = new Particle(L, N);
        this.ParticlesList.insertNewParticle(centerParticle);
        for (int i = 0; i < N; i++) {
            Particle auxParticle = new Particle(L, speed, radius, mass, NewN, i);
            while (!this.ParticlesList.insertNewParticle(auxParticle)) {
                auxParticle = new Particle(L, speed, radius, mass, NewN, i);
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
            if(ParticlesList.getParticles().get(i).isOverlap(crash.getParticleB())){
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
                if(ParticlesList.getParticles().get(i).isOverlap(crash.getParticleA())){
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
}
