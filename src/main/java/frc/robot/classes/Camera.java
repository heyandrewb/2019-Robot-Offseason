/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.classes;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * The Class to run the cameras
 */
public class Camera {
    // Create Camera
    UsbCamera m_camera;

    /**
     * Initializes a USB Camera
     */
    public Camera()
    {
        // Initialize CameraServer
        CameraServer.getInstance().startAutomaticCapture();
    }
}
