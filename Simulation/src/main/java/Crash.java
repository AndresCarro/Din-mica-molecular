public class Crash {
    private final double time;
    private final Particle particleA;
    private final Particle particleB;
    private final Solid solid;

    public Crash(Particle particleA, Particle particleB, double actualTime) {
        this.particleA = particleA;
        this.particleB = particleB;
        this.solid = Solid.NONE;
        this.time = actualTime + calculateTime();
    }

    public Crash(Particle particleA, Solid solid, double actualTime) {
        this.particleA = particleA;
        this.particleB = new Particle(particleA.getL(), particleA.getN());
        this.solid = solid;
        if(solid == Solid.NONE){
            this.time = Double.POSITIVE_INFINITY;
        }else{
            this.time = actualTime + calculateTime();
        }
    }

    // Cambiar las velocidades despues del choque
    public void makeCrash(){
        switch (this.solid) {
            case NONE -> particlesCrash();
            case RIGHT -> particleA.setAngle(Math.PI - particleA.getAngle());
            case LEFT -> particleA.setAngle(Math.PI*3 - particleA.getAngle());
            case UP -> particleA.setAngle(2*Math.PI - particleA.getAngle());
            case DOWN -> particleA.setAngle(2*Math.PI - particleA.getAngle());
            case CENTER -> centerCrash();
        }
    }

    public void particlesCrash(){
        double AVx = particleA.getSpeed() * Math.cos(particleA.getAngle());
        double AVy = particleA.getSpeed() * Math.sin(particleA.getAngle());
        double BVx = particleB.getSpeed() * Math.cos(particleB.getAngle());
        double BVy = particleB.getSpeed() * Math.sin(particleB.getAngle());

        double newAVx = AVx + Jx() / particleA.getMass();
        double newAVy = AVy + Jy() / particleA.getMass();
        double newBVx = BVx - Jx() / particleB.getMass();
        double newBVy = BVy - Jy() / particleB.getMass();

        // Revisar atan2
        particleA.setSpeed(Math.pow(Math.pow(newAVx, 2) + Math.pow(newAVy, 2), 0.5));
        particleA.setAngle(Math.atan2(newAVy, newAVx));
        particleB.setSpeed(Math.pow(Math.pow(newBVx, 2) + Math.pow(newBVy, 2), 0.5));
        particleB.setAngle(Math.atan2(newBVy, newBVx));
    }

    public void centerCrash(){
        double AVx = particleA.getSpeed() * Math.cos(particleA.getAngle());
        double AVy = particleA.getSpeed() * Math.sin(particleA.getAngle());

        double dx = particleA.getX() - particleB.getX();
        double dy = particleA.getY() - particleB.getY();
        double alpha = Math.atan2(dy, dx);

        double newVx = ( (-1) * Math.cos(alpha) * Math.cos(alpha) + Math.sin(alpha) * Math.sin(alpha) ) * AVx + (-2) * Math.sin(alpha) * Math.cos(alpha) * AVy;
        double newVy = ( (-2) * Math.sin(alpha) * Math.cos(alpha)) * AVx + ((-1) * Math.sin(alpha) * Math.sin(alpha) + Math.cos(alpha) * Math.cos(alpha)) * AVy;

        particleA.setSpeed(Math.pow(Math.pow(newVx, 2) + Math.pow(newVy, 2), 0.5));
        particleA.setAngle(Math.atan2(newVy, newVx));
    }

    public double calculateTime(){
        if(this.solid == Solid.NONE || this.solid == Solid.CENTER){
            return calculateParticleTime();
        }else{
            return particleA.timeToSolid(this.solid);
        }
    }

    public double calculateParticleTime(){
        if (d() < 0) {
            return Double.POSITIVE_INFINITY;
        }
        if(Utils.productoPunto(deltaV(), deltaR()) >= 0){
            return Double.POSITIVE_INFINITY;
        }
        return (-1) * (Utils.productoPunto(deltaV(), deltaR()) + Math.pow(d(), 0.5)) / Utils.productoPunto(deltaV(), deltaV());
    }

    public  double J(){
        return (2 * particleA.getMass() * particleB.getMass() * Utils.productoPunto(deltaV(), deltaR())) / (sigma() * (particleA.getMass() + particleB.getMass()));
    }

    public double Jx(){
        return J() * deltaR()[0] / sigma();
    }

    public double Jy(){
        return J() * deltaR()[1] / sigma();
    }

    public double[] deltaR(){
        return new double[]{particleB.getX() - particleA.getX(), particleB.getY() - particleA.getY()};
    }

    public double[] deltaV() {
        return new double[]{
                particleB.getSpeed() * Math.cos(particleB.getAngle()) - particleA.getSpeed() * Math.cos(particleA.getAngle()),
                particleB.getSpeed() * Math.sin(particleB.getAngle()) - particleA.getSpeed() * Math.sin(particleA.getAngle())
        };
    }

    public double sigma(){
        return particleA.getRadius() + particleB.getRadius();
    }

    public double d(){
        return Math.pow(Utils.productoPunto(deltaV(), deltaR()), 2) - (Utils.productoPunto(deltaV(), deltaV()) * (Utils.productoPunto(deltaR(), deltaR()) - Math.pow(sigma(), 2)));
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
