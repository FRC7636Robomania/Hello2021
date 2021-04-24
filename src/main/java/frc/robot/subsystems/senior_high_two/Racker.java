package frc.robot.subsystems.senior_high_two;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.senior_high_two.chassis.DrivetrainBase;
import edu.wpi.first.wpilibj.MedianFilter;

public class Racker extends SubsystemBase {
    private SupplyCurrentLimitConfiguration supplyCurrentLimitConfiguration = new SupplyCurrentLimitConfiguration(true,
            30, 30, 1);
    public TalonSRX rackerSrx = new TalonSRX(Constants.racker);
    private MedianFilter filter = new MedianFilter(9);
    private Map<Double, Integer> map = new HashMap<Double, Integer>();
    private String status = "stop";
    private DrivetrainBase train;

    public Racker(DrivetrainBase train) {
        this.train = train;
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
        rackerSrx.configAllowableClosedloopError(0, Constants.Value.rackerError, 0);
        // rackerSrx.setSelectedSensorPosition(-11580, 0, Constants.kTimesOut);
        rackerSrx.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
        rackerSrx.configClearPositionOnLimitF(true, 10);
        rackerSrx.configReverseSoftLimitThreshold(-10550);
        rackerSrx.configReverseSoftLimitEnable(true);
        rackerSrx.configVoltageCompSaturation(10.7);
        rackerSrx.enableVoltageCompensation(true);
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
        rackerSrx.set(ControlMode.PercentOutput, 0.24);
        status = "up";
    }

    public void rackerstop() {
        rackerSrx.set(ControlMode.PercentOutput, 0);
        status = "stop";
    }

    public void rackerReverse() {
        rackerSrx.set(ControlMode.PercentOutput, -0.24);
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
        Constants.Value.aimming = true;
        double distance = Limelight.getdistances();
        double position = 0;
        if(45>=distance&&distance>30){
            position = -4490 - 392 * (distance - 30) / 15;
        }else if(55>=distance&&distance>45){
            position = -4870 - 446 * (distance - 45) / 10;
        }else if(65>=distance&&distance>55){
            position = -5316 + 116 * (distance - 55) / 10;
        }else if(75>=distance&&distance>65){
            position = -5200 + 150 * (distance - 65) / 10;
        }else if(85>=distance && distance>75){
            position = -5750 - 2700 * (distance - 75) / 10;
        }else if(95>=distance&&distance>85){
            position = -7400 - 1900 * (distance - 85) / 10;
        }else if(105>=distance&&distance>95){
            position = -8210 - 66 * (distance - 95) / 10;
        }else if(115>=distance&&distance>105){
            position = -8276 - 1900 * (distance - 105) / 10;
        }else if(125>=distance&&distance>115){
            position = -9950 + 200 * (distance - 115) / 10;
        }else if(135>=distance&&distance>125){
            position = -10500 + 200 * (distance - 125) / 10;
        }else if(140>=distance&&distance>135){
            position = -10580; 
        }
        else{
            position = -10580;
        }
            /*else if(150>=distance&&distance>140){
            rackerSrx.set(ControlMode.Position, -11580); 
            status = "11400";
        }else{
            rackerSrx.set(ControlMode.Position, -9889); //9889
            status = "11400";
        }*/
        position = filter.calculate((int)position);
           rackerSrx.set(ControlMode.Position, position);
           status = position + " ";
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
        int position = rackerSrx.getSelectedSensorPosition();
        if(train.isMove() && position < -2500 && !Constants.Value.aimming){
            rackerSrx.set(ControlMode.PercentOutput, 0.25);
            
        }
        SmartDashboard.putNumber("Rack Position", rackerSrx.getSelectedSensorPosition());
    }
}
