// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.DriveCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.drive.*;
import frc.robot.subsystems.drive.arcade.*;
import frc.robot.subsystems.intake.IntakeChassis;
import frc.robot.subsystems.intake.IntakeInputs;
import frc.robot.subsystems.intake.IntakeSubsystem;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.robot.usercontrol.HOTASJoystick;
import edu.wpi.first.wpilibj2.command.*;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.AutonCommand;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {

  // HOTAS Flight Stick
  private HOTASJoystick flightStick = new HOTASJoystick(0); // change to correct port in driver station
  private HOTASJoystick joystick2 = new HOTASJoystick(1);
/*
  private DriveSubsystem driveSubsystem = new TankDriveSubsystem(
    new TankDriveChassis( // tank chassis as opposed to arcade or mecanum
      new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless), // first number corresponds with device id - may change
      new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless),
      new CANSparkMax(1, CANSparkMaxLowLevel.MotorType.kBrushless),
      new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless)
      ),
    new TankDriveInputs(flightStick::getX, flightStick::getY)); // x and y of
*/
    private DriveSubsystem driveSubsystem = new ArcadeDriveSubsystem(
    new ArcadeDriveChassis( // arcade chassis as opposed to tank or mecanum
      new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless), // front right
      new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless), // front left
      new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless), // back right
      new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless) // back left
      ),
    new ArcadeDriveInputs(flightStick::getX, flightStick::getY, flightStick::getPOV)); // x and y of

  private IntakeSubsystem intakeSubsystem = new IntakeSubsystem(
          new IntakeChassis(
                  new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless),
                  new CANSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless)
          ),
          new IntakeInputs(
                  () -> {
                      double power = joystick2.getRawAxis(3) - 0.5;
                      if (power == 0) {
                          return IntakeInputs.MoveStatus.STOP;
                      } else if (power > 0) {
                          return IntakeInputs.MoveStatus.FRONT;
                      } else {
                        return IntakeInputs.MoveStatus.BACK;
                      }
                  },
                  () -> {
                     if (joystick2.getRawButton(3)) {
                      return IntakeInputs.MoveStatus.FRONT;
                    } else if (joystick2.getRawButton(4)){
                      return IntakeInputs.MoveStatus.BACK;
                    } else {
                       return IntakeInputs.MoveStatus.STOP;
                     }
                  }
          )
  );

  private DriveCommand driveCommand = new DriveCommand(driveSubsystem, intakeSubsystem); // issue the drive commands from the drive subsystem
  // eventually make an auton command

  /*
  private IntakeSubsystem intakeSubsystem = new IntakeSubsystem(
    new IntakeChassis(
            new CANSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless),
            new CANSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless),
            new CANSparkMax(8, CANSparkMaxLowLevel.MotorType.kBrushless),
            new CANSparkMax(9, CANSparkMaxLowLevel.MotorType.kBrushless)
    )
);
*/


  private AutonCommand autonCommand = new AutonCommand(driveSubsystem);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    //configureBindings(ahrs);

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
  private void configureBindings(AHRS ahrs) {
    // Schedule commands tied to buttons

    // for each button number (corresponds to a button), we are going to run a command / method

    // Reset the ahrs when button 3 on flight stick is pressed (TODO)
    new JoystickButton(flightStick, 3).toggleOnTrue(new InstantCommand(ahrs::reset));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public DriveSubsystem getDriveSubsystem() {
    return driveSubsystem;
  }

  public HOTASJoystick getGamepad() {
    return flightStick;
  }

  /**
  * Use this to pass the teleop command to the main {@link Robot} class.
  *
  * @return the command to run in teleop
  */
  public DriveCommand getDriveCommand()
  {
    // driveCommand will run in teleop
    return driveCommand;
  }

  public AutonCommand getAutonCommand() {
    // autonCommand will run in autonomous
    return autonCommand;
}
}
