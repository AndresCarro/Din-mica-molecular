import java.util.List;
import java.util.Objects;

public class Particle {
    private final int id;
    private double x;
    private double y;
    private double speed;
    private double angle;
    private final double radius;
    private final double mass;
    private final Crash[] crashesList;
    private final double L;
    private int N;

    public Particle(double L, int N){
        this.id = N;
        this.x = L/2;
        this.y = L/2;
        this.L = L;
        this.speed = 0;
        this.angle = 0;
        this.radius = Utils.CENTER_RADIUS;
        this.mass = Utils.CENTER_MASS;
        this.crashesList = new Crash[Solid.values().length + N+1];
    }

    public Particle(double L, double speed, int N, int id){
        this.id = id;
        this.mass = Utils.PARTICLE_MASS;
        this.radius = Utils.PARTICLE_RADIUS;
        this.x = Math.random() * (L-2*radius) + radius;
        this.y = Math.random() * (L-2*radius) + radius;
        this.L = L;
        this.speed = speed;
        this.angle = Math.random() * 2 * Math.PI;
        this.N = N;
        this.crashesList = new Crash[Solid.values().length + N+1];
    }

    public void move(double time){
        this.x += this.speed * Math.cos(this.angle) * time;
        this.y += this.speed * Math.sin(this.angle) * time;
    }

    public boolean isOverlap(Particle particle){
        double dist = Math.pow(Math.pow((particle.x-this.x), 2) + Math.pow((particle.y-this.y),2), 0.5);
        return dist < (this.radius + particle.radius);
    }

    public void updateCrash(Particle particle, Crash crash){
        this.crashesList[Solid.values().length + particle.id] = crash;
    }

    public double timeToSolid(Solid solid){
        double Vx = this.speed * Math.cos(this.angle);
        double Vy = this.speed * Math.sin(this.angle);

        switch (solid) {
            case NONE, CENTER -> System.out.println("No debería estar aca!!!");
            case RIGHT -> {
                if(Vx <= 0){
                    return Double.POSITIVE_INFINITY;
                }
                return (this.L - this.radius - this.x) / Vx;
            }
            case LEFT -> {
                if(Vx >= 0){
                    return Double.POSITIVE_INFINITY;
                }
                return (-1) * (this.x - this.radius) / Vx;
            }
            case UP -> {
                if(Vy <= 0){
                    return Double.POSITIVE_INFINITY;
                }
                return (this.L - this.radius - this.y) / Vy;
            }
            case DOWN -> {
                if(Vy >= 0){
                    return Double.POSITIVE_INFINITY;
                }
                return (-1) * (this.y - this.radius) / Vy;
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    public Crash getCrash(Particle particle){
        return this.crashesList[Solid.values().length + particle.id];
    }

    public void updateCrash(Solid solid, Crash crash){
        this.crashesList[solid.ordinal()] = crash;
    }

    public Crash getCrash(Solid solid){
        return this.crashesList[solid.ordinal()];
    }

    public double getMass() {
        return mass;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSpeed() {
        return speed;
    }

    public double getAngle() {
        return angle;
    }

    public double getRadius() {
        return radius;
    }

    public Crash[] getCrashesList() {
        return crashesList;
    }

    public double getL() {
        return L;
    }

    public int getN() {
        return N;
    }
}
