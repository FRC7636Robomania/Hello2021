package frc.robot.subsystems.senior_high_two;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Limelight extends SubsystemBase{

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

    public static double getarea() {
        double area = ta.getDouble(0.0);
        return area;
    }
    
    public static double getTx() {
        double x = tx.getDouble(0.0);
        return x;
    }
    
    public static double getTy() {
        double y = ty.getDouble(0.0);
        return y;
    }
    
    public static double getdistances(){
        double distance = (250-55)/(Math.tan(Math.toRadians(54+Limelight.getTy())));
        return distance;
    }

@Override
public void periodic(){
    SmartDashboard.putNumber("tx", Limelight.getTx());
    SmartDashboard.putNumber("ty",Limelight.getTy());
    SmartDashboard.putNumber("ta",Limelight.getarea());
    SmartDashboard.putNumber("distance", Limelight.getdistances() * 2.5);
}
}
