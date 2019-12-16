package frc.robot.classes;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;

public class Vision {
    static NetworkTableInstance visionTable = NetworkTableInstance.getDefault();
    static NetworkTable myCam = visionTable.getTable("chameleon-vision").getSubTable("MyCamName");

    private static NetworkTableEntry entryPitch = myCam.getEntry("pitch");
    private static NetworkTableEntry entryYaw = myCam.getEntry("yaw");
    private static NetworkTableEntry entryPipeline = myCam.getEntry("pipeline");
    private static NetworkTableEntry entryTimestamp = myCam.getEntry("timestamp");
    private static NetworkTableEntry entryDriverMode = myCam.getEntry("driver_mode");
    private static NetworkTableEntry entryIsValid = myCam.getEntry("is_valid");

    private static double valuePitch;
    private static double valueYaw;
    private static double valuePipeline = 0;
    private static double valueTimestamp;
    private static boolean valueDriverMode = false;
    private static boolean valueIsValid;

    private static void updateGetValues() {
        valuePitch = entryPitch.getDouble(0.0);
        valueYaw = entryYaw.getDouble(0.0);
        valueTimestamp = entryTimestamp.getDouble(0.0);
        valueIsValid = entryIsValid.getBoolean(false);
    }

    private static void updateSetValue() {
        entryPipeline.setDouble(valuePipeline);
        entryDriverMode.setBoolean(valueDriverMode);
    }

    // #region getters
    public static double getPitch() {
        updateGetValues();
        return valuePitch;
    }

    public static double getYaw() {
        updateGetValues();
        return valueYaw;
    }

    public static double getTimestamp() {
        updateGetValues();
        return valueTimestamp;
    }

    public static boolean getIsValid() {
        updateGetValues();
        return valueIsValid;
    }

    public static double getTurnValue() {
        return getStrafeValue();
    }

    public static double getStrafeValue() {
        double strafeValue = getYaw()/50;
        return strafeValue;
    }

    public static double getDriveValue() {
        double driveValue = getPitch();
        return driveValue;
    }

    public static boolean isLinedUp(double pitchDeadzone, double yawDeadzone) {
        boolean isLinedUp = false;
        boolean isPitchGood = false;
        boolean isYawGood = false;
        if(getPitch() < pitchDeadzone/2.0 && getPitch() < pitchDeadzone/-2.0)
        {
            isPitchGood = true;
        }
        if(getYaw() < yawDeadzone/2.0 && getYaw() < yawDeadzone/-2.0)
        {
            isYawGood = true;
        }
        if(isYawGood && isPitchGood)
        {
            isLinedUp = true;
        }
        return isLinedUp;
    }
    // #endregion

    //#region setters
    public static void setPipeline(double pipeline) {
        valuePipeline = pipeline;
        updateSetValue();
    }

    public static void setDriverMode(boolean driverMode) {
        valueDriverMode = driverMode;
        updateSetValue();
    }
    //#endregion
}