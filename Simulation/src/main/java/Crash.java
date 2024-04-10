public class Crash {
    private final double time;
    private final Particle particleA;
    private final Particle particleB;
    private final Solid solid;

    public Crash(Particle particleA, Particle particleB) {
        this.particleA = particleA;
        this.particleB = particleB;
        this.solid = Solid.NONE;
        this.time = calculateTime(particleA, particleB);
    }

    public Crash(Particle particleA, Solid solid) {
        this.particleA = particleA;
        this.particleB = null;
        this.solid = solid;
        this.time = calculateTime(particleA, solid);
    }

    public void makeCrash(){
        // Cambiar las velocidades de las particulas segun el choque
        switch (this.solid) {
            // Choque entre particulas
            case NONE -> particlesCrash();
            // Choque con borde derecho
            case RIGHT -> particleA.setAngle(particleA.getAngle() + (Math.PI) / 2);
            // Choque con borde izquierdo
            case LEFT -> particleA.setAngle(particleA.getAngle() + (Math.PI) / 2);
            // Choque con borde superior
            case UP -> particleA.setAngle(particleA.getAngle() + (Math.PI) / 2);
            // Choque con borde inferior
            case DOWN -> particleA.setAngle(particleA.getAngle() + (Math.PI) / 2 - Math.PI * 2);
            // Choque con centro
            case CENTER -> centerCrash();
        }
    }

    public void particlesCrash(){

    }

    public void centerCrash(){

    }

    public double calculateTime(Particle particleA, Particle particleB){
        // Tiempo en chocar ocn otra particula
        return 10;
    }

    public double calculateTime(Particle particleA, Solid solid){
        // Tiempo en chocar con un borde
        return 11;
    }

    public double getTime() {
        return time;
    }

    public Particle getParticleA() {
        return particleA;
    }

    public Particle getParticleB() {
        return particleB;
    }

    public Solid getSolid() {
        return solid;
    }
}
