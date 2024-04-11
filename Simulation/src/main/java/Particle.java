import java.util.List;
import java.util.Objects;

public class Particle {
    private final double CENTER_RADIUS = 0.005;
    private final double CENTER_MASS = 3;
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
        this.id = N-1;
        this.x = L/2;
        this.y = L/2;
        this.L = L;
        this.speed = 0;
        this.angle = 0;
        this.radius = CENTER_RADIUS;
        this.mass = CENTER_MASS;
        this.crashesList = new Crash[Solid.values().length + N];
    }

    public Particle(double L, double speed, double radius, double mass,int N, int id){
        this.id = id;
        this.x = Math.random() * (L-2*radius) + radius;
        this.y = Math.random() * (L-2*radius) + radius;
        this.L = L;
        this.speed = speed;
        this.angle = Math.random() * 2 * Math.PI;
        this.radius = radius;
        this.mass = mass;
        this.N = N;
        this.crashesList = new Crash[Solid.values().length + N];
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
