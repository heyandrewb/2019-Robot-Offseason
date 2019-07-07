/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.classes;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
/**
 * This class is used to run basic tank drive.
 */
public class TankDrive {

    // Create TalonSRX Speed Controllers
    private TalonSRX m_SrxLeft;
    private TalonSRX m_SrxRight;

    // Create SpeedControllers
    private SpeedController m_leftFront;
    private SpeedController m_leftBack;
    private SpeedController m_rightFront;
    private SpeedController m_rightBack;

    // Create Variables
    private boolean isFour;
    private boolean isSRX;
    
    // Create Configurable Values
    public NetworkTableEntry m_deadzone;

    /**
     * Configures the drivebase with 2 TalonSRXs
     * @param motorLeft The left TalonSRX
     * @param motorRight The right TalonSRX
     * @param defaultDeadzone The default for Switchboard deadzone value
     */
    public TankDrive(TalonSRX motorLeft, TalonSRX motorRight, double defaultDeadzone)
    {
        m_SrxLeft = motorLeft;
        m_SrxRight = motorRight;
        isFour = false;
        isSRX = true;

        m_deadzone = Shuffleboard.getTab("TankDrive").add("Joystick Deadzone", defaultDeadzone).withWidget("Number Slider").withPosition(1, 1).withSize(2, 1).getEntry();
    }

    /**
     * Configures the drivebase with 2 SpeedControllers
     * @param motorLeft The left SpeedControllers
     * @param motorRight The right SpeedControllers
     */
    public TankDrive(SpeedController motorLeft, SpeedController motorRight)
    {
        m_leftFront = motorLeft;
        m_rightFront = motorRight;
        isFour = false;
        isSRX = false;
    }

    /**
     * Configures the drivebase with 2 SpeedControllers
     * @param motorLeftFront The left front SpeedControllers
     * @param motorLeftBack The left back SpeedControllers
     * @param motorRightFront The right front SpeedControllers
     * @param motorRightBack The right back SpeedControllers
     */
    public TankDrive(SpeedController motorLeftFront, SpeedController motorLeftBack, SpeedController motorRightFront, SpeedController motorRightBack)
    {
        m_leftFront = motorLeftFront;
        m_leftBack = motorLeftBack;
        m_rightFront = motorRightFront;
        m_rightBack = motorRightBack;
        isFour = true;
        isSRX = false;
    }

    /**
     * Drives the Robot using a squared tankdrive.
     * 
     * @param joystickLeftY The left joystick Y value.
     * @param joystickRightY The right joystick Y value.
     * @param speed The percentage speed from 0 to 1 to drive the robot.
     */
    public void drive(double joystickLeftY, double joystickRightY, double speed)
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

        // Square joystick values
        double updatedLeft = joystickLeftY * Math.abs(joystickLeftY);
        double updatedRight = joystickRightY * Math.abs(joystickRightY);

        // Set Motor Values
        if(isSRX && !isFour)
        {
            // Set left values
            m_SrxLeft.set(ControlMode.PercentOutput, updatedLeft);
            // Set right values
            m_SrxRight.set(ControlMode.PercentOutput, updatedRight);
        }
        else if(!isSRX && !isFour)
        {
            // Set left values
            m_leftFront.set(updatedLeft);
            // Set right values
            m_rightFront.set(updatedRight);
        }
        else if(!isSRX && isFour)
        {
            // Set left values
            m_leftFront.set(updatedLeft);
            m_leftBack.set(updatedLeft);
            // Set right values
            m_rightFront.set(updatedRight);
            m_rightBack.set(updatedRight);
        }
    }
}
