
package frc.robot.subsystems.senior_high_one;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.senior_high_two.PDP;


public class Wing extends SubsystemBase {

  private final WPI_VictorSPX TwoWing = new WPI_VictorSPX(Constants.Wing_TwoWing);
  private final WPI_VictorSPX Middle = new WPI_VictorSPX(Constants.Wing_middle);

  private String status = "stoooooooop";
  public Wing(){
    TwoWing.configNeutralDeadband(0.01);
    TwoWing.configPeakOutputForward(0.5);
    TwoWing.configPeakOutputReverse(-0.5);
    TwoWing.setNeutralMode(NeutralMode.Coast);

    Middle.configNeutralDeadband(0.01);
    Middle.configPeakOutputForward(0.5);
    Middle.configPeakOutputReverse(-0.5);
    Middle.setNeutralMode(NeutralMode.Coast);

    Middle.setInverted(true);
    TwoWing.setInverted(true);

  }
  
  public String getstatus(){
    return status;
  }
  
  
  public void forward(){
      TwoWing.set(ControlMode.PercentOutput,-0.5);
      Middle.set(ControlMode.PercentOutput,-0.5);
      status = "Enggggggggggggggggggggggggageeeeeeee!!!";
  }

  public void stop(){
    TwoWing.set(ControlMode.PercentOutput,0);
    Middle.set(ControlMode.PercentOutput,0);
    status = "stoooooooop！";
  }

  public void reverse(){
      TwoWing.set(ControlMode.PercentOutput,-0.5);
      Middle.set(ControlMode.PercentOutput,0.5);
    status = "Retreeeeeeeeeeeeeeeeeeeeeeeeat!!!";
  }

  @Override
  public void periodic(){
    if(PDP.getCurrent(Constants.Wing_TwoWing_PDP)>25){
      TwoWing.set(ControlMode.PercentOutput,0);
    }
    if(PDP.getCurrent(Constants.Wing_middle_PDP)>25){
      Middle.set(ControlMode.PercentOutput,0);
    }
  }
}