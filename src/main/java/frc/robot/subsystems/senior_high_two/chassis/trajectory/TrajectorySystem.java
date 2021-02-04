/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems.senior_high_two.chassis.trajectory;

import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj2.command.Subsystem;

/**
 * 一個預防忘記該實作哪些方法的類別，可以在每個程式中重複使用
 */
public interface TrajectorySystem extends Subsystem {
    /**
     * Provide kinematics object, contain track width
     * 
     * @return kinematics
     */
    public DifferentialDriveKinematics getKinematics();

    /**
     * encoder velocity to chassis speed
     * 
     * @return current chassis speed
     */
    default public DifferentialDriveWheelSpeeds getSpeed(){
        System.out.println("default getSpeed method");
        return null;
    }

    /**
     * Provide feedforward controller
     * 
     * @return feedForward controlller 
     */
    default public SimpleMotorFeedforward getFeedforward(){
        System.out.println("default getFeedforawrd method");
        return null;
    }

    /**
     * Provide PID controller
     * 
     * @return left PID controller
     */
    default public PIDController getLeftPidController(){
        System.out.println("default getLeftPidController method");
        return null;
    }

    /**
     * Provide PID controller
     * 
     * @return right PID controller
     */
    default public PIDController getRightPidController(){
        System.out.println("default getRightPidController method");
        return null;
    }

    /**
     * Returns the currently-estimated pose of the robot.
     *
     * @return The pose.
     */
    public Pose2d getPose();

    /**
     * set odmetry,let the starting position 
     * of the robot be the starting point of the trajectory
     * 
     * @param pose2d the trajectory origin
     */
    public void setOdometry(Pose2d pose2d);

    /**
     * Returns left velocity
     * @return
     */
    public double getLeftVelocity();

    /**
     * Returns left position
     * @return
     */
    public double getLeftPosition();

    /**
     * Returns right velocity
     * @return
     */
    public double getRightVelocity();

    /**
     * Returns right position
     * @return
     */
    public double getRightPosition();

    /**
     * get "X" from odmetry
     * 
     * @return current "X"
     */
    public double getX();

    /**
     * get "Y" from odmetry
     * 
     * @return current "Y"
     */
    public double getY();

    /**
     * Use velocity to set output.
     * @param left
     * @param right
     */
    default public void setOutput(double left, double right){
        System.out.println("default speed output method");
    }
    /**
     * Use voltage to set output.
     * @param left
     * @param right
     */

    default public void voltage(double left, double right){
        System.out.println("default voltage output method");
    }

    /**
     * Returns the heading of the robot.
     *
     * @return the robot's heading 
     */
    public Rotation2d getHeading();

    /**
     * Show message
     */
    default public void message(){
        System.out.println("default message method");
    }

    public void resetSensor();
}
