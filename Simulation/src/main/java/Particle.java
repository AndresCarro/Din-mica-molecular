import java.util.List;
import java.util.Objects;

public class Particle {
    private final int id;
    private static int nextId = 0;
    private double x;
    private double y;
    private double speed;
    private double angle;
    private double radius;
    private double mass;
    private Crash[] crashesList;

    public Particle(double L, double speed, double radius, double mass,int N){
        this.id = nextId++;
        this.x = Math.random() * L;
        this.y = Math.random() * L;
        this.speed = speed;
        this.angle = Math.random() * 2 * Math.PI;
        this.radius = radius;
        this.mass = mass;
        this.crashesList = new Crash[Solid.values().length + N];
    }

    public void move(double time){
        this.x += this.speed * Math.cos(this.angle) * time;
        this.y += this.speed * Math.sin(this.angle) * time;
    }

    public boolean isOverlap(Particle particle){
        double dist = Math.pow(Math.pow((particle.x-this.x), 2) + Math.pow((particle.y-this.y),2), 0.5);
        return dist > (this.radius + particle.radius);
    }

    public void updateCrash(Particle particle, Crash crash){
        this.crashesList[Solid.values().length + particle.id] = crash;
    }

    public Crash getCrash(Particle particleA){
        return this.crashesList[Solid.values().length + particleA.id];
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

    public static int getNextId() {
        return nextId;
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
}
