package frc.robot.subsystems.senior_high_one;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.senior_high_two.PDP;

import com.ctre.phoenix.motorcontrol.ControlMode;

public class Intake extends SubsystemBase {
    /** Creates a new ExampleSubsystem. */
   private final WPI_VictorSPX motor = new WPI_VictorSPX(7);

   public Intake(){
   }

    public void foward(){
      motor.set(ControlMode.PercentOutput,0.5);
    }
    public void stop(){
      motor.set(ControlMode.PercentOutput,-0.0);
    }
    public void reverse(){
      motor.set(ControlMode.PercentOutput,-0.5);
    }

    @Override
    public void periodic(){
      if (PDP.getCurrent(Constants.Intake_PDP) > 25){
        motor.set(ControlMode.PercentOutput,0.0);
       }
    }
}