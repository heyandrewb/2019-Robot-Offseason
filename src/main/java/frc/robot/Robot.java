/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.classes.SwerveDrive;
import frc.robot.classes.SwerveGyroAdapter;

public class Robot extends TimedRobot {

  // Create Joysticks
  Joystick m_joystickLeft;
  Joystick m_joystickRight;
  Joystick m_gamepad;

  // Create Drive Motors
  TalonSRX m_FrontLeftSteering;
  TalonSRX m_FrontRightSteering;
  TalonSRX m_BackLeftSteering;
  TalonSRX m_BackRightSteering;
  VictorSPX m_FrontLeftDrive;
  VictorSPX m_FrontRightDrive;
  VictorSPX m_BackLeftDrive;
  VictorSPX m_BackRightDrive;

  // Create the PDP
  PowerDistributionPanel m_powerDistributionPanel;

  // Create the Gyro
  AHRS ahrs;

  //Create Custom Classes
  SwerveDrive m_drive;

  //Create Variables
  double trackWidth = 20.625;
  double wheelBase = 20.625;
  boolean fieldCentric = false;
  double gyroAngle = 0;
  boolean shuffleboardRecording = false;

  //Create Shuffleboard Widgets for Angles
  private static SwerveGyroAdapter frontLeftGyroAdapter = new SwerveGyroAdapter();
  private static SwerveGyroAdapter frontRightGyroAdapter = new SwerveGyroAdapter();
  private static SwerveGyroAdapter backLeftGyroAdapter = new SwerveGyroAdapter();
  private static SwerveGyroAdapter backRightGyroAdapter = new SwerveGyroAdapter();

  //Create Shuffleboard Tabs
  private static ShuffleboardTab SwerveTab = Shuffleboard.getTab("Swerve");
  private static ShuffleboardTab Joysticks = Shuffleboard.getTab("Joysticks");
  private static ShuffleboardTab SwerveEncoders = Shuffleboard.getTab("SwerveEncoders");
  private static ShuffleboardTab SubSystems = Shuffleboard.getTab("SubSystems");

  //#region NetworkEntries
  //Create Network Table Entries
  static NetworkTableEntry networkTableEntryJoystickX = Joysticks
                                .add("Joystick X", 0)
                                .withWidget(BuiltInWidgets.kNumberBar)
                                .getEntry();

  static NetworkTableEntry networkTableEntryJoystickY = Joysticks
                                .add("Joystick Y", 0)
                                .withWidget(BuiltInWidgets.kNumberBar)
                                .getEntry();

  static NetworkTableEntry networkTableEntryJoystickZ = Joysticks
                                .add("Joystick Z", 0)
                                .withWidget(BuiltInWidgets.kNumberBar)
                                .getEntry();

  static NetworkTableEntry networkTableEntryFWD = Joysticks
                                .add("FWD", 0)
                                .withWidget(BuiltInWidgets.kNumberBar)
                                .getEntry();

  static NetworkTableEntry networkTableEntryRCW = Joysticks
                                .add("RCW", 0)
                                .withWidget(BuiltInWidgets.kNumberBar)
                                .getEntry();

  static NetworkTableEntry networkTableEntrySTR = Joysticks
                                .add("STR", 0)
                                .withWidget(BuiltInWidgets.kNumberBar)
                                .getEntry();  
  
  static NetworkTableEntry networkTableEntryFrontLeftSpeed = SwerveTab
                                .add("FL Speed", 0)
                                .withWidget(BuiltInWidgets.kVoltageView)
                                .withProperties(Map.of("Min", 0, "Max", 1, "Center", 0, "Orientation", "VERTICAL"))
                                .withPosition(1, 0)
                                .withSize(2, 5)
                                .getEntry();  
  
  static NetworkTableEntry networkTableEntryFrontRightSpeed = SwerveTab
                                .add("FR Speed", 0)
                                .withWidget(BuiltInWidgets.kVoltageView)
                                .withProperties(Map.of("Min", 0, "Max", 1, "Center", 0, "Orientation", "VERTICAL"))
                                .withPosition(14, 0)
                                .withSize(2, 5)
                                .getEntry();

  static NetworkTableEntry networkTableEntryBackLeftSpeed = SwerveTab
                                .add("BL Speed", 0)
                                .withWidget(BuiltInWidgets.kVoltageView)
                                .withProperties(Map.of("Min", 0, "Max", 1, "Center", 0, "Orientation", "VERTICAL"))
                                .withPosition(1, 5)
                                .withSize(2, 5)
                                .getEntry();

  static NetworkTableEntry networkTableEntryBackRightSpeed = SwerveTab
                                .add("BR Speed", 0)
                                .withWidget(BuiltInWidgets.kVoltageView)
                                .withProperties(Map.of("Min", 0, "Max", 1, "Center", 0, "Orientation", "VERTICAL"))
                                .withPosition(14, 5)
                                .withSize(2, 5)
                                .getEntry();

  static NetworkTableEntry networkTableEntryFrontLeftEncoderActual = SwerveEncoders
                                .add("FL Encoder Actual", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(0, 0)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry networkTableEntryFrontRightEncoderActual = SwerveEncoders
                                .add("FR Encoder Actual", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(2, 0)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry networkTableEntryBackLeftEncoderActual = SwerveEncoders
                                .add("BL Encoder Actual", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(0, 1)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry networkTableEntryBackRightEncoderActual = SwerveEncoders
                                .add("BR Encoder Actual", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(2, 1)
                                .withSize(2, 1)
                                .getEntry();

  static NetworkTableEntry networkTableEntryFrontLeftEncoderTarget = SwerveEncoders
                                .add("FL Encoder Target", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(5, 0)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry networkTableEntryFrontRightEncoderTarget = SwerveEncoders
                                .add("FR Encoder Target", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(7, 0)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry networkTableEntryBackLeftEncoderTarget = SwerveEncoders
                                .add("BL Encoder Target", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(5, 1)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry networkTableEntryBackRightEncoderTarget = SwerveEncoders
                                .add("BR Encoder Target", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(7, 1)
                                .withSize(2, 1)
                                .getEntry();

  static NetworkTableEntry frontLeftEncoderDifference = SwerveEncoders
                                .add("FL Encoder Difference", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(10, 0)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry frontRightEncoderDifference = SwerveEncoders
                                .add("FR Encoder Difference", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(12, 0)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry backLeftEncoderDifference = SwerveEncoders
                                .add("BL Encoder Difference", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(10, 1)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry backRightEncoderDifference = SwerveEncoders
                                .add("BR Encoder Difference", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(12, 1)
                                .withSize(2, 1)
                                .getEntry();

  static NetworkTableEntry frontLeftAngle = SwerveEncoders
                                .add("FL Angle", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(0, 3)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry frontRightAngle = SwerveEncoders
                                .add("FR Angle", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(2, 3)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry backLeftAngle = SwerveEncoders
                                .add("BL Angle", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(0, 4)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry backRightAngle = SwerveEncoders
                                .add("BR Angle", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(2, 4)
                                .withSize(2, 1)
                                .getEntry();
                                
  static NetworkTableEntry frontLeft360Angle = SwerveEncoders
                                .add("FL 360 Angle", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(4, 3)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry frontRight360Angle = SwerveEncoders
                                .add("FR 360 Angle", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(6, 3)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry backLeft360Angle = SwerveEncoders
                                .add("BL 360 Angle", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(4, 4)
                                .withSize(2, 1)
                                .getEntry();
  
  static NetworkTableEntry backRight360Angle = SwerveEncoders
                                .add("BR 360 Angle", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .withPosition(6, 4)
                                .withSize(2, 1)
                                .getEntry();

  static NetworkTableEntry ShuffleboardLog = SwerveEncoders
                                .add("ShuffleboardLog", "")
                                .withWidget(BuiltInWidgets.kTextView)
                                .withSize(4, 2)
                                .withPosition(0, 6)
                                .getEntry();

  static NetworkTableEntry shuffleboardGyroYaw = SubSystems
                                .add("Gyro - Yaw", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .getEntry();  
  static NetworkTableEntry shuffleboardGyroCompass = SubSystems
                                .add("Gyro - Compass Heading", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .getEntry();  
  static NetworkTableEntry shuffleboardGyroFused = SubSystems
                                .add("Gyro - Fused Heading", 0)
                                .withWidget(BuiltInWidgets.kTextView)
                                .getEntry();
                                

  static String ShuffleboardLogString;
  //#endregion

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {

    // Initialize Joysticks
    m_joystickLeft = new Joystick(0);
    m_joystickRight = new Joystick(1);
    m_gamepad = new Joystick(2);

    // Initialize Drive Motors
    m_FrontLeftSteering = new TalonSRX(1);
    m_FrontRightSteering = new TalonSRX(3);
    m_BackLeftSteering = new TalonSRX(2);
    m_BackRightSteering = new TalonSRX(0);
    m_FrontLeftDrive = new VictorSPX(7);
    m_FrontRightDrive = new VictorSPX(4);
    m_BackLeftDrive = new VictorSPX(5);
    m_BackRightDrive = new VictorSPX(6);

    //Initialize Power Distribution Panel
    m_powerDistributionPanel = new PowerDistributionPanel();

    Shuffleboard.getTab("PDP")
      .add("PDP", m_powerDistributionPanel)
      .withWidget(BuiltInWidgets.kPowerDistributionPanel)
      .withPosition(0, 0)
      .withSize(17, 10);

    try {
      ahrs = new AHRS(SPI.Port.kMXP); 
      ahrs.zeroYaw();
    } catch (Exception ex) {
      DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
    }

    // Initialize Custom Classes
    m_drive = new SwerveDrive(m_FrontLeftSteering, m_FrontRightSteering, m_BackLeftSteering, m_BackRightSteering, m_FrontLeftDrive, m_FrontRightDrive, m_BackLeftDrive, m_BackRightDrive);

    //Get shuffleboard tab
    SwerveTab.add("Front Left Swerve Angle Gyro", frontLeftGyroAdapter)
             .withProperties(Map.of("Starting angle", 0))
             .withPosition(3, 0)
             .withSize(5, 5);
    SwerveTab.add("Front Right Swerve Angle Gyro", frontRightGyroAdapter)
             .withProperties(Map.of("Starting angle", 0))
             .withPosition(9, 0)
             .withSize(5, 5);
    SwerveTab.add("Back Left Swerve Angle Gyro", backLeftGyroAdapter)
             .withProperties(Map.of("Starting angle", 0))
             .withPosition(3, 5)
             .withSize(5, 5);
    SwerveTab.add("Back Right Swerve Angle Gyro", backRightGyroAdapter)
             .withProperties(Map.of("Starting angle", 0))
             .withPosition(9, 5)
             .withSize(5, 5);

  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   */
  @Override
  public void robotPeriodic() {
    //m_pixy.arduinoRead();
    if(!shuffleboardRecording && isEnabled())
    {
      Shuffleboard.startRecording();
      shuffleboardRecording = true;
    }
    else if(shuffleboardRecording && isDisabled())
    {
      Shuffleboard.stopRecording();
      shuffleboardRecording = false;
    }

    networkTableEntryFrontLeftEncoderActual.setDouble(m_FrontLeftSteering.getSelectedSensorPosition());
    networkTableEntryFrontRightEncoderActual.setDouble(m_FrontRightSteering.getSelectedSensorPosition());
    networkTableEntryBackLeftEncoderActual.setDouble(m_BackLeftSteering.getSelectedSensorPosition());
    networkTableEntryBackRightEncoderActual.setDouble(m_BackRightSteering.getSelectedSensorPosition());

    networkTableEntryFrontLeftEncoderTarget.setDouble(m_drive.frontLeftTargetPosition);
    networkTableEntryFrontRightEncoderTarget.setDouble(m_drive.frontRightTargetPosition);
    networkTableEntryBackLeftEncoderTarget.setDouble(m_drive.backLeftTargetPosition);
    networkTableEntryBackRightEncoderTarget.setDouble(m_drive.backRightTargetPosition);

    frontLeftAngle.setDouble(m_drive.frontLeftAngle);
    frontRightAngle.setDouble(m_drive.frontRightAngle);
    backLeftAngle.setDouble(m_drive.backLeftAngle);
    backRightAngle.setDouble(m_drive.backRightAngle);

    frontLeft360Angle.setDouble(m_drive.frontLeft360Angle);
    frontRight360Angle.setDouble(m_drive.frontRight360Angle);
    backLeft360Angle.setDouble(m_drive.backLeft360Angle);
    backRight360Angle.setDouble(m_drive.backRight360Angle);

    frontLeftEncoderDifference.setDouble(m_FrontLeftSteering.getSelectedSensorPosition() - m_drive.frontLeftTargetPosition);
    frontRightEncoderDifference.setDouble(m_FrontRightSteering.getSelectedSensorPosition() - m_drive.frontRightTargetPosition);
    backLeftEncoderDifference.setDouble(m_BackLeftSteering.getSelectedSensorPosition() - m_drive.backLeftTargetPosition);
    backRightEncoderDifference.setDouble(m_BackRightSteering.getSelectedSensorPosition() - m_drive.backRightTargetPosition);

    networkTableEntryJoystickX.setDouble(m_joystickLeft.getRawAxis(0));
    networkTableEntryJoystickY.setDouble(m_joystickLeft.getRawAxis(1));
    networkTableEntryJoystickZ.setDouble(m_joystickRight.getRawAxis(0));

    networkTableEntryFWD.setDouble(m_drive.FWD);
    networkTableEntryRCW.setDouble(m_drive.RCW);
    networkTableEntrySTR.setDouble(m_drive.STR);
  
    shuffleboardGyroFused.setDouble(ahrs.getFusedHeading());
    shuffleboardGyroYaw.setDouble(ahrs.getYaw());
  }

  /**
   * This function is called when autonomous is first started.
   */
  @Override
  public void autonomousInit() {
    // m_BackLeftSteering.configFactoryDefault();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    //TODO: Uncomment for gyro
    try {
      // Get gryo angle
      gyroAngle = ahrs.getFusedHeading();
      //fieldCentric is true only if you can recieve a gyro angle
      fieldCentric = true;
    } catch (Exception e) {
      DriverStation.reportError("Couldn't recieve gyro angle:" + e, true);
      DriverStation.reportError("Moving to robotCentric Controlls", true);
      gyroAngle = 0;
    }
  
    // Run Drive
    //if you change joysticks or want to pull different Axis change them here
    double strafe = m_joystickLeft.getRawAxis(0);
    double forward = m_joystickLeft.getRawAxis(1);
    double rotateClockwise = m_joystickRight.getRawAxis(0);

    shuffleboardGyroCompass.setDouble(gyroAngle);

    if(m_joystickLeft.getRawButton(1))
    {
      fieldCentric = true;
    }
    else
    {
      fieldCentric = false;
    }
    m_drive.drive(
      rotateClockwise, 
      forward, 
      strafe, 
      wheelBase, 
      trackWidth, 
      fieldCentric, 
      gyroAngle, 
      m_FrontLeftSteering.getSelectedSensorPosition(), 
      m_FrontRightSteering.getSelectedSensorPosition(), 
      m_BackLeftSteering.getSelectedSensorPosition(), 
      m_BackRightSteering.getSelectedSensorPosition());
  
    //using a Gyro Widget on the Shuffleboard, have to convert our angle 180 -> -180 to 0 - 360
    //then put the converted angle into the Gyro Adapter widget.
    frontLeftGyroAdapter.setAngle(m_drive.ShuffleBoardAngleConversion(m_drive.frontLeftAngle));
    frontRightGyroAdapter.setAngle(m_drive.ShuffleBoardAngleConversion(m_drive.frontRightAngle));
    backLeftGyroAdapter.setAngle(m_drive.ShuffleBoardAngleConversion(m_drive.backLeftAngle));
    backRightGyroAdapter.setAngle(m_drive.ShuffleBoardAngleConversion(m_drive.backRightAngle));

    networkTableEntryFrontLeftSpeed.setDouble(m_drive.frontLeftSpeed);
    networkTableEntryFrontRightSpeed.setDouble(m_drive.frontRightSpeed);
    networkTableEntryBackLeftSpeed.setDouble(m_drive.backLeftSpeed);
    networkTableEntryBackRightSpeed.setDouble(m_drive.backRightSpeed);
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}

