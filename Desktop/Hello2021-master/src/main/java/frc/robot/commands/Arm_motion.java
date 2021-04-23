package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.senior_high_one.*;

public class Arm_motion extends CommandBase{
  public Arm m_arm;
    
  public  Arm_motion(Arm m_arm) {
    this.m_arm = m_arm ;
    addRequirements(m_arm);
  }
  @Override
  public void initialize() {
    m_arm.Count();
  }
  
  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
     m_arm.Arm_motion();
   }
   
   // Called once the command ends or is interrupted.
   @Override
   public void end(boolean interrupted) {
   }
   
   // Returns true when the command should end.
   @Override
   public boolean isFinished() {
   return false;
   }
}