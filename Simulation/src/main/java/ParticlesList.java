import java.util.ArrayList;
import java.util.List;

public class ParticlesList {
    private final List<Particle> particles;

    public ParticlesList() {
        this.particles = new ArrayList<>();
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public boolean addParticle(Particle particle){
        return particles.add(particle);
    }

    public boolean removeParticle(Particle particle){
        return particles.remove(particle);
    }

    public boolean insertNewParticle(Particle newParticle){
        for(Particle particle : particles){
            if(particle.isOverlap(newParticle)){
                return false;
            }
        }
        this.addParticle(newParticle);
        return true;
    }

}
