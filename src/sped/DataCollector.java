/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sped;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import org.json.JSONObject;

/**
 *
 * @author edwinfinch
 */

interface DataCollectorCallback {
    /**
     * Gets how long it took for the sensor pin to be traveled through. 1D acceleration only.
     * @param sensorPin The sensor pin which it went through
     * @param timeTravelled The amount of time traveled though the sensor in milliseconds
     */
    void gotTravelThroughSensor(int sensorPin, long timeTravelled);
    /**
     * Gets the speed through the pole (from DataCollector)
     * @param frontPole Whether or not the speed is through the front pole
     * @param speedInMetersPerSec The speed in meters per second
     * @param timestamp The timestamp in millis of the event
     */
    void gotSpeedThroughPole(boolean frontPole, double speedInMetersPerSec, long timestamp);
    /**
     * Got the amount of acceleration through a pole in meters per second squared
     * @param accelerationInMetersPerSecSquared The acceleration in meters per second squared
     * @param forceOfFriction The force of friction applied to the cart
     * @param coefficientOfFriction The coefficient of friction 
     */
    void gotAccelerationAndFriction(double accelerationInMetersPerSecSquared, double forceOfFriction, double coefficientOfFriction);
}

public class DataCollector implements ArduinoConnectionCallback {
    final int LOWEST_PIN = 10;
    final static int DECIMAL_PLACES = 3;
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
    private boolean inverted;
    private boolean isCollisionSimulation;
    
    /**
     * A handy function which rounds a double down
     * @param toRound The decimal to round
     * @param decimalPlaces The amount of decimal places to round to
     * @return The rounded decimal 
     */
    public static double roundTo(double toRound, int decimalPlaces){
        String format = "#.";
        for(int i = 0; i < decimalPlaces; i++){
            format += "#";
        }
        DecimalFormat df = new DecimalFormat(format);
        df.setRoundingMode(RoundingMode.CEILING);
        double newDouble = Double.parseDouble(df.format(toRound));
        return newDouble;
    }
    
    /**
     * Rounds to the set amount of decimal places
     * @param toRound The decimal to round
     * @return The rounded decimal 
     */
    public static double round(double toRound){
        return roundTo(toRound, DECIMAL_PLACES);
    }
    
    /**
     * Sets the data collection callback for this instance
     * @param callback The callback to set
     */
    public void setDataCollectionCallback(DataCollectorCallback callback){
        dataCollectionCallback = callback;
    }
    
    /**
     * Sets the mass of the object 
     * @param massInKg The mass in kilograms
     */
    public void setMassOfObject(double massInKg){
        massOfObjectInKg = massInKg;
    }
    
    /**
     * Set whether or not the simulation is collision based
     * @param yesOrNo Boolean on whether or not its collision 
     */
    public void setIsCollisionSimulation(boolean yesOrNo){
        isCollisionSimulation = yesOrNo;
    }
    
    /**
     * Gets the mass of the object set
     * @return The mass of the object
     */
    public double getMassOfObject(){
        return massOfObjectInKg;
    }
    
    /**
     * Gets the associated pin on a pole
     * @param pin The pin to get the associated pin of
     * @return The associated pin
     */
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
    
    /**
     * Acceleration for two speeds
     * @param speed1 The first speed
     * @param speed2 The second speed
     * @return The calculated acceleration
     */
    public double accelerationForSpeeds(double speed1, double speed2){
        return (speed2-speed1)/(timeFinal/1000 - timeInitial/1000);
    }
    
    /**
     * Gets a serial event from the Arduino
     * @param serialOutput The output
     */
    @Override
    public void serialEvent(String serialOutput) {
        //System.out.println("Got serial event " + serialOutput);

        JSONObject object = new JSONObject(serialOutput);
        
        int sensorPin = object.getInt("sensorPin");
        int fixedPin = sensorPin-LOWEST_PIN;
        boolean broken = object.getBoolean("broken"); //Whether or not the connection to the sensor was broken
        
        if(!isCollisionSimulation){
            if(broken){
                sensorBreakTimes[fixedPin] = System.currentTimeMillis();

                if(fixedPin == 0){
                    if(timeInitial == 0){
                        timeInitial = System.currentTimeMillis();
                    }
                    else{
                        timeFinal = System.currentTimeMillis();
                    }
                }
                if(fixedPin == 3){
                    if(timeInitial == 0){
                        inverted = true;
                        timeInitial = System.currentTimeMillis();
                    }
                    else{
                        timeFinal = System.currentTimeMillis();
                    }
                }

                int firstSensor = inverted ? 3 : 0;
                int secondSensor = inverted ? 2 : 1;

                System.out.println("Inverted: " + inverted);
                
                long associatedBreakTime = sensorBreakTimes[getAssociatedFixedPin(fixedPin)];
                if(associatedBreakTime != 0){
                    //System.out.println("Both times exist");
                    double time = (double)(sensorBreakTimes[fixedPin]-associatedBreakTime)/1000;
                    double speed = DISTANCE_BETWEEN_SENSORS_CM/time;
                    boolean frontPole = (fixedPin == firstSensor || fixedPin == secondSensor);
                    poleSpeeds[frontPole ? 0 : 1] = speed;
                    if(dataCollectionCallback != null){
                        System.out.println("1D: Initial time " + timeInitial + " final time " + timeFinal);
                        dataCollectionCallback.gotSpeedThroughPole(frontPole, round(speed/100), frontPole ? 0 : (timeFinal-timeInitial));
                        if(poleSpeeds[0] != 0 && poleSpeeds[1] != 0){
                            accelerationInCmPerSecSquared = accelerationForSpeeds(poleSpeeds[0], poleSpeeds[1]);
                            forceOfFriction = (accelerationInCmPerSecSquared/100)*massOfObjectInKg;
                            coefficientOfFriction = forceOfFriction/(massOfObjectInKg*FORCE_OF_GRAVITY);
                            dataCollectionCallback.gotAccelerationAndFriction(roundTo(accelerationInCmPerSecSquared/100, 5), round(forceOfFriction), round(coefficientOfFriction));

                            inverted = false;
                            timeInitial = 0;
                            timeFinal = 0;
                            poleSpeeds[0] = 0;
                            poleSpeeds[1] = 0;
                            for(int i = 0; i < sensorBreakTimes.length; i++){
                                sensorBreakTimes[i] = 0;
                                sensorUnbreakTimes[i] = 0;
                            }
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

                //sensorUnbreakTimes[fixedPin] = 0;
                //sensorBreakTimes[fixedPin] = 0;
            }
        }
        else{
            if(broken){
                sensorBreakTimes[fixedPin] = System.currentTimeMillis();

                int firstSensor = 0;
                int secondSensor = 1;

                long associatedBreakTime = sensorBreakTimes[getAssociatedFixedPin(fixedPin)];
                if(associatedBreakTime != 0){
                    double time = (double)(sensorBreakTimes[fixedPin]-associatedBreakTime)/1000;
                    double speed = DISTANCE_BETWEEN_SENSORS_CM/time;
                    boolean frontPole = (fixedPin == firstSensor || fixedPin == secondSensor);
                    int poleInt = frontPole ? 0 : 1;
                    poleSpeeds[poleInt] = speed;
                    if(dataCollectionCallback != null){
                        dataCollectionCallback.gotSpeedThroughPole(frontPole, round(speed/100), frontPole ? 0 : (timeFinal-timeInitial));
                        poleSpeeds[poleInt] = 0;
                        for(int i = 0; i < 2; i++){
                            sensorBreakTimes[i + (poleInt*2)] = 0;
                            sensorUnbreakTimes[i + (poleInt*2)] = 0;
                        }
                    }
                }
            }
            else{
                sensorUnbreakTimes[fixedPin] = System.currentTimeMillis();
            }
        }

        //System.out.println("Break times: " + Arrays.toString(sensorBreakTimes) + " Unbreak times: " + Arrays.toString(sensorUnbreakTimes));
        //System.out.println(broken + " for " + sensorPin);
    }
    
}
