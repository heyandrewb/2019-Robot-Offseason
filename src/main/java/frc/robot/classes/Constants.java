/*
 * Simple class containing constants used throughout project
 */
package frc.robot.classes;

public class Constants {
	/*
	 * Which PID slot to pull gains from. Starting 2018, you can choose from
	 * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
	 * configuration.
	 */
    public static final int kSlotIdx = 0;

	/*
	 * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
	 * now we just want the primary one.
	 */
	public static final int kPIDLoopIdx = 0;

	/*
	 * Set to zero to skip waiting for confirmation, set to nonzero to wait and
	 * report to DS if action fails.
	 */
	public static final int kTimeoutMs = 30;
	
	/* Choose so that Talon does not report sensor out of phase */
	public static boolean kSensorPhase = true;

	/* Choose if feedback is non-continuous
	true:   1023 -> 0
	false:  1023 -> 1024
	*/
	public static boolean kNonContinuousFeedback = true;

	/*The amount of allowed error in the pid loop*/
	public static int kAlloweedError = 7;

	/*
	 * Choose based on what direction you want to be positive,
	 * this does not affect motor invert. 
	 */
	public static boolean kMotorInvert = false;

	/*
	 * Gains used in Positon Closed Loop, to be adjusted accordingly
     * Gains(kp, ki, kd, kf, izone, peak output);
     */
    public static final Gains kGains = new Gains(20, 0.0, 200, 0.0, 0, 1.0);
}