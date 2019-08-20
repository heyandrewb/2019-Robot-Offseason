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
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

/**
 * This class runs the subsystems for the 2019 game robot.
 */
public class SubSystems {

    // Create Talon Speed Controllers
    private Talon m_armRaise;
    private Talon m_armLower;
    private Talon m_lowerIntakeRoller;

    // Create Spark Max Speed Controller
    private CANSparkMax m_topIntakeRoller;

    // Create Solenoid
    private Solenoid m_hatcherExtend;
    private Solenoid m_hatcherGrab;

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
    public SubSystems(Talon ArmRaise, Talon ArmLower, Talon LowerIntakeRoller, CANSparkMax TopIntakeRoller, Talon Intake, Solenoid Hatcher, Solenoid HatcherDrop, Solenoid HatcherLift, double defaultDeadzone, double defaultClimbOffset)
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
     * Runs the Cargo Arm motors
     * @param joystickY The arm joystick value
     */
    public void moveCargoArm(double joystickY, double encoderValue)
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
     * Zeros all hatcher solenoids
     */
    public void hatcherZero()
    {
        m_hatcherExtend.set(false);
        m_hatcherGrab.set(false);
    }
}
