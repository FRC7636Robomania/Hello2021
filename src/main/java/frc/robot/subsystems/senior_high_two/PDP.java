// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.senior_high_two;

import edu.wpi.first.wpilibj.PowerDistributionPanel;

/** Add your docs here. */
public class PDP {
    private final static PowerDistributionPanel pdp = new PowerDistributionPanel();

    public static double getCurrent(int channel){
        return pdp.getCurrent(channel);
    }
public class PCM{
    //private final static Power
}
}
