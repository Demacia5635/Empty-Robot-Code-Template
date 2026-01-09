// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.demacia.utils.DemaciaUtils;
import frc.demacia.utils.chassis.Chassis;
import frc.demacia.utils.controller.CommandController;
import frc.demacia.utils.controller.CommandController.ControllerType;
import frc.demacia.utils.log.LogManager;
import frc.demacia.utils.mechanisms.DriveCommand;
import frc.robot.arm.ArmConstants.AngleChangeConstants;
import frc.robot.arm.ArmConstants.TelescopeConstants;
import frc.robot.arm.commands.ArmCalibration;
import frc.robot.arm.commands.ArmCommand;
import frc.robot.arm.subsystems.Arm;
import frc.robot.chassisConstants.ChassisConstants;
import frc.robot.intake.commands.IntakeToggle;
import frc.robot.intake.commands.OuttakeToggle;
import frc.robot.intake.subsystems.IntakeSubsystem;
import frc.robot.shooter.commands.AngleCalibration;
import frc.robot.shooter.commands.ShooterAutoFire;
import frc.robot.shooter.commands.ShooterStateFire;
import frc.robot.shooter.subsystems.Shooter;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer implements Sendable{

  public static boolean isComp = false;
  private static boolean hasRemovedFromLog = false;
  public static boolean isRed = false;

  // The robot's subsystems and commands are defined here...

  private final Chassis chassis;
  private final frc.demacia.utils.chassis.DriveCommand driveCommand;

  public static CommandController driverController;

  private final Arm arm;
  private final Shooter shooter;
  private final IntakeSubsystem intake;


  // Replace with CommandPS4Controller or CommandJoystick if needed

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    SmartDashboard.putData("RC", this);
    new DemaciaUtils(() -> getIsComp(), () -> getIsRed());

    driverController = new CommandController(0, ControllerType.kXbox);

    chassis = new Chassis(ChassisConstants.CHASSIS_CONFIG);
    driveCommand = new frc.demacia.utils.chassis.DriveCommand(chassis, driverController);

    arm = new Arm();
    shooter = new Shooter();
    intake = new IntakeSubsystem();
    
    // Configure the trigger bindings
    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    chassis.setDefaultCommand(driveCommand);
    arm.setDefaultCommand(new ArmCommand(arm));
    shooter.setDefaultCommand(new ShooterStateFire(shooter));
    
    driverController.upButton().onTrue(new ArmCalibration(arm));
    driverController.downButton().onTrue(new AngleCalibration(shooter));

    driverController.getLeftStickMove().whileTrue(
      new DriveCommand(arm, TelescopeConstants.MOTOR_NAME, () -> driverController.getLeftY() * -0.3)
    );
    
    driverController.getRightStickMove().whileTrue(
      new DriveCommand(arm, AngleChangeConstants.MOTOR_NAME, () -> driverController.getRightY() * 0.3)
    );

    driverController.povDown().whileTrue(new ShooterAutoFire(shooter));

    driverController.rightButton().whileTrue(new IntakeToggle(intake));
    
    driverController.leftBumper().whileTrue(new OuttakeToggle(intake));
  }

  public static boolean getIsRed() {
    return isRed;
  }

  public static void setIsRed(boolean isRed) {
    RobotContainer.isRed = isRed;
  }

  public static boolean getIsComp() {
    return isComp;
  }

  public static void setIsComp(boolean isComp) {
    RobotContainer.isComp = isComp;
    if(!hasRemovedFromLog && isComp) {
      hasRemovedFromLog = true;
      LogManager.removeInComp();
    }
  }

  @Override
  public void initSendable(SendableBuilder builder) {
    builder.addBooleanProperty("isRed", RobotContainer::getIsRed, RobotContainer::setIsRed);
    builder.addBooleanProperty("isComp", RobotContainer::getIsComp, RobotContainer::setIsComp);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }
}