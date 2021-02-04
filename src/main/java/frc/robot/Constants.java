/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static class Path{
        public static String LeftUp = "output/LeftUp.wpilib.json";
    }
    public static final int kTimesOut = 10;  

    /*馬達腳位*/
    public static final int conveyor         = 3;	
    public static final int Wing_TwoWing     = 1;
    public static final int Wing_middle      = 2;
    public static final int flywheelleft     = 17;
    public static final int flywheelRight    = 15;
    public static final int tower            = 36;
    public static final int racker           = 25;

    /*PDP位址*/
    public static final int Conveyor_PDP        = 5; 
    public static final int Wing_TwoWing_PDP    = 10;
    public static final int Wing_middle_PDP     = 11;
    public static final int Intake_PDP          = 9;
    //public static final int flywheelleft_PDP    = 12;

    public static class Value{
        public static final int flywheelSpeed    = 12000;

        public static final double fly_kp        = 0.1;
        public static final double fly_kF_1        = 0.506;
        public static final double fly_kF_2        = 0.06;
        public static final double rackerKP      = 0.45;
        public static final double rackerKI      = 0.001;

        public static final double towerConst    = 0.015;
        public static final int rackerIZone      = 100;
        public static final int portHigh         = 250;     //cm
        public static final int robotHigh        = 55+5;    //cm 
    }

    public static class Button{
        public static final int tower_left = 1;
        public static final int tower_right = 3;
        public static final int intake_wing = 2;
        public static final int arm = 4;
        public static final int flywheel = 5;
        public static final int aim = 6;
        public static final int shoot = 7;
        public static final int ranker_up = 11;
        public static final int ranker_down = 12;
    }

    public static class Chassis{
        public static final int leftMaster    = 19;
        public static final int leftFollewer  = 18;
        public static final int rightMaster   = 21;
        public static final int rightFollower = 20;
        public static final double distancePerPulse = 0.1524 * Math.PI / 2048 / 9.7;
        public static final double wheelPitch = 0.7407;

        public static final boolean isRightMotorInvert = false;
        public static final boolean isLeftMotorInvert = true;
        public static final boolean isRightPhaseInvert = false;
        public static final boolean isLeftPhaseInvert = true;

        public static final double kS = 1.07;
        public static final double kV = 0.365;
        public static final double kA = 0.008;
        public static final double kP = 1.0;
    }
}
