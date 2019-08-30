/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.classes;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Solenoid;
import com.revrobotics.CANSparkMax;

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

    private DoubleSolenoid m_rearClimb;

    public Double m_deadzone = 0.05;

    public Double tensionSpeed = 0.25;

    public Boolean g_hatcherExtended = false;
    public Boolean g_hatcherGrabbing = false;

    public Double g_armRaisePower = 0.0;
    public Double g_armLowerPower = 0.0;

    public Double g_topIntakePower = 0.0;
    public Double g_lowerIntakePower = 0.0;

    /**
     * This initializes all of the motors and base settings for the robot subsystems
     * @param ClimbFront The motor for the Talon front climber
     * @param ClimbBack The motor for the Talon back climber
     * @param ClimbDrive The Talon drive motor on the climber
     * @param Lift The CANSparkMax Lift Motor
     * @param Intake The intake motor
     * @param Hatcher The solenoid for the Hatcher system
     */
    public SubSystems(Talon ArmRaise, Talon ArmLower, Talon LowerIntakeRoller, CANSparkMax TopIntakeRoller, Solenoid HatcherGrab, Solenoid HatcherExtend, DoubleSolenoid RearClimb)
    {
        m_armRaise = ArmRaise;
        m_armLower = ArmLower;

        m_lowerIntakeRoller = LowerIntakeRoller;
        m_topIntakeRoller = TopIntakeRoller;
        
        m_hatcherGrab = HatcherGrab;
        m_hatcherExtend = HatcherExtend;

        m_rearClimb = RearClimb;
       }

    

    //#region State Managers
    public void subsystemsStateManager(String systemInput, String stateInput)
    {
        subsystemsStateManager(systemInput, stateInput, null);
    }

    public void subsystemsStateManager(String systemInput, String stateInput, Double speed)
    {
        switch (systemInput) {
            case "hatchIntake":
                switch (stateInput) {
                    case "Extend":
                        hatchMechanismStateManager("Extend");
                        break;

                    case "Retract":
                        hatchMechanismStateManager("Retract");
                
                    case "Grab":
                        hatchMechanismStateManager("Grab");

                    case "Release":
                        hatchMechanismStateManager("Release");
                    
                    default:
                        printError("No state found - " + stateInput + " - for Hatch intake in subsystems manager.");
                        break;
                }
                break;
        
            case "cargoIntake":
                switch (stateInput) {
                    case "In":
                        intakeStateManager("In");
                        break;

                    case "Out":
                        intakeStateManager("Out");
                        break;
                    
                    case "Hold":
                        intakeStateManager("Hold");
                        break;

                    case "Stop":
                        intakeStateManager("Stop");
                        break;

                    case "climbPull":
                        intakeStateManager("climbPull");
                        break;
                
                    default:
                        printError("No state found - " + stateInput + " - for Cargo Intake in subsystems manager.");
                        break;
                }
                break;

            case "cargoArm":
                switch (stateInput) {
                    case "manuallyRaise":
                        armStateManager("manuallyRaise", speed);
                        break;

                    case "manuallyLower":
                        armStateManager("manuallyLower", speed);
                        break;
                    
                    case "scoreRocket":
                        armStateManager("scoreRocket");
                        break;

                    case "scoreCargo":
                        armStateManager("scoreCargo");
                        break;

                    case "intakeFromGround":
                        armStateManager("intakeFromGround");
                        break;

                    case "storeUpright":
                        armStateManager("storeUpright");
                        break;

                    case "stop":
                        armStateManager("stop");
                        break;
                
                    default:
                        armStateManager("stop");
                        printError("No state found - " + stateInput + " - for Cargo Intake in subsystems manager.");
                        break;
                }
                break;

            case "climber":
                switch (stateInput) {
                    case "Up":
                        climbStateManager("Up");
                        break;

                    case "Down":
                        climbStateManager("Down");
                        break;
                    
                    case "Stop":
                        climbStateManager("Stop");
                        break;
                
                    default:
                        climbStateManager("Stop");
                        printError("No state found - " + stateInput + " - for climb in subsystems manager.");
                        break;
                }
                break;

            default:
                printError("There is no SubSystem by that name.");
                break;
        }
    }

    public void intakeStateManager(String input)
    {
        switch (input) {
            case "In":
                intakeCargo();
                break;
        
            case "Out":
                outtakeCargo();
                break;
            
            case "Hold":
                holdCargo();
                break;

            case "Stop":
                stopIntake();
                break;

            case "climbPull":
                climbPull();
                break;

            default:
                printError("Invalid input for intake state manager - Stopping intake");
                stopIntake();
                break;
        }
    }

    public void hatchMechanismStateManager(String input)
    {
        switch (input) {
            case "Extend":
                hatcherExtend();
                break;

            case "Retract":
                hatcherRetract();
                break;
                
            case "Grab":
                hatcherGrab();
                break;
                
            case "Release":
                hatcherRelease();
                break;

            default:
                printError("Illegal value for hatchMechanismStateManager - " + input);
                hatcherRelease();
                hatcherRetract();
                break;
        }
    }

    public void armStateManager(String input)
        { armStateManager(input, null); }
    public void armStateManager(String input, Double speed)
    {
        if(speed == null)
        {
            switch (input) {
                case "scoreRocket":
                    
                    break;
            
                case "scoreCargo":
                    
                    break;
            
                case "intakeFromGround":
                    
                    break;
    
                case "storeUpright":
                    
                    break;
    
                case "stop":
                    stopArm();
                    break;
    
                 default:
                    printError("Invalid case for arm state manager - Stopping Arm");
                    stopArm();
                    break;
            }
        }
        else
        {
            switch (input) {
                case "manuallyRaise":
                    raiseArm(speed);
                    break;
            
                case "manuallyLower":
                    lowerArm(speed);
                    break;

                 default:
                    stopArm();
                    break;
            }
        }
    }

    public void climbStateManager(String input)
    {
        switch (input) {
            case "Up":
                raiseClimber();
                break;

            case "Down":
                lowerClimber();
                break;

            case "Stop":
                stopClimber();
                break;
        
            default:
                break;
        }
    }
    //#endregion

    //#region Hatch Panel Actions
    public void hatcherExtend()
    {
        g_hatcherExtended = true;
    }

    public void hatcherRetract()
    {
        g_hatcherExtended = false;
    }

    public void hatcherGrab()
    {
        g_hatcherGrabbing = true;
    }

    public void hatcherRelease()
    {
        g_hatcherGrabbing = false;
    }
    //#endregion

    //#region Cargo Arm Actions
    public void raiseArm(Double speed)
    {
        g_armRaisePower = speed;
        g_armLowerPower = -speed * tensionSpeed;
    }

    public void lowerArm(Double speed)
    {
        g_armRaisePower = -speed * tensionSpeed;
        g_armLowerPower = speed;
    }

    public void stopArm()
    {
        g_armRaisePower = 0.0;
        g_armLowerPower = 0.0;
    }
    //#endregion

    //#region Intake Actions
    public void intakeCargo()
    {
        g_lowerIntakePower = 1.0;
        g_topIntakePower = .75;
    }

    public void outtakeCargo()
    {
        g_lowerIntakePower = -1.0;
        g_topIntakePower = -1.0;
    }

    public void holdCargo()
    {
        g_lowerIntakePower = -0.1;
        g_topIntakePower = -0.1;
    }

    public void stopIntake()
    {
        g_lowerIntakePower = 0.0;
        g_topIntakePower = 0.0;
    }

    public void climbPull()
    {
        g_lowerIntakePower = 0.0;
    }
    //#endregion

    //#region Climb Actions
    public void raiseClimber()
    {
        m_rearClimb.set(Value.kForward);
    }

    public void lowerClimber()
    {
        m_rearClimb.set(Value.kReverse);
    }

    public void stopClimber()
    {
        m_rearClimb.set(Value.kOff);
    }
    //#endregion

    public void updatePhysicalState()
    {
        if(g_hatcherExtended) { m_hatcherExtend.set(true); } else { m_hatcherExtend.set(false); }
        if(g_hatcherGrabbing) { m_hatcherGrab.set(true);   } else { m_hatcherGrab.set(false);   }

        m_armRaise.set(g_armRaisePower);
        m_armLower.set(g_armLowerPower);

        m_topIntakeRoller.set(g_topIntakePower);
        m_lowerIntakeRoller.set(g_lowerIntakePower);
    }

    public void printError(String error)
    {
        DriverStation.reportError(error, false);
    }
}
