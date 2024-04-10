import java.util.*;

public class CrashList {

    private Set<Crash> crashList;

    public CrashList() {
        this.crashList = new TreeSet<>(Comparator.comparingDouble(Crash::getTime));
    }

    public Crash nextCrash() {
        Crash nextCrash = this.crashList.iterator().next();
        this.crashList.remove(nextCrash);
        return nextCrash;
    }

    public void updateCrash(Particle particleA, Particle particleB) {
        // Eliminar el anterior
        Crash prevCrash = particleA.getCrash(particleB);
        this.crashList.remove(prevCrash);

        // Poner el nuevo
        Crash newCrash = new Crash(particleA, particleB);
        addCrash(newCrash);
        particleA.updateCrash(particleB, newCrash);
        particleB.updateCrash(particleA, newCrash);
    }

    public void updateCrash(Particle particleA, Solid solid){
        // Eliminar el anterior
        Crash prevCrash = particleA.getCrash(solid);
        this.crashList.remove(prevCrash);

        // Poner el nuevo
        Crash newCrash = new Crash(particleA, solid);
        addCrash(newCrash);
        particleA.updateCrash(solid, newCrash);
    }

    public void addCrash(Crash crash){
        if(crash.getParticleA().isOverlap(crash.getParticleB())){
            return;
        }
        this.crashList.add(crash);
    }

}
