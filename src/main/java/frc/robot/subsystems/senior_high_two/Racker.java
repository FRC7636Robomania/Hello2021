package frc.robot.subsystems.senior_high_two;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.MedianFilter;


public class Racker extends SubsystemBase {
    private SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true, 30, 30, 1);
    public TalonSRX rackerSrx = new TalonSRX(Constants.racker);
    private MedianFilter filter = new MedianFilter(5);
    String status = "stop";


    public Racker(Limelight limelight){
        rackerSrx.configFactoryDefault();
        rackerSrx.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.CTRE_MagEncoder_Absolute, 0, Constants.kTimesOut);
        rackerSrx.configMotionAcceleration(1000 ,Constants.kTimesOut);
        rackerSrx.configMotionCruiseVelocity(1000, Constants.kTimesOut);

        rackerSrx.configClosedLoopPeakOutput(0, 0.5); 
        rackerSrx.setNeutralMode(NeutralMode.Brake);  
        rackerSrx.configNeutralDeadband(0.2);    
        rackerSrx.setInverted(true);
        rackerSrx.configPeakOutputForward(0.5);
        rackerSrx.configPeakOutputReverse(-0.5);

        rackerSrx.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
        rackerSrx.config_kP(0, Constants.Value.rackerKP);
        rackerSrx.config_kI(0, Constants.Value.rackerKI);
        rackerSrx.config_IntegralZone(0, Constants.Value.rackerIZone);

        rackerSrx.setSelectedSensorPosition(0, 0, Constants.kTimesOut);
        
        //????
        rackerSrx.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector , LimitSwitchNormal.NormallyOpen);
        if(!rackerSrx.getSensorCollection().isFwdLimitSwitchClosed()){
            rackerSrx.set(ControlMode.PercentOutput, -0.4);
            if(rackerSrx.getSensorCollection().isFwdLimitSwitchClosed()){
                rackerSrx.setSelectedSensorPosition(0, 0, 10); 
                rackerSrx.overrideLimitSwitchesEnable(false);     
            }
        } 
    }

    public void rackerForward(){
        rackerSrx.set(ControlMode.PercentOutput,0.4);
        status = "up";
    }
   
    public void rackerstop(){
        rackerSrx.set(ControlMode.PercentOutput, 0);
        status = "stop";
    }

    public void rackerReverse(){
        rackerSrx.set(ControlMode.PercentOutput, -0.4);
        status = "down";
    }

    public void PortDistance(){
        double distance = Limelight.getdistances();
        filter.calculate(distance);

        if(distance<=250){
            rackerSrx.set(ControlMode.Position, 11500);
            status = "11500";
        }else if(300>=distance&&distance>250){
            rackerSrx.set(ControlMode.Position, 11650);
            status = "11650";
        }else if(350>=distance&&distance>300){
            rackerSrx.set(ControlMode.Position, 11750);
            status = "11750";
        }else if(400>=distance&&distance>350){
            rackerSrx.set(ControlMode.Position, 11900);
            status = "11900";
        }else if(450>=distance&&distance>400){
            rackerSrx.set(ControlMode.Position, 12000);
            status = "11200";
        }else if(500>=distance&&distance>450){
            rackerSrx.set(ControlMode.Position, 12000);
            status = "12000";
        }else{
            rackerSrx.set(ControlMode.Position, 12000);
            status = "12000";
        }
    }

    public String racker_status(){
        return status;
    }

    public Boolean rack_limit(){
        return rackerSrx.getSensorCollection().isRevLimitSwitchClosed();
    }
    
  @Override
    public void periodic(){
        if(rackerSrx.getSensorCollection().isFwdLimitSwitchClosed()){
            SmartDashboard.putBoolean("limitswitch", true);
        }else{
            SmartDashboard.putBoolean("limitswitch", false);
        }
        SmartDashboard.putNumber("Rack Position", rackerSrx.getSelectedSensorPosition());
    }
} 
