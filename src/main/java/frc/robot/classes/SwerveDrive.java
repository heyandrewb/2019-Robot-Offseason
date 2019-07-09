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
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

/**
 * This class is used to run swerve drive.
 */
public class SwerveDrive {

    // Create TalonSRX Speed Controllers
    private TalonSRX m_SrxFrontLeftSteering;
    private TalonSRX m_SrxFrontRightSteering;
    private TalonSRX m_SrxBackLeftSteering;
    private TalonSRX m_SrxBackRightSteering;

    // Create SpeedControllers
    private VictorSPX m_FrontLeftDrive;
    private VictorSPX m_FrontRightDrive;
    private VictorSPX m_BackLeftDrive;
    private VictorSPX m_BackRightDrive;

    // Create Variables
    public double frontRightAngle = 0;
    public double frontLeftAngle = 0;
    public double backLeftAngle = 0;
    public double backRightAngle = 0;

    public double frontRight360Angle = 0;
    public double frontLeft360Angle = 0;
    public double backLeft360Angle = 0;
    public double backRight360Angle = 0;

    public double frontLeftTargetPosition = 0;
    public double frontRightTargetPosition = 0;
    public double backLeftTargetPosition = 0;
    public double backRightTargetPosition = 0;

    public double frontLeftCurrentPosition = 0;
    public double frontRightCurrentPosition = 0;
    public double backLeftCurrentPosition = 0;
    public double backRightCurrentPosition = 0;

    public double frontRightSpeed = 0; 
    public double frontLeftSpeed = 0;
    public double backLeftSpeed = 0;
    public double backRightSpeed = 0;

    public int frontLeftOffset = -762;
    public int frontRightOffset = -931;
    public int backLeftOffset = -222;
    public int backRightOffset = -74;

    public double FWD = 0;
    public double STR = 0;
    public double RCW = 0;

    public double defaultDeadzone = 0.1;

    public boolean isZero = false;

    public double degreesToRadians = Math.PI/180.00;
    public double degreesToTicks = 1024.0/360.0;
    public double ticksToDegrees = 360.0/1024.0;

    /**
     * Configures the drivebase with Steering TalonSRXs and Speed Controller Drives
     * @param motorFrontLeftSteering The front left steering TalonSRX
     * @param motorFrontRightSteering The front right steering TalonSRX
     * @param motorBackLeftSteering The back left steering TalonSRX
     * @param motorBackRightSteering The back right steering TalonSRX
     * 
     * @param motorFrontLeftDrive The front left drive speed controller
     * @param motorFrontRightDrive The front right drive speed controller
     * @param motorBackLeftDrive The back left drive speed controller
     * @param motorBackRightDrive The back right drive speed controller
     * 
     * @param defaultDeadzone The default for Switchboard deadzone value
     */
    public SwerveDrive(
    TalonSRX motorFrontLeftSteering, TalonSRX motorFrontRightSteering,
    TalonSRX motorBackLeftSteering, TalonSRX motorBackRightSteering,
    VictorSPX FrontLeftDrive, VictorSPX FrontRightDrive,
    VictorSPX BackLeftDrive, VictorSPX BackRightDrive)
    {
        m_SrxFrontLeftSteering = motorFrontLeftSteering;
        m_SrxFrontRightSteering = motorFrontRightSteering;
        m_SrxBackLeftSteering = motorBackLeftSteering;
        m_SrxBackRightSteering = motorBackRightSteering;

        m_FrontLeftDrive = FrontLeftDrive;
        m_FrontRightDrive = FrontRightDrive;
        m_BackLeftDrive = BackLeftDrive;
        m_BackRightDrive = BackRightDrive;

        // Configure Feedback Sensors for Drive
    m_SrxFrontLeftSteering.configSelectedFeedbackSensor(FeedbackDevice.Analog, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    m_SrxFrontRightSteering.configSelectedFeedbackSensor(FeedbackDevice.Analog, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    m_SrxBackLeftSteering.configSelectedFeedbackSensor(FeedbackDevice.Analog, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    m_SrxBackRightSteering.configSelectedFeedbackSensor(FeedbackDevice.Analog, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

    //Tell the talon to not report if the sensor is out of Phase
    m_SrxFrontLeftSteering.setSensorPhase(Constants.kSensorPhase);
    m_SrxFrontRightSteering.setSensorPhase(Constants.kSensorPhase);
    m_SrxBackLeftSteering.setSensorPhase(Constants.kSensorPhase);
    m_SrxBackRightSteering.setSensorPhase(Constants.kSensorPhase);

    /*
		 * Set based on what direction you want forward/positive to be.
		 * This does not affect sensor phase. 
		 */ 
    m_SrxFrontLeftSteering.setInverted(Constants.kMotorInvert);
    m_SrxFrontRightSteering.setInverted(Constants.kMotorInvert);
    m_SrxBackLeftSteering.setInverted(Constants.kMotorInvert);
    m_SrxBackRightSteering.setInverted(Constants.kMotorInvert);

    /* Config the peak and nominal outputs, 12V means full */
      //Front Left
        m_SrxFrontLeftSteering.configNominalOutputForward(0, Constants.kTimeoutMs);
        m_SrxFrontLeftSteering.configNominalOutputReverse(0, Constants.kTimeoutMs);
        m_SrxFrontLeftSteering.configPeakOutputForward(1, Constants.kTimeoutMs);
        m_SrxFrontLeftSteering.configPeakOutputReverse(-1, Constants.kTimeoutMs);
      //Front Right
        m_SrxFrontRightSteering.configNominalOutputForward(0, Constants.kTimeoutMs);
        m_SrxFrontRightSteering.configNominalOutputReverse(0, Constants.kTimeoutMs);
        m_SrxFrontRightSteering.configPeakOutputForward(1, Constants.kTimeoutMs);
        m_SrxFrontRightSteering.configPeakOutputReverse(-1, Constants.kTimeoutMs);
      //Back Left
        m_SrxBackLeftSteering.configNominalOutputForward(0, Constants.kTimeoutMs);
        m_SrxBackLeftSteering.configNominalOutputReverse(0, Constants.kTimeoutMs);
        m_SrxBackLeftSteering.configPeakOutputForward(1, Constants.kTimeoutMs);
        m_SrxBackLeftSteering.configPeakOutputReverse(-1, Constants.kTimeoutMs);
      //Back Right
        m_SrxBackRightSteering.configNominalOutputForward(0, Constants.kTimeoutMs);
        m_SrxBackRightSteering.configNominalOutputReverse(0, Constants.kTimeoutMs);
        m_SrxBackRightSteering.configPeakOutputForward(1, Constants.kTimeoutMs);
        m_SrxBackRightSteering.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    //Configure the amount of allowable error in the loop
    m_SrxFrontLeftSteering.configAllowableClosedloopError(Constants.kAlloweedError, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    m_SrxFrontRightSteering.configAllowableClosedloopError(Constants.kAlloweedError, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    m_SrxBackLeftSteering.configAllowableClosedloopError(Constants.kAlloweedError, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    m_SrxBackRightSteering.configAllowableClosedloopError(Constants.kAlloweedError, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

    //Configure the overloop behavior of the encoders
    m_SrxFrontLeftSteering.configFeedbackNotContinuous(Constants.kNonContinuousFeedback, Constants.kTimeoutMs);
    m_SrxFrontRightSteering.configFeedbackNotContinuous(Constants.kNonContinuousFeedback, Constants.kTimeoutMs);
    m_SrxBackLeftSteering.configFeedbackNotContinuous(Constants.kNonContinuousFeedback, Constants.kTimeoutMs);
    m_SrxBackRightSteering.configFeedbackNotContinuous(Constants.kNonContinuousFeedback, Constants.kTimeoutMs);

    /* Config Position Closed Loop gains in slot0, typically kF stays zero. */
      //Front Left
        m_SrxFrontLeftSteering.config_kF(Constants.kPIDLoopIdx, Constants.kGains.kF, Constants.kTimeoutMs);
        m_SrxFrontLeftSteering.config_kP(Constants.kPIDLoopIdx, Constants.kGains.kP, Constants.kTimeoutMs);
        m_SrxFrontLeftSteering.config_kI(Constants.kPIDLoopIdx, Constants.kGains.kI, Constants.kTimeoutMs);
        m_SrxFrontLeftSteering.config_kD(Constants.kPIDLoopIdx, Constants.kGains.kD, Constants.kTimeoutMs);
      //Front Right
        m_SrxFrontRightSteering.config_kF(Constants.kPIDLoopIdx, Constants.kGains.kF, Constants.kTimeoutMs);
        m_SrxFrontRightSteering.config_kP(Constants.kPIDLoopIdx, Constants.kGains.kP, Constants.kTimeoutMs);
        m_SrxFrontRightSteering.config_kI(Constants.kPIDLoopIdx, Constants.kGains.kI, Constants.kTimeoutMs);
        m_SrxFrontRightSteering.config_kD(Constants.kPIDLoopIdx, Constants.kGains.kD, Constants.kTimeoutMs);
      //Back Left
        m_SrxBackLeftSteering.config_kF(Constants.kPIDLoopIdx, Constants.kGains.kF, Constants.kTimeoutMs);
        m_SrxBackLeftSteering.config_kP(Constants.kPIDLoopIdx, Constants.kGains.kP, Constants.kTimeoutMs);
        m_SrxBackLeftSteering.config_kI(Constants.kPIDLoopIdx, Constants.kGains.kI, Constants.kTimeoutMs);
        m_SrxBackLeftSteering.config_kD(Constants.kPIDLoopIdx, Constants.kGains.kD, Constants.kTimeoutMs);
      //Back Right
        m_SrxBackRightSteering.config_kF(Constants.kPIDLoopIdx, Constants.kGains.kF, Constants.kTimeoutMs);
        m_SrxBackRightSteering.config_kP(Constants.kPIDLoopIdx, Constants.kGains.kP, Constants.kTimeoutMs);
        m_SrxBackRightSteering.config_kI(Constants.kPIDLoopIdx, Constants.kGains.kI, Constants.kTimeoutMs);
        m_SrxBackRightSteering.config_kD(Constants.kPIDLoopIdx, Constants.kGains.kD, Constants.kTimeoutMs);
    }

    /**
     * Drives the Robot
     * 
     * FWD = Forward
     * STR = Strafe Right
     * RCW = Rotate Clockwise
     * 
     * Steering Angles:
     * -180 to +180 measured clockwise 
     * with 0 being straight ahead
     * 
     * @param RCW_Joystick The left joystick X value.
     * @param FWD_Joystick The right joystick Y value.
     * @param STR_Joystick The right joystick X value.
     * @param fieldCentric Changes steering angles between robot and field centric
     * @param gyroAngle 0 to 360 clockwise, 0 being straight down field
     * @param wheelBase Measuement of wheelbase (Same units as trackwidth)
     * @param trackWidth Measurement of trackwidth (Same units as wheelbase)
     * Note: Units for wheelbase and trackwidth don't matter 
     * so long as they are the same, it is simply a ratio that is calculated from them
     */
    public void drive(double RCW_Joystick, double FWD_Joystick, double STR_Joystick, double wheelBase, double trackWidth, boolean fieldCentric, double gyroAngle, int FrontLeftEncoder, int FrontRightEncoder, int BackLeftEncoder, int BackRightEncoder)
    {
        if(-FWD_Joystick > defaultDeadzone || -FWD_Joystick < -defaultDeadzone) {
            FWD = -FWD_Joystick;
        } 
        else {
            FWD = 0;
        }
        if(STR_Joystick > defaultDeadzone || STR_Joystick < -defaultDeadzone) {
            STR = STR_Joystick;
        } else {
            STR = 0;
        }
        if(RCW_Joystick > defaultDeadzone || RCW_Joystick < -defaultDeadzone) {
            RCW = RCW_Joystick;
        } else {
            RCW = 0;
        }

        if(fieldCentric)
        {
            gyroAngle *= degreesToRadians;
            double temp = FWD * Math.cos(gyroAngle) + STR * Math.sin(gyroAngle);
            STR = -FWD * Math.sin(gyroAngle) + STR * Math.cos(gyroAngle);
            FWD = temp;
        }

        double R = Math.sqrt(Math.pow(wheelBase, 2) + Math.pow(trackWidth, 2));

        //Math
        double A = STR - RCW * (wheelBase / R);
        double B = STR + RCW * (wheelBase / R);
        double C = FWD - RCW * (trackWidth / R);
        double D = FWD + RCW * (trackWidth / R);

        //Calculate speeds
        frontRightSpeed = Math.sqrt(Math.pow(B, 2) + Math.pow(C, 2));
        frontLeftSpeed = Math.sqrt(Math.pow(B, 2) + Math.pow(D, 2));
        backLeftSpeed = Math.sqrt(Math.pow(A, 2) + Math.pow(D, 2));
        backRightSpeed = Math.sqrt(Math.pow(A, 2) + Math.pow(C, 2));

        //Normalize speeds to between 0 and 1
        double max = frontRightSpeed;
        if(frontLeftSpeed > max){max = frontLeftSpeed;}
        if(backLeftSpeed > max){max = backLeftSpeed;}
        if(backRightSpeed > max){max = backRightSpeed;}
        
        if(max > 1) 
        {
            frontRightSpeed/=max; 
            frontLeftSpeed/=max; 
            backLeftSpeed/=max; 
            backRightSpeed/=max; 
        }

        frontRightAngle = Math.atan2(B,C) * 180/Math.PI;
        frontLeftAngle = Math.atan2(B,D) * 180/Math.PI;
        backLeftAngle = Math.atan2(A,D) * 180/Math.PI;
        backRightAngle = Math.atan2(A,C) * 180/Math.PI;

        frontRight360Angle = ConvertTo360Angle(frontRightAngle);
        frontLeft360Angle = ConvertTo360Angle(frontLeftAngle);
        backLeft360Angle = ConvertTo360Angle(backLeftAngle);
        backRight360Angle = ConvertTo360Angle(backRightAngle);

        m_FrontLeftDrive.set(ControlMode.PercentOutput, -frontLeftSpeed*.25);
        m_FrontRightDrive.set(ControlMode.PercentOutput, -frontRightSpeed*.25);
        m_BackLeftDrive.set(ControlMode.PercentOutput, backLeftSpeed*.25);
        m_BackRightDrive.set(ControlMode.PercentOutput, -backRightSpeed*.25);

        frontLeftTargetPosition = -1 * ((ConvertAngleToPosition(frontLeft360Angle) + Math.abs(frontLeftOffset)) % 1024);
        frontRightTargetPosition = -1 * ((ConvertAngleToPosition(frontRight360Angle) + Math.abs(frontRightOffset)) % 1024);
        backLeftTargetPosition = -1 * ((ConvertAngleToPosition(backLeft360Angle) + Math.abs(backLeftOffset)) % 1024);
        backRightTargetPosition = -1 * ((ConvertAngleToPosition(backRight360Angle) + Math.abs(backRightOffset)) % 1024);
        
        m_SrxFrontRightSteering.set(ControlMode.Position, frontRightTargetPosition);
        m_SrxFrontLeftSteering.set(ControlMode.Position, frontLeftTargetPosition);
        m_SrxBackLeftSteering.set(ControlMode.Position, backLeftTargetPosition);
        m_SrxBackRightSteering.set(ControlMode.Position, backRightTargetPosition);
    }

    public void DriveInit()
    {
        m_FrontLeftDrive.set(ControlMode.PercentOutput, 0);
        m_FrontRightDrive.set(ControlMode.PercentOutput, 0);
        m_BackLeftDrive.set(ControlMode.PercentOutput, 0);
        m_BackRightDrive.set(ControlMode.PercentOutput, 0);
    }

    public double ConvertTo360Angle(double Angle)
    {
        //Convert from 0 -> -180/180 to 0 -> 360
        //Create a new output that is in the correct format
        double AdjustedAngle;

        if(Angle < 0)
        {
            AdjustedAngle = 360 + Angle;
        }
        else
        {
            AdjustedAngle = Angle;
        }
        
        return AdjustedAngle;
    }

    public double ConvertAngleToPosition(double Angle)
    {
        double TargetPosition = (1024.0/360.0) * Angle;
       
        return TargetPosition;
    }

    public void driveZero()
    {
        m_SrxFrontRightSteering.set(ControlMode.Position, frontRightTargetPosition);
        m_SrxFrontLeftSteering.set(ControlMode.Position, frontLeftTargetPosition);
        m_SrxBackLeftSteering.set(ControlMode.Position, backLeftTargetPosition);
        m_SrxBackRightSteering.set(ControlMode.Position, backRightTargetPosition);
    }

    /**
     * Converts your input angle 180 -> -180 to a 0 - 360 angle
     * 
     * @param inputAngle Value betweekn 180 -> -180
     * @return Value from 0 -> 360
     */
    public double ShuffleBoardAngleConversion(double inputAngle)
    {
        //Convert from 0 -> -180/180 to 0 -> 360
        //Create a new output that is in the correct format
        double ShuffleboardAdjustedAngle;
        if(inputAngle < 0)
        {
            ShuffleboardAdjustedAngle = 180 + (180 + inputAngle);
        }
        else
        {
            ShuffleboardAdjustedAngle = inputAngle;
        }

        return ShuffleboardAdjustedAngle;
    }

}
