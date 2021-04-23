package frc.robot.subsystems.senior_high_two;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    private Map<Double, Integer> map = new HashMap<Double, Integer>();
    private String status = "stop";
    

    public Racker(Limelight limelight) {
        rackerSrx.configFactoryDefault();
        rackerSrx.configSelectedFeedbackSensor(TalonSRXFeedbackDevice.CTRE_MagEncoder_Relative, 0, Constants.kTimesOut);
        rackerSrx.configMotionAcceleration(1000, Constants.kTimesOut);
        rackerSrx.configMotionCruiseVelocity(1000, Constants.kTimesOut);

        rackerSrx.configClosedLoopPeakOutput(0, 0.5);
        rackerSrx.setNeutralMode(NeutralMode.Brake);
        rackerSrx.configNeutralDeadband(0.05);
        rackerSrx.setInverted(false);
        rackerSrx.configPeakOutputForward(0.5);
        rackerSrx.configPeakOutputReverse(-0.5);

        rackerSrx.configSupplyCurrentLimit(supplyCurrentLimitConfiguration);
        rackerSrx.config_kP(0, Constants.Value.rackerKP);
        rackerSrx.config_kI(0, Constants.Value.rackerKI);
        rackerSrx.config_kD(0, Constants.Value.rackerKD);
        rackerSrx.config_IntegralZone(0, Constants.Value.rackerIZone);

        // rackerSrx.setSelectedSensorPosition(-11580, 0, Constants.kTimesOut);
        rackerSrx.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        rackerSrx.configClearPositionOnLimitF(true, 10);
        // rackerSrx.configForwardSoftLimitThreshold(-2500);
        // rackerSrx.configForwardSoftLimitEnable(true);
    }

    public void add(){
        map.put(Limelight.getdistances(), rackerSrx.getSelectedSensorVelocity());
    }
    public void printAll(){
        Set<Double> dis = map.keySet();
        for(double d : dis){
            System.out.println(d + ": " + map.get(d));
        }
    }

    public void rackerForward() {
        rackerSrx.set(ControlMode.PercentOutput, 0.28);
        status = "up";
    }

    public void rackerstop() {
        rackerSrx.set(ControlMode.PercentOutput, 0);
        status = "stop";
    }

    public void rackerReverse() {
        rackerSrx.set(ControlMode.PercentOutput, -0.28);
        status = "down";
    }

    public void reset() {
        rackerSrx.overrideLimitSwitchesEnable(false);
        rackerSrx.setSelectedSensorPosition(1000000, 0, Constants.kTimesOut);
        double[] history = new double[5];
        int count = 0;
        while (true) {
            double max = Double.MIN_VALUE, min = Double.MAX_VALUE;
            rackerSrx.set(ControlMode.PercentOutput, 0.35);
            wait(200);
            history[count] = rackerSrx.getSelectedSensorPosition();
            count++;
            // 超出十個就從最舊的開始覆蓋
            if (count >= 5) {
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
        rackerSrx.overrideLimitSwitchesEnable(true);

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

        
         if(50>=distance&&distance>40){
            rackerSrx.set(ControlMode.Position, -4870);
            status = "2800";
        }else if(60>=distance&&distance>50){
            rackerSrx.set(ControlMode.Position, -5316);
            status = "2900";
        }else if(70>=distance&&distance>60){
            rackerSrx.set(ControlMode.Position, -5200);
            status = "4100";
        }else if(80>=distance&&distance>70){
            rackerSrx.set(ControlMode.Position, -5050);
            status = "6000";
        }else if(90>=distance&&distance>80){
            rackerSrx.set(ControlMode.Position, -7150);
            status = "10000";
        }else if(100>=distance&&distance>90){
            rackerSrx.set(ControlMode.Position, -8210);
            status = "10600";
        }else if(110>=distance&&distance>100){
            rackerSrx.set(ControlMode.Position, -8276);
            status = "11000";
        }else if(120>=distance&&distance>110){
            rackerSrx.set(ControlMode.Position, -9780);
            status = "11500";
        }else if(130>=distance&&distance>120){
            rackerSrx.set(ControlMode.Position, -9540);
            status = "11900";
        }
        else if(140>=distance&&distance>130){
            rackerSrx.set(ControlMode.Position, -10590); 
            status = "11400";
        }//else{

        // }
            /*else if(150>=distance&&distance>140){
            rackerSrx.set(ControlMode.Position, -11580); 
            status = "11400";
        }else{
            rackerSrx.set(ControlMode.Position, -9889); //9889
            status = "11400";
        }*/
           
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
