public class Crash {
    private final double CENTER_RADIUS = 0.5;
    private final double CENTER_MASS = 3;
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
        this.particleB = new Particle(particleA.getL(), CENTER_MASS, CENTER_RADIUS );
        this.solid = solid;
        if(solid == Solid.NONE){
            this.time = Double.POSITIVE_INFINITY;
        }else{
            this.time = actualTime + calculateTime();
        }
    }

    public void makeCrash(){
        // Cambiar las velocidades de las particulas segun el choque
        switch (this.solid) {
            // Choque entre particulas
            case NONE -> particlesCrash();
            // Choque con borde derecho
            case RIGHT -> particleA.setAngle(Math.PI - particleA.getAngle());
            // Choque con borde izquierdo
            case LEFT -> particleA.setAngle(Math.PI*3 - particleA.getAngle());
            // Choque con borde superior
            case UP -> particleA.setAngle(2*Math.PI - particleA.getAngle());
            // Choque con borde inferior
            case DOWN -> particleA.setAngle(2*Math.PI - particleA.getAngle());
            // Choque con centro
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

        double newVx = ( (-1) * Math.cos(alpha) * Math.cos(alpha) + Math.sin(alpha) * Math.sin(alpha) ) * AVx + (-1) * Math.sin(alpha) * Math.cos(alpha) * AVy;
        double newVy = ( (-1) * Math.sin(alpha) * Math.cos(alpha)) * AVx + ((-1) * Math.sin(alpha) * Math.sin(alpha) + Math.cos(alpha) * Math.cos(alpha)) * AVy;

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
        if(productoPunto(deltaV(), deltaR()) >= 0){
            return Double.POSITIVE_INFINITY;
        }
        return (-1) * (productoPunto(deltaV(), deltaR()) + Math.pow(d(), 0.5)) / productoPunto(deltaV(), deltaV());
    }


    public  double J(){
        return (2 * particleA.getMass() * particleB.getMass() * productoPunto(deltaV(), deltaR())) / (sigma() * (particleA.getMass() + particleB.getMass()));
    }

    public double Jx(){
        return J() * deltaR()[0] / sigma();
    }

    public double Jy(){
        return J() * deltaR()[1] / sigma();
    }

    public double[] deltaR(){
        return new double[]{particleA.getX() - particleB.getX(), particleA.getY() - particleB.getY()};
    }

    public double[] deltaV() {
        return new double[]{
                particleA.getSpeed() * Math.cos(particleA.getAngle()) - particleB.getSpeed() * Math.cos(particleB.getAngle()),
                particleA.getSpeed() * Math.sin(particleA.getAngle()) - particleB.getSpeed() * Math.sin(particleB.getAngle())
        };
    }

    public double sigma(){
        return particleA.getRadius() + particleB.getRadius();
    }

    public double d(){
        return Math.pow(productoPunto(deltaV(), deltaR()), 2) - (productoPunto(deltaV(), deltaV()) * (productoPunto(deltaR(), deltaR()) - Math.pow(sigma(), 2)));
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

    public static double productoPunto(double[] vector1, double[] vector2) {
        // Asegurarse de que ambos vectores tengan la misma longitud
        if (vector1.length != vector2.length) {
            throw new IllegalArgumentException("Los vectores deben tener la misma longitud");
        }

        double productoPunto = 0;
        // Calcular el producto punto sumando los productos de los elementos correspondientes
        for (int i = 0; i < vector1.length; i++) {
            productoPunto += vector1[i] * vector2[i];
        }

        return productoPunto;
    }

    public static double[][] productoMatricial(double[][] matriz1, double[][] matriz2) {
        int filas1 = matriz1.length;
        int columnas1 = matriz1[0].length;
        int filas2 = matriz2.length;
        int columnas2 = matriz2[0].length;

        if (columnas1 != filas2) {
            throw new IllegalArgumentException("El número de columnas de la matriz1 debe ser igual al número de filas de la matriz2");
        }

        double[][] resultado = new double[filas1][columnas2];
        for (int i = 0; i < filas1; i++) {
            for (int j = 0; j < columnas2; j++) {
                for (int k = 0; k < columnas1; k++) {
                    resultado[i][j] += matriz1[i][k] * matriz2[k][j];
                }
            }
        }

        return resultado;
    }

}
