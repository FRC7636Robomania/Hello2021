package frc.robot.subsystems.senior_high_two;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {

    private static NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight-unicorn");
    private static NetworkTableEntry tx = table.getEntry("tx");
    private static NetworkTableEntry ty = table.getEntry("ty");
    private static NetworkTableEntry ta = table.getEntry("ta");

    double x = tx.getDouble(0.0);
    double y = ty.getDouble(0.0);
    double area = ta.getDouble(0.0);
    double[] limelightvalue = new double[4];

    public double[] limeldouble() {
        double x = tx.getDouble(0.0);       
        limelightvalue[0]=x;
        double y = ty.getDouble(0.0);       
        limelightvalue[1]=y;
        double area = ta.getDouble(0.0);    
        limelightvalue[2]=area;
        double distance = (250-55)/(Math.tan(Math.toRadians(54+y)));  
        limelightvalue[3]=distance;
        return limelightvalue;
    }

    @Override
    public void periodic(){
        SmartDashboard.putNumber("tx", limeldouble()[0]);
        SmartDashboard.putNumber("ty", limeldouble()[1]);
        SmartDashboard.putNumber("ta", limeldouble()[2]);
        SmartDashboard.putNumber("distance", limeldouble()[3]);
    }
}
