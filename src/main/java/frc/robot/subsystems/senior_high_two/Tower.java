package frc.robot.subsystems.senior_high_two;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.MedianFilter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Tower extends SubsystemBase {
    private SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, 10, 10, 1);
    private String status = "stop";
    private static TalonSRX towerSrx = new TalonSRX(Constants.tower);
    private MedianFilter filter = new MedianFilter(3);
    private DigitalInput digInput = new DigitalInput(0);
    
    public Tower(Limelight limelight){
        towerSrx.configFactoryDefault();
        towerSrx.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.CTRE_MagEncoder_Absolute, 0, Constants.kTimesOut);

        towerSrx.setNeutralMode(NeutralMode.Brake);      
        towerSrx.setInverted(true);
       
        towerSrx.configPeakOutputForward(0.25,5);
        towerSrx.configPeakOutputReverse(-0.25,5);
        towerSrx.configNominalOutputForward(0,Constants.kTimesOut);
        towerSrx.configNominalOutputReverse(0,Constants.kTimesOut);
        towerSrx.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);

        towerSrx.configForwardSoftLimitThreshold(3600, 0);
        towerSrx.configReverseSoftLimitThreshold(-3600, 0);
        towerSrx.configForwardSoftLimitEnable(true, 0);
        towerSrx.configReverseSoftLimitEnable(true, 0);
        towerSrx.setNeutralMode(NeutralMode.Brake);

    }

    public void aimming(){
        double horizenError = Limelight.getTx()*Constants.Value.towerConst;
        double targetArea = Limelight.getTy();
        filter.calculate(horizenError);
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

    @Override
    public void periodic(){
        if(digInput.get()){
            towerSrx.setSelectedSensorPosition(0,0,10);
        }  
        SmartDashboard.putNumber("towerpo", towerSrx.getSelectedSensorPosition(0));
    }
} 
