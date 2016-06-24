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
import javax.swing.JTextField;

/**
 *
 * @author edwinfinch
 */
public class CollisionSimulationWindow extends javax.swing.JFrame implements DataCollectorCallback {
    DataCollector collector;
    ArduinoConnection arduinoConnection;
    
    JTextField[][] speedTextFields;
    JTextField[][] momentumTextFields;
    JTextField[][] energiesTextFields;
    
    double[][] speeds = new double[2][2];
    double[][] momentums = new double[2][2];
    double[][] energies = new double[2][2];
    
    double coefficientOfFriction = 0.0;
    double distance = 0.20;
    
    boolean finished[] = new boolean[2];
    boolean oneAccelerating = false;
    
    /**
     * Shows an error dialog
     * @param errorMessage The error message to display
     */
    private void showErrorDialog(String errorMessage){
        JOptionPane.showMessageDialog(this, errorMessage, "Uh oh...", ERROR_MESSAGE);
    }
    
    /**
     * Creates new form CollisionSimulationWindow
     */
    public CollisionSimulationWindow() {
        initComponents();
        
        //This is for when the user wants to close the window
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                arduinoConnection.prepareForClose();
                arduinoConnection.close();
                System.out.println("Done");
            }
        });
        
        //This is for when the window shows, make sure the Arduino is properly connected
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
                
                speedTextFields = new JTextField[2][2];
                momentumTextFields = new JTextField[2][2];
                energiesTextFields = new JTextField[2][2];
                
                speedTextFields[0][0] = cart1BeforeSpeedTextField;
                speedTextFields[0][1] = cart1AfterSpeedTextField;
                speedTextFields[1][0] = cart2BeforeSpeedTextField;
                speedTextFields[1][1] = cart2AfterSpeedTextField;

                momentumTextFields[0][0] = cart1BeforeMomentumTextField;
                momentumTextFields[0][1] = cart1AfterMomentumTextField;
                momentumTextFields[1][0] = cart2BeforeMomentumTextField;
                momentumTextFields[1][1] = cart2AfterMomentumTextField;

                energiesTextFields[0][0] = cart1BeforeKineticTextField;
                energiesTextFields[0][1] = cart1AfterKineticTextField;
                energiesTextFields[1][0] = cart2BeforeKineticTextField;
                energiesTextFields[1][1] = cart2AfterKineticTextField;
            }
        });
        
        arduinoConnection = new ArduinoConnection();

        ArduinoConnection.ArduinoConnectionResult result = arduinoConnection.initialize();
        arduinoConnection.mostRecentResult = result;
        boolean connectedToArduino = (result == ArduinoConnection.ArduinoConnectionResult.Success);
        System.out.println(connectedToArduino ? "Connected to Arduino." : ("Failed to connect: " + result.getMessage()));

        if(connectedToArduino){
            collector = new DataCollector();
            collector.setIsCollisionSimulation(true);
            collector.setDataCollectionCallback(this);
            arduinoConnection.setOnSerialListener(collector);
        }
    }
    
    /**
     * Sets the mass of the object being collided.
     * @param massInKg The mass of the object in kilograms.
     */
    public void setMassOfObject(double massInKg){
        if(collector != null){
            collector.setMassOfObject(massInKg);
        }
    }
    
    /**
     * Sets the coefficient of friction of the surface that has the collision occurring on it.
     * @param newCoefficient The new coefficient to set.
     */
    public void setCoefficientOfFriction(double newCoefficient){
        coefficientOfFriction = newCoefficient;
    }
    
    @Override
    public void gotTravelThroughSensor(int sensorPin, long timeTravelled) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gotSpeedThroughPole(boolean frontPole, double speedInMetersPerSec, long timestamp) {
        double mass = collector.getMassOfObject();
        
        int poleInt = frontPole ? 0 : 1;
        boolean before = (speeds[poleInt][0] != 0);
        if(!frontPole && oneAccelerating){
            before = true;
        }
        int alreadyCalculatedInitial = before ? 1 : 0;
        
        if(finished[poleInt]){
            System.out.println("already finished " + poleInt);
            return;
        }
        
        System.out.println("got speed of " + speedInMetersPerSec + "m/s and frontpole " + frontPole);
        
        double speed = Math.sqrt(Math.pow(speedInMetersPerSec, 2) - (2*distance*coefficientOfFriction*9.8));
        double momentum = speedInMetersPerSec*mass;        
        double kineticEnergy = DataCollector.round(0.5*mass*(Math.pow(speed, 2)));
        
        speeds[poleInt][alreadyCalculatedInitial] = speed;
        momentums[poleInt][alreadyCalculatedInitial] = momentum;
        energies[poleInt][alreadyCalculatedInitial] = kineticEnergy;
        
        System.out.println("Setting " + poleInt +  " calc " + alreadyCalculatedInitial + " speed " + speed);
        
        speedTextFields[poleInt][alreadyCalculatedInitial].setText(speed + "m/s");
        momentumTextFields[poleInt][alreadyCalculatedInitial].setText(momentum + "");
        energiesTextFields[poleInt][alreadyCalculatedInitial].setText(kineticEnergy + "");
        
        System.out.println("speed " + speed + " momentum " + momentum + " KE " + kineticEnergy);
        
        if(alreadyCalculatedInitial == 1 || (!frontPole && oneAccelerating)){
            if(oneAccelerating){
                speedTextFields[1][0].setText("0m/s");
                momentumTextFields[1][0].setText("0");
                energiesTextFields[1][0].setText("0");
                speedTextFields[0][1].setText("0m/s");
                momentumTextFields[0][1].setText("0");
                energiesTextFields[0][1].setText("0");
            }
            finished[poleInt] = true;
            
        }
        //System.out.println
        //Speed before
        //sqrt((2*distance*coefficient*9.8) - speedfromsensors ^2)
        
        //Speed after
        //sqrt(speedfromsensors ^2 - (2*distance*coefficient*9.8))
    }

    @Override
    public void gotAccelerationAndFriction(double accelerationInMetersPerSecSquared, double forceOfFriction, double coefficientOfFriction) {
        
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
        cart1BeforePanel = new javax.swing.JPanel();
        cart1BeforeTitle = new javax.swing.JLabel();
        cart1BeforeKineticLabel = new javax.swing.JLabel();
        cart1BeforeKineticTextField = new javax.swing.JTextField();
        cart1BeforeMomentumLabel = new javax.swing.JLabel();
        cart1BeforeMomentumTextField = new javax.swing.JTextField();
        cart1BeforeSpeedLabel = new javax.swing.JLabel();
        cart1BeforeSpeedTextField = new javax.swing.JTextField();
        cart1AfterPanel = new javax.swing.JPanel();
        cart1AfterTitle = new javax.swing.JLabel();
        cart1AfterKineticLabel = new javax.swing.JLabel();
        cart1AfterKineticTextField = new javax.swing.JTextField();
        cart1AfterMomentumLabel = new javax.swing.JLabel();
        cart1AfterMomentumTextField = new javax.swing.JTextField();
        cart1AfterSpeedLabel = new javax.swing.JLabel();
        cart1AfterSpeedTextField = new javax.swing.JTextField();
        cart2BeforePanel = new javax.swing.JPanel();
        cart2BeforeTitle = new javax.swing.JLabel();
        cart2BeforeKineticLabel = new javax.swing.JLabel();
        cart2BeforeKineticTextField = new javax.swing.JTextField();
        cart2BeforeMomentumLabel = new javax.swing.JLabel();
        cart2BeforeMomentumTextField = new javax.swing.JTextField();
        cart2BeforeSpeedLabel = new javax.swing.JLabel();
        cart2BeforeSpeedTextField = new javax.swing.JTextField();
        cart2AfterPanel = new javax.swing.JPanel();
        cart2AfterTitle = new javax.swing.JLabel();
        cart2AfterKineticLabel = new javax.swing.JLabel();
        cart2AfterKineticTextField = new javax.swing.JTextField();
        cart2AfterMomentumLabel = new javax.swing.JLabel();
        cart2AfterMomentumTextField = new javax.swing.JTextField();
        cart2AfterSpeedLabel = new javax.swing.JLabel();
        cart2AfterSpeedTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Cart Collision");

        cart1BeforePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cart1BeforeTitle.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        cart1BeforeTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cart1BeforeTitle.setText("Cart 1 Before Collision");

        cart1BeforeKineticLabel.setText("Kinetic Energy");

        cart1BeforeKineticTextField.setEditable(false);
        cart1BeforeKineticTextField.setText("Waiting...");
        cart1BeforeKineticTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cart1BeforeKineticTextFieldActionPerformed(evt);
            }
        });

        cart1BeforeMomentumLabel.setText("Momentum");

        cart1BeforeMomentumTextField.setEditable(false);
        cart1BeforeMomentumTextField.setText("Waiting...");

        cart1BeforeSpeedLabel.setText("Speed");

        cart1BeforeSpeedTextField.setEditable(false);
        cart1BeforeSpeedTextField.setText("Waiting...");
        cart1BeforeSpeedTextField.setToolTipText("");

        javax.swing.GroupLayout cart1BeforePanelLayout = new javax.swing.GroupLayout(cart1BeforePanel);
        cart1BeforePanel.setLayout(cart1BeforePanelLayout);
        cart1BeforePanelLayout.setHorizontalGroup(
            cart1BeforePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cart1BeforePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cart1BeforePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cart1BeforeTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addComponent(cart1BeforeKineticLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart1BeforeKineticTextField)
                    .addComponent(cart1BeforeMomentumLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart1BeforeMomentumTextField)
                    .addComponent(cart1BeforeSpeedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart1BeforeSpeedTextField))
                .addContainerGap())
        );
        cart1BeforePanelLayout.setVerticalGroup(
            cart1BeforePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cart1BeforePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cart1BeforeTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cart1BeforeKineticLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart1BeforeKineticTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cart1BeforeMomentumLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart1BeforeMomentumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cart1BeforeSpeedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart1BeforeSpeedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cart1AfterPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cart1AfterTitle.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        cart1AfterTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cart1AfterTitle.setText("Cart 1 After Collision");

        cart1AfterKineticLabel.setText("Kinetic Energy");

        cart1AfterKineticTextField.setEditable(false);
        cart1AfterKineticTextField.setText("Waiting...");

        cart1AfterMomentumLabel.setText("Momentum");

        cart1AfterMomentumTextField.setEditable(false);
        cart1AfterMomentumTextField.setText("Waiting...");

        cart1AfterSpeedLabel.setText("Speed");

        cart1AfterSpeedTextField.setEditable(false);
        cart1AfterSpeedTextField.setText("Waiting...");

        javax.swing.GroupLayout cart1AfterPanelLayout = new javax.swing.GroupLayout(cart1AfterPanel);
        cart1AfterPanel.setLayout(cart1AfterPanelLayout);
        cart1AfterPanelLayout.setHorizontalGroup(
            cart1AfterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cart1AfterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cart1AfterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cart1AfterTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addComponent(cart1AfterKineticLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart1AfterKineticTextField)
                    .addComponent(cart1AfterMomentumLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart1AfterMomentumTextField)
                    .addComponent(cart1AfterSpeedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart1AfterSpeedTextField))
                .addContainerGap())
        );
        cart1AfterPanelLayout.setVerticalGroup(
            cart1AfterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cart1AfterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cart1AfterTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cart1AfterKineticLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart1AfterKineticTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cart1AfterMomentumLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart1AfterMomentumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cart1AfterSpeedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart1AfterSpeedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cart2BeforePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cart2BeforeTitle.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        cart2BeforeTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cart2BeforeTitle.setText("Cart 2 Before Collision");

        cart2BeforeKineticLabel.setText("Kinetic Energy");

        cart2BeforeKineticTextField.setEditable(false);
        cart2BeforeKineticTextField.setText("Waiting...");
        cart2BeforeKineticTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cart2BeforeKineticTextFieldActionPerformed(evt);
            }
        });

        cart2BeforeMomentumLabel.setText("Momentum");

        cart2BeforeMomentumTextField.setEditable(false);
        cart2BeforeMomentumTextField.setText("Waiting...");

        cart2BeforeSpeedLabel.setText("Speed");

        cart2BeforeSpeedTextField.setEditable(false);
        cart2BeforeSpeedTextField.setText("Waiting...");

        javax.swing.GroupLayout cart2BeforePanelLayout = new javax.swing.GroupLayout(cart2BeforePanel);
        cart2BeforePanel.setLayout(cart2BeforePanelLayout);
        cart2BeforePanelLayout.setHorizontalGroup(
            cart2BeforePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cart2BeforePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cart2BeforePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cart2BeforeTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addComponent(cart2BeforeKineticLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart2BeforeKineticTextField)
                    .addComponent(cart2BeforeMomentumLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart2BeforeMomentumTextField)
                    .addComponent(cart2BeforeSpeedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart2BeforeSpeedTextField))
                .addGap(7, 7, 7))
        );
        cart2BeforePanelLayout.setVerticalGroup(
            cart2BeforePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cart2BeforePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cart2BeforeTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cart2BeforeKineticLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart2BeforeKineticTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cart2BeforeMomentumLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart2BeforeMomentumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cart2BeforeSpeedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart2BeforeSpeedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cart2AfterPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cart2AfterTitle.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        cart2AfterTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cart2AfterTitle.setText("Cart 2 After Collision");

        cart2AfterKineticLabel.setText("Kinetic Energy");

        cart2AfterKineticTextField.setEditable(false);
        cart2AfterKineticTextField.setText("Waiting...");

        cart2AfterMomentumLabel.setText("Momentum");

        cart2AfterMomentumTextField.setEditable(false);
        cart2AfterMomentumTextField.setText("Waiting...");

        cart2AfterSpeedLabel.setText("Speed");

        cart2AfterSpeedTextField.setEditable(false);
        cart2AfterSpeedTextField.setText("Waiting...");

        javax.swing.GroupLayout cart2AfterPanelLayout = new javax.swing.GroupLayout(cart2AfterPanel);
        cart2AfterPanel.setLayout(cart2AfterPanelLayout);
        cart2AfterPanelLayout.setHorizontalGroup(
            cart2AfterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cart2AfterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(cart2AfterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cart2AfterTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart2AfterKineticLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart2AfterKineticTextField)
                    .addComponent(cart2AfterMomentumLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart2AfterMomentumTextField)
                    .addComponent(cart2AfterSpeedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart2AfterSpeedTextField))
                .addGap(7, 7, 7))
        );
        cart2AfterPanelLayout.setVerticalGroup(
            cart2AfterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cart2AfterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cart2AfterTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cart2AfterKineticLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart2AfterKineticTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cart2AfterMomentumLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart2AfterMomentumTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(cart2AfterSpeedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cart2AfterSpeedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cart1BeforePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cart1AfterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cart2BeforePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cart2AfterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cart1BeforePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cart1AfterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cart2AfterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cart2BeforePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cart1BeforeKineticTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cart1BeforeKineticTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cart1BeforeKineticTextFieldActionPerformed

    private void cart2BeforeKineticTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cart2BeforeKineticTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cart2BeforeKineticTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CollisionSimulationWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CollisionSimulationWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CollisionSimulationWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CollisionSimulationWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CollisionSimulationWindow window = new CollisionSimulationWindow();
                window.setVisible(window.arduinoConnection.connected);
                System.out.println("connected " + window.arduinoConnection.connected);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cart1AfterKineticLabel;
    private javax.swing.JTextField cart1AfterKineticTextField;
    private javax.swing.JLabel cart1AfterMomentumLabel;
    private javax.swing.JTextField cart1AfterMomentumTextField;
    private javax.swing.JPanel cart1AfterPanel;
    private javax.swing.JLabel cart1AfterSpeedLabel;
    private javax.swing.JTextField cart1AfterSpeedTextField;
    private javax.swing.JLabel cart1AfterTitle;
    private javax.swing.JLabel cart1BeforeKineticLabel;
    private javax.swing.JTextField cart1BeforeKineticTextField;
    private javax.swing.JLabel cart1BeforeMomentumLabel;
    private javax.swing.JTextField cart1BeforeMomentumTextField;
    private javax.swing.JPanel cart1BeforePanel;
    private javax.swing.JLabel cart1BeforeSpeedLabel;
    private javax.swing.JTextField cart1BeforeSpeedTextField;
    private javax.swing.JLabel cart1BeforeTitle;
    private javax.swing.JLabel cart2AfterKineticLabel;
    private javax.swing.JTextField cart2AfterKineticTextField;
    private javax.swing.JLabel cart2AfterMomentumLabel;
    private javax.swing.JTextField cart2AfterMomentumTextField;
    private javax.swing.JPanel cart2AfterPanel;
    private javax.swing.JLabel cart2AfterSpeedLabel;
    private javax.swing.JTextField cart2AfterSpeedTextField;
    private javax.swing.JLabel cart2AfterTitle;
    private javax.swing.JLabel cart2BeforeKineticLabel;
    private javax.swing.JTextField cart2BeforeKineticTextField;
    private javax.swing.JLabel cart2BeforeMomentumLabel;
    private javax.swing.JTextField cart2BeforeMomentumTextField;
    private javax.swing.JPanel cart2BeforePanel;
    private javax.swing.JLabel cart2BeforeSpeedLabel;
    private javax.swing.JTextField cart2BeforeSpeedTextField;
    private javax.swing.JLabel cart2BeforeTitle;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
