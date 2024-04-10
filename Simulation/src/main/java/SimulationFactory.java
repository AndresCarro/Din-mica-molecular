
import java.util.ArrayList;
import java.util.List;

public class SimulationFactory {
    private final ParticlesList ParticlesList;
    private final CrashList CrashList;

    public SimulationFactory(double L, int N, double radius, double speed, double mass) {
        ParticlesList = new ParticlesList();
        CrashList = new CrashList();
        createParticles(N, L, radius, speed, mass);
        setTimes();
    }

    public void simulate(double maxTime){
        Crash crash = CrashList.nextCrash();
        double prevTime = 0;
        double actualTime = crash.getTime();

        while( actualTime < maxTime){
            for(Particle particle : ParticlesList.getParticles()){
                particle.move(actualTime - prevTime);
            }

            crash.makeCrash();
            changeCrashes(crash);

            prevTime = actualTime;
            crash = CrashList.nextCrash();
            actualTime = crash.getTime();
        }
    }

    private void createParticles(int N, double L, double radius, double speed, double mass) {
        for (int i = 0; i < N; i++) {
            Particle auxParticle = new Particle(L, speed, radius, mass,N);
            while (!this.ParticlesList.insertNewParticle(auxParticle)) {
                auxParticle = new Particle(L, speed, radius, mass,N);
            }
        }
    }

    private void setTimes() {
        for(int i=0; i<ParticlesList.getParticles().size(); i++){
            for(int j=i; j<ParticlesList.getParticles().size(); j++){
                CrashList.updateCrash(ParticlesList.getParticles().get(i), ParticlesList.getParticles().get(j));
            }
            for(Solid solid : Solid.values()){
                CrashList.updateCrash(ParticlesList.getParticles().get(i), solid);
            }
        }

    }

    // Cambio los choques asociados a las particulas que chocaron
    public void changeCrashes(Crash crash){
        for(int i=0; i<ParticlesList.getParticles().size(); i++){
            CrashList.updateCrash(crash.getParticleA(), ParticlesList.getParticles().get(i));
        }
        for(Solid solid : Solid.values()){
            CrashList.updateCrash(crash.getParticleA(), solid);
        }

        if(crash.getSolid() == Solid.NONE){
            for(int i=0; i<ParticlesList.getParticles().size(); i++){
                CrashList.updateCrash(crash.getParticleB() ,ParticlesList.getParticles().get(i));
            }
            for(Solid solid : Solid.values()){
                CrashList.updateCrash(crash.getParticleB(), solid);
            }
        }
    }
}
