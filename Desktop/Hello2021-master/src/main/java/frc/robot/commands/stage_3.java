/*拉板機 + 持續傳球*/

package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.senior_high_one.*;

public class stage_3 extends CommandBase{
    private Conveyor  m_conveyor;
    private Wing      m_wing;
    private Intake    intake;
    public stage_3(Conveyor  m_conveyor, Wing wing, Intake intake) {
        this.m_conveyor=m_conveyor;
        this.m_wing = wing;
        this.intake = intake;
        addRequirements(m_conveyor, wing, intake);
    }
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        m_conveyor.forward();
        m_wing.forward();
        intake.slowForward();
    }
        
        // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        m_conveyor.stop();
        m_wing.stop();
        intake.stop();
    }
        
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
