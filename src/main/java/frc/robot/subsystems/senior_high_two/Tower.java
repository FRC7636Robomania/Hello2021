package frc.robot.subsystems.senior_high_two;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;
import edu.wpi.first.wpilibj.MedianFilter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Tower extends SubsystemBase {
    private SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, 10, 10, 1);
    private String status = "stop";
    private static TalonSRX towerSrx = new TalonSRX(Constants.tower);
    private static final double basicPower = 0.065;
    // private DigitalInput digInput = new DigitalInput(0);
    
    public Tower(Limelight limelight){
        towerSrx.configFactoryDefault();
        towerSrx.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.CTRE_MagEncoder_Absolute, 0, Constants.kTimesOut);
        towerSrx.setSelectedSensorPosition(0);

        towerSrx.setNeutralMode(NeutralMode.Brake);      
        towerSrx.setInverted(true);
       
        towerSrx.configPeakOutputForward(0.2,5);
        towerSrx.configPeakOutputReverse(-0.2,5);
        towerSrx.configNominalOutputForward(0,Constants.kTimesOut);
        towerSrx.configNominalOutputReverse(0,Constants.kTimesOut);
        towerSrx.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);

        towerSrx.configForwardSoftLimitThreshold(3600, 0);
        towerSrx.configReverseSoftLimitThreshold(-3600, 0);
        towerSrx.configForwardSoftLimitEnable(true, 0);
        towerSrx.configReverseSoftLimitEnable(true, 0);
        towerSrx.setNeutralMode(NeutralMode.Brake);
        towerSrx.configVoltageCompSaturation(10.5);
        towerSrx.enableVoltageCompensation(true);

    }

    public void aimming(){
        double horizon = -Limelight.getTx() * Constants.Value.towerConst;
        double targetArea = Limelight.getarea();
        if(targetArea > 0 && Math.abs(Limelight.getTx()) > 0.095){

            if(Math.abs(Limelight.getTx()) < 1){
                if(horizon > 0){
                    horizon += basicPower - 0.0037;
                }else if(horizon < 0){
                    horizon -= (basicPower - 0.0037);
                }
                towerSrx.set(ControlMode.PercentOutput, horizon);
            }else if (Math.abs(Limelight.getTx()) <= 3.8){
                if(horizon > 0){
                    horizon += basicPower - 0.005;
                }else if(horizon < 0){
                    horizon -= (basicPower -0.005);
                }
                towerSrx.set(ControlMode.PercentOutput, horizon);
            }else if (Math.abs(Limelight.getTx()) <= 5){
                if(horizon > 0){
                    horizon += basicPower - 0.009;
                }else if(horizon < 0){
                    horizon -= (basicPower -0.009);
                }
                towerSrx.set(ControlMode.PercentOutput, horizon);
            }else{
                towerSrx.set(ControlMode.PercentOutput, horizon);
            }
            status = "aimming";
        }
        else{
            towerSrx.set(ControlMode.PercentOutput, 0);
            status = "out vision";
        }
        
    }

    public void towerForward(){
        towerSrx.set(ControlMode.PercentOutput, 0.17);
        status = "turn right";
    }

    public void towerStop(){
        towerSrx.set(ControlMode.PercentOutput, 0);
        status = "stop";
    }

    public void towerReverse(){
        towerSrx.set(ControlMode.PercentOutput, -0.17);
        status = "turn left";
    }

    public String tower_status(){
        return status;
    }

    @Override
    public void periodic(){
        // if(digInput.get()){
        //     towerSrx.setSelectedSensorPosition(0,0,10);
        // }  
        SmartDashboard.putNumber("towerpo", towerSrx.getSelectedSensorPosition(0));
    }
} 
