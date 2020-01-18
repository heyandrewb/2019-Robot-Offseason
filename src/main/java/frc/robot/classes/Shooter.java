/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.classes;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * This class is used to run swerve drive.
 */
public class Shooter {
    // Create TalonSRX Speed Controllers
    private TalonSRX m_TopMotor;
    private TalonSRX m_BottomMotor;

    // Create Variables
    private int ticksPerRevolution = 4084;

    public double topSpeed = 0;
    public double bottomSpeed = 0;

    /**
     * Configures the drivebase with Steering TalonSRXs and Speed Controller Drives
     * 
     * @param topMotor                The TalonSRX for the top motor
     * @param bottomMotor             The TalonSRX for the bottom motor
     */

    public Shooter(TalonSRX topMotor, TalonSRX bottomMotor) {

        m_TopMotor = topMotor;
        m_BottomMotor = bottomMotor;

        // Configure Feedback Sensors for Drive
        m_TopMotor.configSelectedFeedbackSensor(FeedbackDevice.Analog, Constants.kPIDLoopIdx,
                Constants.kTimeoutMs);
        m_BottomMotor.configSelectedFeedbackSensor(FeedbackDevice.Analog, Constants.kPIDLoopIdx,
                Constants.kTimeoutMs);
       
        // Tell the talon to not report if the sensor is out of Phase
        m_TopMotor.setSensorPhase(Constants.kSensorPhase);
        m_BottomMotor.setSensorPhase(Constants.kSensorPhase);
      
        /*
         * Set based on what direction you want forward/positive to be. This does not
         * affect sensor phase.
         */
        m_TopMotor.setInverted(Constants.kMotorInvert);
        m_BottomMotor.setInverted(Constants.kMotorInvert);
       
        /* Config the peak and nominal outputs, 12V means full */
        // Top
        m_TopMotor.configNominalOutputForward(0, Constants.kTimeoutMs);
        m_TopMotor.configNominalOutputReverse(0, Constants.kTimeoutMs);
        m_TopMotor.configPeakOutputForward(1, Constants.kTimeoutMs);
        m_TopMotor.configPeakOutputReverse(-1, Constants.kTimeoutMs);

        // Bottom
        m_BottomMotor.configNominalOutputForward(0, Constants.kTimeoutMs);
        m_BottomMotor.configNominalOutputReverse(0, Constants.kTimeoutMs);
        m_BottomMotor.configPeakOutputForward(1, Constants.kTimeoutMs);
        m_BottomMotor.configPeakOutputReverse(-1, Constants.kTimeoutMs);
      
        // Configure the amount of allowable error in the loop
        m_TopMotor.configAllowableClosedloopError(Constants.kAlloweedError, Constants.kPIDLoopIdx,
                Constants.kTimeoutMs);
        m_BottomMotor.configAllowableClosedloopError(Constants.kAlloweedError, Constants.kPIDLoopIdx,
                Constants.kTimeoutMs);
     

        // Configure the overloop behavior of the encoders
        m_TopMotor.configFeedbackNotContinuous(Constants.kNonContinuousFeedback, Constants.kTimeoutMs);
        m_BottomMotor.configFeedbackNotContinuous(Constants.kNonContinuousFeedback, Constants.kTimeoutMs);

        /* Config Position Closed Loop gains in slot0, typically kF stays zero. */
        // Top
        m_TopMotor.config_kF(Constants.kPIDLoopIdx, Constants.kGains.kF, Constants.kTimeoutMs);
        m_TopMotor.config_kP(Constants.kPIDLoopIdx, Constants.kGains.kP, Constants.kTimeoutMs);
        m_TopMotor.config_kI(Constants.kPIDLoopIdx, Constants.kGains.kI, Constants.kTimeoutMs);
        m_TopMotor.config_kD(Constants.kPIDLoopIdx, Constants.kGains.kD, Constants.kTimeoutMs);

        // Bottom
        m_BottomMotor.config_kF(Constants.kPIDLoopIdx, Constants.kGains.kF, Constants.kTimeoutMs);
        m_BottomMotor.config_kP(Constants.kPIDLoopIdx, Constants.kGains.kP, Constants.kTimeoutMs);
        m_BottomMotor.config_kI(Constants.kPIDLoopIdx, Constants.kGains.kI, Constants.kTimeoutMs);
        m_BottomMotor.config_kD(Constants.kPIDLoopIdx, Constants.kGains.kD, Constants.kTimeoutMs);
    }

    /**
     * Runs the shooter at a set velocity for the top and bottom wheels
     * 
     *      * @param RCW_Joystick The left joystick X value.
     */
    public void velocityShoot(double topVelocity, double bottomVelocity) {
        m_TopMotor.set(ControlMode.Velocity, topVelocity);
        m_BottomMotor.set(ControlMode.Velocity, bottomVelocity);
    }

    public void distanceShoot(double distance) {
        m_TopMotor.set(ControlMode.Velocity, topDistanceToVelocity(distance));
        m_BottomMotor.set(ControlMode.Velocity, bottomDistanceToVelocity(distance));
    }

    public double topDistanceToVelocity(double distance)
    {
        return distance;
    }
    
    public double bottomDistanceToVelocity(double distance)
    {
        return distance;
    }

    public void ShooterKill() {
        m_TopMotor.set(ControlMode.PercentOutput, 0);
        m_BottomMotor.set(ControlMode.PercentOutput, 0);
    }

    public int rpmToNativeUnits(double input)
    {
        int output = ((int)input*ticksPerRevolution)/600;
        return output;
    } 

}
