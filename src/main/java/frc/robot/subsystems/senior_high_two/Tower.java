package frc.robot.subsystems.senior_high_two;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Tower extends SubsystemBase {
    private SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, 10, 10, 1);
    private TalonSRX towerSrx = new TalonSRX(Constants.tower);
    private String status = "stop";
    
    public Tower(Limelight limelight){
        towerSrx.configFactoryDefault();
        towerSrx.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kTimesOut);

        towerSrx.setNeutralMode(NeutralMode.Brake);      
        towerSrx.setInverted(true);
       
        towerSrx.configPeakOutputForward(0.25,5);
        towerSrx.configPeakOutputReverse(-0.25,5);
        towerSrx.configNominalOutputForward(0,Constants.kTimesOut);
        towerSrx.configNominalOutputReverse(0,Constants.kTimesOut);
        towerSrx.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);

    }

    public void aimming(){
        double horizenError = Limelight.getTx()*Constants.Value.towerConst;
        double targetArea = Limelight.getTy();
        if(targetArea != 0){
            if((Limelight.getTx()<0.1) && (Limelight.getTx()>(-0.1))){
                towerSrx.set(ControlMode.PercentOutput, 0);
                status = "done";
            }else{
                towerSrx.set(ControlMode.PercentOutput, horizenError);
                status = "aimming";
            }
        }
        else{
            towerSrx.set(ControlMode.PercentOutput, 0);
            status = "out vision";
        }
        
    }

    public void towerForward(){
        towerSrx.set(ControlMode.PercentOutput, 0.1);
        status = "turn right";
    }

    public void towerStop(){
        towerSrx.set(ControlMode.PercentOutput, 0);
        status = "stop";
    }

    public void towerReverse(){
        towerSrx.set(ControlMode.PercentOutput, -0.1);
        status = "turn left";
    }

    public String tower_status(){
        return status;
    }
} 
