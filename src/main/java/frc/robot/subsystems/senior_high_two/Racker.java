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
    private SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true,
            30, 30, 1);
    public TalonSRX rackerSrx = new TalonSRX(Constants.racker);
    private MedianFilter filter = new MedianFilter(5);
    String status = "stop";
    

    public Racker(Limelight limelight) {
        rackerSrx.configFactoryDefault();
        rackerSrx.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kTimesOut);
        rackerSrx.configMotionAcceleration(1000, Constants.kTimesOut);
        rackerSrx.configMotionCruiseVelocity(1000, Constants.kTimesOut);

        rackerSrx.configClosedLoopPeakOutput(0, 0.5);
        rackerSrx.setNeutralMode(NeutralMode.Brake);
        rackerSrx.configNeutralDeadband(0.2);
        rackerSrx.setInverted(false);
        rackerSrx.configPeakOutputForward(0.5);
        rackerSrx.configPeakOutputReverse(-0.5);

        rackerSrx.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
        rackerSrx.config_kP(0, Constants.Value.rackerKP);
        rackerSrx.config_kI(0, Constants.Value.rackerKI);
        rackerSrx.config_IntegralZone(0, Constants.Value.rackerIZone);

        rackerSrx.setSelectedSensorPosition(0, 0, Constants.kTimesOut);
        rackerSrx.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        rackerSrx.configClearPositionOnLimitF(true, 10);
        // ????  
    }

    public void rackerForward() {
        rackerSrx.set(ControlMode.PercentOutput, 0.4);
        status = "up";
    }

    public void rackerstop() {
        rackerSrx.set(ControlMode.PercentOutput, 0);
        status = "stop";
    }

    public void rackerReverse() {
        rackerSrx.set(ControlMode.PercentOutput, -0.4);
        status = "down";
    }

    public void reset() {
        // rackerSrx.overrideLimitSwitchesEnable(false);
        rackerSrx.setSelectedSensorPosition(1000000, 0, Constants.kTimesOut);
        double[] history = new double[7];
        
        int count = 0;
        while (true) {
            double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
            rackerSrx.set(ControlMode.PercentOutput, 0.35);
            wait(200);
            history[count] = rackerSrx.getSelectedSensorPosition();
            count++;
            // 超出十個就從最舊的開始覆蓋
            if (count >= 6) {
                count = 0;
            }
            // 找出最大最小
            for (int i = 0; i < history.length; i++) {
                if (history[i] > max)
                    max = history[i];
                if (history[i] < min)
                    min = history[i];
            }
            //SmartDashboard.putNumber("temp max", max);
            //SmartDashboard.putNumber("temp min", min);
            // 判斷是否有改變
            if ((max - min) < 35) {
                rackerSrx.set(ControlMode.PercentOutput, 0);
                break;
            }
        }
    }

    public void wait(int ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void PortDistance(){
        double distance = Limelight.getdistances();
        filter.calculate(distance);
        
        if(35>=distance){
            rackerSrx.set(ControlMode.Position, -2000);
            status = "2000";
        }else if(40>=distance&&distance>35){
            rackerSrx.set(ControlMode.Position, -2700);
            status = "2700";
        }else if(50>=distance&&distance>40){
            rackerSrx.set(ControlMode.Position, -2800);
            status = "2800";
        }else if(60>=distance&&distance>50){
            rackerSrx.set(ControlMode.Position, -4600);
            status = "2900";
        }else if(70>=distance&&distance>60){
            rackerSrx.set(ControlMode.Position, -5500);
            status = "4100";
        }else if(80>=distance&&distance>70){
            rackerSrx.set(ControlMode.Position, -5800);
            status = "6000";
        }else if(90>=distance&&distance>80){
            rackerSrx.set(ControlMode.Position, -10000);
            status = "10000";
        }else if(100>=distance&&distance>90){
            rackerSrx.set(ControlMode.Position, -10400);
            status = "10600";
        }else if(110>=distance&&distance>100){
            rackerSrx.set(ControlMode.Position, -11000);
            status = "11000";
        }else if(120>=distance&&distance>100){
            rackerSrx.set(ControlMode.Position, -11500);
            status = "11500";
        }else if(130>=distance&&distance>120){
            rackerSrx.set(ControlMode.Position, -11900);
            status = "11900";
        }else if(distance>130){
            rackerSrx.set(ControlMode.Position, -11400); 
            status = "11400";
        }
    }
    public String racker_status(){
        return status;
    }
    public Boolean rack_limit(){
        return rackerSrx.getSensorCollection().isFwdLimitSwitchClosed();
    }
    public void setPosition(int position){
        rackerSrx.setSelectedSensorPosition(position);
    }
    public int getPosition(){
        return rackerSrx.getSelectedSensorPosition();
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
