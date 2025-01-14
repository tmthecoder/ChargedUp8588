/****
 * Made by Tejas Mehta
 * Made on Monday, March 29, 2021
 * File Name: DriveCommand
 * Package: frc.team8588.commands*/
package frc.robot.commands;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.drive.DriveDirection;
import frc.robot.subsystems.drive.DriveSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;

public class DriveCommand extends CommandBase {

    private DriveSubsystem subsystem;
    private IntakeSubsystem intake;
    double power = 0.0;
    DriveDirection direction = DriveDirection.FORWARD;
    double lPower = 0.0;
    double rPower = 0.0;
    public DriveCommand(DriveSubsystem subsystem, IntakeSubsystem intake) {
        this.subsystem = subsystem;
        this.intake = intake;
        addRequirements(subsystem, intake);
    }

    @Override
    public void execute() {
        subsystem.setPowers();
        intake.setPowers();
    }

    public void execute(AHRS ahrs) {subsystem.setPowersFO(ahrs);}

    public void setPower(double power) {
        this.power = power;
    }

    public void setLeft(double power) {
        lPower = power;
    }

    public void setRight(double power){
        rPower = power;
    }

    public void setPower(double speed, DriveDirection direction) {
        this.direction = direction;
        this.power = speed;
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
