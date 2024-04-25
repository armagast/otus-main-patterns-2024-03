package me.blokhin.homework002;

public class CommandRotate {
    private final Rotatable rotatable;

    public CommandRotate(Rotatable rotatable) {
        Assert.notNull(rotatable, "{rotatable} must not be null");

        this.rotatable = rotatable;
    }

    public void execute() {
        final int angleDivisor = rotatable.getAngleDivisor();
        Assert.state(angleDivisor > 0, "{angleDivisor} must be positive");

        final int angle = rotatable.getAngle();
        final int angularVelocity = rotatable.getAngularVelocity();

        final int angleNext = (angle + angularVelocity) % angleDivisor;
        rotatable.setAngle(angleNext);
    }
}
