/*飛輪加速*/

package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.senior_high_two.*;

public class stage_2 extends CommandBase{
    private Shooter m_shooter;
          
    public stage_2(Shooter m_shooter) {
        this.m_shooter=m_shooter;
        addRequirements(m_shooter);
    }
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_shooter.forward();
    }
        
        // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_shooter.stop();
    }
        
        // Returns true when the command should end.
    @Override
    public boolean isFinished() {
    return false;
    }
}
