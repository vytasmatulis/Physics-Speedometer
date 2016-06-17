/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sped;

import java.util.Arrays;
import org.json.JSONObject;

/**
 *
 * @author edwinfinch
 */

interface DataCollectorCallback {
    void gotTravelThroughSensor(int sensorPin, long timeTravelled);
    void gotSpeedThroughPole(boolean frontPole, double speedInCmPerSec, long timestamp);
    void gotAccelerationAndFriction(double accelerationInCmSquaredPerSec, double forceOfFriction, double coefficientOfFriction);
}

public class DataCollector implements ArduinoConnectionCallback {
    final int LOWEST_PIN = 10;
    final double DISTANCE_BETWEEN_SENSORS_CM = 5.7;
    final double DISTANCE_BETWEEN_POLES_CM = 33.6;
    final double FORCE_OF_GRAVITY = 9.81;
    
    long[] sensorBreakTimes = new long[4];
    long[] sensorUnbreakTimes = new long[4];
    long timeInitial, timeFinal;
    double[] poleSpeeds = new double[2];
    
    private DataCollectorCallback dataCollectionCallback;
    private double massOfObjectInKg = 0.34;
    private double forceOfFriction;
    private double coefficientOfFriction;
    private double accelerationInCmPerSecSquared;
    
    public void setDataCollectionCallback(DataCollectorCallback callback){
        dataCollectionCallback = callback;
    }
    
    public void setMassOfObject(double massInKg){
        massOfObjectInKg = massInKg;
    }
    
    private int getAssociatedFixedPin(int pin){
        switch(pin){
            case 0:
                return 1;
            case 1:
                return 0;
            case 2:
                return 3;
            case 3: 
                return 2;
        }
        return -1;
    }
    
    public double accelerationForSpeeds(double speed1, double speed2){
        return (speed2-speed1)/(timeFinal/1000 - timeInitial/1000);
    }
    
    @Override
    public void serialEvent(String serialOutput) {
        //System.out.println("Got serial event " + serialOutput);

        JSONObject object = new JSONObject(serialOutput);
        
        int sensorPin = object.getInt("sensorPin");
        int fixedPin = sensorPin-LOWEST_PIN;
        boolean broken = object.getBoolean("broken");
        
        if(broken){
            sensorBreakTimes[fixedPin] = System.currentTimeMillis();
            
            if(fixedPin == 0){
                timeInitial = System.currentTimeMillis();
            }
            if(fixedPin == 3){
                timeFinal = System.currentTimeMillis();
            }
            
            long associatedBreakTime = sensorBreakTimes[getAssociatedFixedPin(fixedPin)];
            if(associatedBreakTime != 0){
                //System.out.println("Both times exist");
                double time = (double)(sensorBreakTimes[fixedPin]-associatedBreakTime)/1000;
                System.out.println(time);
                double speed = DISTANCE_BETWEEN_SENSORS_CM/time;
                boolean frontPole = (fixedPin == 0 || fixedPin == 1);
                poleSpeeds[frontPole ? 0 : 1] = speed;
                if(dataCollectionCallback != null){
                    dataCollectionCallback.gotSpeedThroughPole(frontPole, speed, frontPole ? 0 : timeFinal-timeInitial);
                    if(poleSpeeds[0] != 0 && poleSpeeds[1] != 0){
                        accelerationInCmPerSecSquared = accelerationForSpeeds(poleSpeeds[0], poleSpeeds[1]);
                        forceOfFriction = accelerationInCmPerSecSquared*massOfObjectInKg;
                        coefficientOfFriction = forceOfFriction/(massOfObjectInKg*FORCE_OF_GRAVITY);
                        dataCollectionCallback.gotAccelerationAndFriction(accelerationInCmPerSecSquared, forceOfFriction, coefficientOfFriction);
                        
                    }
                }
            }
        }
        else{
            sensorUnbreakTimes[fixedPin] = System.currentTimeMillis();
            
            long totalTime = sensorUnbreakTimes[fixedPin]-sensorBreakTimes[fixedPin];
            if(dataCollectionCallback != null){
                dataCollectionCallback.gotTravelThroughSensor(sensorPin, totalTime);
            }
            
            sensorUnbreakTimes[fixedPin] = 0;
            sensorBreakTimes[fixedPin] = 0;
        }
        System.out.println("Break times: " + Arrays.toString(sensorBreakTimes) + " Unbreak times: " + Arrays.toString(sensorUnbreakTimes));
        System.out.println(broken + " for " + sensorPin);
    }
    
}
