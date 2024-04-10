import java.util.List;

public class ParticlesList {
    private List<Particle> particles;

    public ParticlesList(List<Particle> particles) {
        this.particles = particles;
    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticles(List<Particle> particles) {
        this.particles = particles;
    }

    public boolean addParticle(Particle particle){
        return particles.add(particle);
    }

    public boolean removeParticle(Particle particle){
        return particles.remove(particle);
    }

}
