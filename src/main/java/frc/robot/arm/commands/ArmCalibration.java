package frc.robot.arm.commands;

import frc.demacia.utils.mechanisms.CalibratinCommand;
import frc.robot.arm.ArmConstants.TelescopeConstants;
import frc.robot.arm.subsystems.Arm;

/**
 * Calibration command for the Telescope mechanism.
 * Drives the motor towards the limit switch and resets the encoder position.
 */
public class ArmCalibration extends CalibratinCommand {
    /**
     * Creates a new ArmCalibration command.
     * @param arm The Arm subsystem to calibrate.
     */
    public ArmCalibration(Arm arm) {
        super(
            arm, 
            TelescopeConstants.MOTOR_NAME, 
            TelescopeConstants.CalibrationConstants.CALIBRATION_POWER,
            () -> arm.isAtBottom(),
            TelescopeConstants.CalibrationConstants.RESET_POSITION,
            TelescopeConstants.CalibrationConstants.START_POWER,
            TelescopeConstants.CalibrationConstants.START_TIME_SEC
        );
    }
}