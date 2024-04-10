import java.util.List;
import java.util.Objects;

public class Particle {
    private final int id;
    private double x;
    private double y;
    private double speed;
    private double angle;
    private double radius;
    private double mass;
    private Crash[] crashesList;
    private double L;

    public Particle(double L, double mass, double radius){
        this.id = 0;
        this.x = L/2;
        this.y = L/2;
        this.L = L;
        this.speed = 0;
        this.angle = 0;
        this.radius = radius;
        this.mass = mass;
        this.crashesList = null;
    }

    public Particle(double L, double speed, double radius, double mass,int N, int id){
        this.id = id;
        this.x = Math.random() * L;
        this.y = Math.random() * L;
        this.L = L;
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
        return dist < (this.radius + particle.radius);
    }

    public void updateCrash(Particle particle, Crash crash){
        this.crashesList[Solid.values().length + particle.id] = crash;
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

    public double timeToSolid(Solid solid){
        switch (solid) {
            // Choque entre particulas
            case NONE -> System.out.println("No debería estar aca");
            // Choque con borde derecho
            case RIGHT -> {
                double Vx = this.speed * Math.cos(this.angle);
                if(Vx < 0){
                    return Double.POSITIVE_INFINITY;
                }
                return (this.L - this.radius - this.x) / Vx;
            }
            // Choque con borde izquierdo
            case LEFT -> {
                double Vx = this.speed * Math.cos(this.angle);
                if(Vx > 0){
                    return Double.POSITIVE_INFINITY;
                }
                return (-1) * (this.x - this.radius) / Vx;
            }
            // Choque con borde superior
            case UP -> {
                double Vy = this.speed * Math.sin(this.angle);
                if(Vy < 0){
                    return Double.POSITIVE_INFINITY;
                }
                return (this.L - this.radius - this.y) / Vy;
            }
            // Choque con borde inferior
            case DOWN -> {
                double Vy = this.speed * Math.sin(this.angle);
                if(Vy > 0){
                    return Double.POSITIVE_INFINITY;
                }
                return (-1) * (this.y - this.radius) / Vy;
            }
            // Choque con centro
            case CENTER -> System.out.println("No debería estar aca");
        }
        return 0;
    }


    public double getL() {
        return L;
    }
}
