package frc.robot.shooter.commands;

import frc.demacia.utils.mechanisms.ShooterCommand;
import frc.demacia.utils.motors.MotorInterface.ControlMode;
import frc.robot.shooter.ShooterConstants.Config;
import frc.robot.shooter.ShooterConstants.Hardware;
import frc.robot.shooter.subsystems.Shooter;

public class ShooterAutoFire extends ShooterCommand {
    private final Shooter shooter;

    public ShooterAutoFire(Shooter shooter) {
        super(shooter, 
            new ControlMode[] { 
                ControlMode.MOTION,   
                ControlMode.VELOCITY, 
                ControlMode.VELOCITY
            }, 
            () -> false
        );
        this.shooter = shooter;
    }

    @Override
    public void execute() {
        super.execute();
        if (shooter.isReadyToShoot()) {
            shooter.setPower(Hardware.FEEDER_MOTOR_NAME, Config.FEEDER_POWER);
        } else {
            shooter.stop(Hardware.FEEDER_MOTOR_NAME);
        }
    }
}