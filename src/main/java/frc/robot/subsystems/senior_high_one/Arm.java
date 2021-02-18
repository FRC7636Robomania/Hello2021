
package frc.robot.subsystems.senior_high_one;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm extends SubsystemBase {
  private static DoubleSolenoid d = new DoubleSolenoid(0,1);
  protected static Compressor c = new Compressor(0);
  static int count = 0;
  String status = "in";

  public Arm() {
  }

  public void Pneumatic_Status(){
    c.setClosedLoopControl(true);
  }

  public void Arm_motion(){
    if((count%2)==0){
      d.set(DoubleSolenoid.Value.kForward);
      status = "out";
    }else{
      d.set(DoubleSolenoid.Value.kReverse);
      status = "in";
    }
  }

  public void Count(){
    if(count==10){
      count = 3;
    }else{
      count++;
    }
  }

  public String arm_status(){
    return status;
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Count", count);
  }
}
