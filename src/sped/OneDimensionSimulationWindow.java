/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sped;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import sped.ArduinoConnection.ArduinoConnectionResult;

/**
 *
 * @author edwinfinch
 */
public class OneDimensionSimulationWindow extends javax.swing.JFrame implements DataCollectorCallback {
    DataCollector collector;
    ArduinoConnection arduinoConnection;
    
    /**
     * Shows an error dialog
     * @param errorMessage The error message to display
     */
    private void showErrorDialog(String errorMessage){
        JOptionPane.showMessageDialog(this, errorMessage, "Uh oh...", ERROR_MESSAGE);
    }
    
    /**
     * Creates new form OneDimensionWindow
     */
    public OneDimensionSimulationWindow() {
        initComponents();
        
        //For when the window closes
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                arduinoConnection.prepareForClose();
                arduinoConnection.close();
                System.out.println("Done");
            }
        });
        
        //For when the window opens
        this.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
            }
            public void componentShown(ComponentEvent e) {
                System.out.println("Shown window");
                
                if(!arduinoConnection.connected){
                    setVisible(false);
                    showErrorDialog("Failed to connect to Arduino: " + arduinoConnection.mostRecentResult.getMessage() + "\n\n"
                    + "Please try unplugging the Arduino, plugging it back in, waiting a few seconds, and trying again.");
                }
            }
        });
        
        arduinoConnection = new ArduinoConnection();

        ArduinoConnectionResult result = arduinoConnection.initialize();
        arduinoConnection.mostRecentResult = result;
        boolean connectedToArduino = (result == ArduinoConnectionResult.Success);
        System.out.println(connectedToArduino ? "Connected to Arduino." : ("Failed to connect: " + result.getMessage()));

        if(connectedToArduino){
            collector = new DataCollector();
            collector.setDataCollectionCallback(this);
            arduinoConnection.setOnSerialListener(collector);
        }
    }
    
    /**
     * Sets the mass of the object in kilograms
     * @param massInKg The mass of the object
     */
    public void setMassOfObject(double massInKg){
        if(collector != null){
            collector.setMassOfObject(massInKg);
        }
        massLabel.setText("Mass: " + massInKg + "kg");
    }
    
    @Override
    public void gotTravelThroughSensor(int sensorPin, long timeTravelled) {
        System.out.println("Got a pin travel through " + sensorPin + " with a time of " + timeTravelled + "ms.");
    }

    @Override
    public void gotSpeedThroughPole(boolean frontPole, double speedInMetersPerSec, long timestamp) {
        System.out.println("Got a speed of " + speedInMetersPerSec + "m/sec on the " + (frontPole ? "front pole" : "back pole"));
                
        double mass = collector.getMassOfObject();
        
        double momentum = speedInMetersPerSec*mass;
        currentMomentumLabel.setText("Current momentum: " + DataCollector.round(momentum));
        
        double kineticEnergy = DataCollector.round(0.5*mass*(Math.pow(speedInMetersPerSec, 2)));
        kineticEnergyLabel.setText("Kinetic energy: " + kineticEnergy);
        
        if(frontPole){
            initialSpeedLabel.setText("Initial speed: " + speedInMetersPerSec + "m/s");
            initialTimeLabel.setText("Initial time: " + DataCollector.round(((double)timestamp/1000)));
        }
        else{
            finalSpeedLabel.setText("Final speed: " + speedInMetersPerSec + "m/s");
            finalTimeLabel.setText("Final time: " + DataCollector.round(((double)timestamp/1000)));
        }
    }

    @Override
    public void gotAccelerationAndFriction(double accelerationInMetersPerSecSquared, double forceOfFriction, double coefficientOfFriction) {
        System.out.println("Got an acceleration of " + accelerationInMetersPerSecSquared + "m/sec^2 with a force of friction of " + forceOfFriction + " and a coefficient of " + coefficientOfFriction);
        accelerationLabel.setText("Acceleration: " + accelerationInMetersPerSecSquared + "m/s^2");
        forceOfFrictionLabel.setText("Force of friction: " + forceOfFriction + "N");
        coefficientOfFrictionLabel.setText("Coefficient of friction: " + Math.abs(coefficientOfFriction));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        initialTimeLabel = new javax.swing.JLabel();
        initialSpeedLabel = new javax.swing.JLabel();
        finalTimeLabel = new javax.swing.JLabel();
        finalSpeedLabel = new javax.swing.JLabel();
        accelerationLabel = new javax.swing.JLabel();
        forceOfFrictionLabel = new javax.swing.JLabel();
        coefficientOfFrictionLabel = new javax.swing.JLabel();
        currentMomentumLabel = new javax.swing.JLabel();
        massLabel = new javax.swing.JLabel();
        kineticEnergyLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("1D Acceleration");

        initialTimeLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        initialTimeLabel.setText("Initial time: N/A");

        initialSpeedLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        initialSpeedLabel.setText("Initial speed: N/A");

        finalTimeLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        finalTimeLabel.setText("Final time: N/A");

        finalSpeedLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        finalSpeedLabel.setText("Final speed: N/A");

        accelerationLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        accelerationLabel.setText("Acceleration: N/A");

        forceOfFrictionLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        forceOfFrictionLabel.setText("Force of friction: N/A");

        coefficientOfFrictionLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        coefficientOfFrictionLabel.setText("Coefficient of friction: N/A");

        currentMomentumLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        currentMomentumLabel.setText("Current momentum: N/A");

        massLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        massLabel.setText("Mass: N/A");

        kineticEnergyLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        kineticEnergyLabel.setText("Kinetic energy: N/A");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(currentMomentumLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(initialTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(initialSpeedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(finalTimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(finalSpeedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(accelerationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(forceOfFrictionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(coefficientOfFrictionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                            .addComponent(massLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(kineticEnergyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(initialTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(initialSpeedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(finalTimeLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(finalSpeedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(accelerationLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(forceOfFrictionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(coefficientOfFrictionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(currentMomentumLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kineticEnergyLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(massLabel)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accelerationLabel;
    private javax.swing.JLabel coefficientOfFrictionLabel;
    private javax.swing.JLabel currentMomentumLabel;
    private javax.swing.JLabel finalSpeedLabel;
    private javax.swing.JLabel finalTimeLabel;
    private javax.swing.JLabel forceOfFrictionLabel;
    private javax.swing.JLabel initialSpeedLabel;
    private javax.swing.JLabel initialTimeLabel;
    private javax.swing.JLabel kineticEnergyLabel;
    private javax.swing.JLabel massLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
