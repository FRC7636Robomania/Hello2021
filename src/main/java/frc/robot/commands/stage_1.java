/* 吸球+送球*/

package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.senior_high_one.*;

public class stage_1 extends CommandBase{
    private Intake  m_Intake;
    private Wing  m_Wing;
          
    public stage_1(Intake  m_Intake,Wing  m_Wing) {
        this.m_Intake=m_Intake;
        this.m_Wing=m_Wing;
        addRequirements(m_Wing);
        addRequirements(m_Intake);
    }
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_Intake.foward();
        m_Wing.forward();
    }
        
        // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_Wing.stop();
        m_Intake.stop();
    }
        
        // Returns true when the command should end.
    @Override
    public boolean isFinished() {
    return false;
    }
}
