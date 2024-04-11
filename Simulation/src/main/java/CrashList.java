import java.util.*;

public class CrashList {

    private final List<Crash> crashList;

    public CrashList() {
        this.crashList = new LinkedList<>();
    }

    public void createCrash(Particle particleA, Particle particleB) {
        Crash newCrash = new Crash(particleA, particleB, 0);
        addCrash(newCrash);
        particleA.updateCrash(particleB, newCrash);
        particleB.updateCrash(particleA, newCrash);
    }

    public void createCrash(Particle particleA, Solid solid){
        Crash newCrash = new Crash(particleA, solid, 0);
        addCrash(newCrash);
        particleA.updateCrash(solid, newCrash);
    }


    public void updateCrash(Particle particleA, Particle particleB, double actualTime) {
        // Eliminar el anterior
        Crash prevCrash = particleA.getCrash(particleB);
        removeCrash(prevCrash);

        // Poner el nuevo
        Crash newCrash = new Crash(particleA, particleB, actualTime);
        addCrash(newCrash);
        particleA.updateCrash(particleB, newCrash);
        particleB.updateCrash(particleA, newCrash);
    }

    public void updateCrash(Particle particleA, Solid solid, double actualTime){
        // Eliminar el anterior
        Crash prevCrash = particleA.getCrash(solid);
        removeCrash(prevCrash);

        // Poner el nuevo
        Crash newCrash = new Crash(particleA, solid, actualTime);
        addCrash(newCrash);
        particleA.updateCrash(solid, newCrash);
    }

    public void addCrash(Crash crash) {
        int index = 0;
        for (Crash c : crashList) {
            if (crash.getTime() < c.getTime()) {
                crashList.add(index, crash);
                return;
            }
            index++;
        }
        crashList.add(crash);
    }

    public Crash nextCrash() {
        Crash nextCrash = this.crashList.iterator().next();
        this.crashList.remove(nextCrash);
        return nextCrash;
    }

    public void removeCrash(Crash crash) {
        crashList.remove(crash);
    }

}
