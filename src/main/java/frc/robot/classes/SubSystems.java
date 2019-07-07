/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.classes;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;

/**
 * This class runs the subsystems for the 2019 game robot.
 */
public class SubSystems {

    // Create Talon Speed Controllers
    private Talon m_climbFront;
    private Talon m_climbBack;
    private Talon m_climbDrive;

    // Create Spark Speed Controller
    private Spark m_lift;

    // Create Talon Intake Controller
    private Talon m_intake;

    // Create Solenoid
    private Solenoid m_hatcher;
    private Solenoid m_hatcherDrop;
    private Solenoid m_hatcherLift;

    // Create Configurable Values
    public NetworkTableEntry m_deadzone;
    public NetworkTableEntry m_climbOffset;
    public NetworkTableEntry m_encoderValue;

    /**
     * This initializes all of the motors and base settings for the robot subsystems
     * @param ClimbFront The motor for the Talon front climber
     * @param ClimbBack The motor for the Talon back climber
     * @param ClimbDrive The Talon drive motor on the climber
     * @param Lift The CANSparkMax Lift Motor
     * @param Intake The intake motor
     * @param Hatcher The solenoid for the Hatcher system
     * @param defaultDeadzone The default for Switchboard deadzone value
     */
    public SubSystems(Talon ClimbFront, Talon ClimbBack, Talon ClimbDrive, Spark Lift, Talon Intake, Solenoid Hatcher, Solenoid HatcherDrop, Solenoid HatcherLift, double defaultDeadzone, double defaultClimbOffset)
    {
        m_climbFront = ClimbFront;
        m_climbBack = ClimbBack;
        m_climbDrive = ClimbDrive;
        m_lift = Lift;
        m_intake = Intake;
        m_hatcher = Hatcher;
        m_hatcherDrop = HatcherDrop;
        m_hatcherLift = HatcherLift;

        m_deadzone = Shuffleboard.getTab("SubSystems").add("Joystick Deadzone", defaultDeadzone).withWidget(BuiltInWidgets.kNumberSlider).withPosition(2, 1).withSize(2, 1).getEntry();
        m_climbOffset = Shuffleboard.getTab("SubSystems").add("Climb Offset", defaultClimbOffset).withWidget(BuiltInWidgets.kNumberSlider).withPosition(2, 2).withSize(2, 1).getEntry();
        m_encoderValue = Shuffleboard.getTab("SubSystems").add("Encoder Value", 0).withWidget(BuiltInWidgets.kTextView).withPosition(2, 4).withSize(2, 3).getEntry();
    }

    /**
     * This runs the climb motors
     * @param joystickLeftY The left joystick value
     * @param joystickRightY The right joystick value
     * @param joystickDriveY The drive joystick value
     */
    public void climber(double joystickLeftY, double joystickRightY, double joystickDriveY, boolean joystickButton)
    {
        // Impliment Deadzone
        if(joystickLeftY < m_deadzone.getDouble(.02) && joystickLeftY > -m_deadzone.getDouble(.02))
        {
            joystickLeftY = 0;
        }
        if(joystickRightY < m_deadzone.getDouble(.02) && joystickRightY > -m_deadzone.getDouble(.02))
        {
            joystickRightY = 0;
        }
        if(joystickDriveY < m_deadzone.getDouble(.02) && joystickDriveY > -m_deadzone.getDouble(.02))
        {
            joystickDriveY = 0;
        }

        // Square joystick values
        double updatedLeft = joystickLeftY * Math.abs(joystickLeftY);
        double updatedRight = joystickRightY * Math.abs(joystickRightY);
        double updatedDrive = joystickDriveY * Math.abs(joystickDriveY);

        if(joystickButton)
        {
            // Set front values
            m_climbFront.set(updatedLeft * m_climbOffset.getDouble(0));
            // Set back values
            m_climbBack.set(updatedLeft);
        }
        else
        {
            // Set front values
            m_climbFront.set(updatedLeft);
            // Set back values
            m_climbBack.set(updatedRight);
        }
        // Set drive values
        m_climbDrive.set(updatedDrive);
    }

    /**
     * Runs the lift motors
     * @param joystickY The lift joystick value
     */
    public void lift(double joystickY, double encoderValue)
    {
        // Impliment Deadzone
        if(joystickY < m_deadzone.getDouble(.02) && joystickY > -m_deadzone.getDouble(.02))
        {
            joystickY = 0;
        }

        // Square joystick values
        double updatedY = joystickY * Math.abs(joystickY);

        // Set motor value
        m_lift.set(updatedY);

        m_encoderValue.setDouble(encoderValue);
    }

    /**
     * Runs the intake motor
     * @param trigerLeft The trigger to intake
     * @param triggerRight The trigger to output
     */
    public void intake(double triggerLeft, double triggerRight)
    {
        if(triggerLeft > m_deadzone.getDouble(.02))
        {
            m_intake.set(-triggerLeft);
        }
        else if(triggerRight > m_deadzone.getDouble(.02))
        {
            m_intake.set(triggerRight);
        }
        else
        {
            m_intake.set(0);
        }
    }

    /**
     * Runs the hatcher solenoid
     * @param buttonKick The button to activate the kicker solenoid
     * @param button
     */
    public void hatcher(Boolean buttonKick, Boolean buttonDrop, Boolean buttonLift)
    {
      m_hatcher.set(buttonKick);
      m_hatcherDrop.set(buttonDrop);
      m_hatcherLift.set(buttonLift);
    }

    /**
     * Zeros all climb motors
     */
    public void climbZero()
    {
    m_climbFront.set(0);
    m_climbBack.set(0);
    m_climbDrive.set(0);
    }

    /**
     * Zeros all lift motors
     */
    public void liftZero()
    {
        m_lift.set(0);
    }

    /**
     * Zeros intake motor
     */
    public void intakeZero()
    {
        m_intake.set(0);
    }

    /**
     * Zeros all hatcher solenoids
     */
    public void hatcherZero()
    {
        m_hatcher.set(false);
        m_hatcherDrop.set(false);
        m_hatcherLift.set(false);
    }
}
