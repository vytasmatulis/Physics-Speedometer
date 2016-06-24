package sped;

import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import javax.swing.JTextField;

/**
 *
 * @author edwinfinch
 */
public class SimulationPickerWindow extends javax.swing.JFrame {

    public SimulationPickerWindow() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        launchSimulation0 = new javax.swing.JButton();
        launchSimulation1 = new javax.swing.JButton();
        launchSimulation2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        twoCarsAccelCoefficientLabel = new javax.swing.JLabel();
        twoCarsAccelCoefficientTextField = new javax.swing.JTextField();
        twoCarsAccelMassLabel = new javax.swing.JLabel();
        twoCarsAccelMassTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        twoCarsOneAccelMassTextField = new javax.swing.JTextField();
        twoCarsOneAccelMassLabel = new javax.swing.JLabel();
        twoCarsOneAccelCoefficientTextField = new javax.swing.JTextField();
        twoCarsOneAccelCoefficientLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        oneDimensionMassTextField = new javax.swing.JTextField();
        oneDimensionMassLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        launchSimulation0.setFont(new java.awt.Font("Helvetica Neue", 0, 13)); // NOI18N
        launchSimulation0.setText("Launch");
        launchSimulation0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchSimulation0ActionPerformed(evt);
            }
        });

        launchSimulation1.setFont(new java.awt.Font("Helvetica Neue", 0, 13)); // NOI18N
        launchSimulation1.setText("Launch");

        launchSimulation2.setFont(new java.awt.Font("Helvetica Neue", 0, 13)); // NOI18N
        launchSimulation2.setText("Launch");
        launchSimulation2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                launchSimulation2ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        twoCarsAccelCoefficientLabel.setText("Coefficient of Friction");

        twoCarsAccelCoefficientTextField.setText("0.07");

        twoCarsAccelMassLabel.setText("Mass of One Cart (in kg)");

        twoCarsAccelMassTextField.setText("0.508");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(twoCarsAccelCoefficientTextField)
                    .addComponent(twoCarsAccelMassTextField)
                    .addComponent(twoCarsAccelMassLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(twoCarsAccelCoefficientLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(twoCarsAccelCoefficientLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(twoCarsAccelCoefficientTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(twoCarsAccelMassLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(twoCarsAccelMassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        twoCarsOneAccelMassTextField.setText("0.508");

        twoCarsOneAccelMassLabel.setText("Mass of One Cart (in kg)");

        twoCarsOneAccelCoefficientTextField.setText("0.07");

        twoCarsOneAccelCoefficientLabel.setText("Coefficient of Friction");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(twoCarsOneAccelCoefficientTextField)
                    .addComponent(twoCarsOneAccelMassTextField)
                    .addComponent(twoCarsOneAccelMassLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(twoCarsOneAccelCoefficientLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(twoCarsOneAccelCoefficientLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(twoCarsOneAccelCoefficientTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(twoCarsOneAccelMassLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(twoCarsOneAccelMassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        oneDimensionMassTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        oneDimensionMassTextField.setText("0.508");

        oneDimensionMassLabel.setText("Mass of Cart (in kg)");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(oneDimensionMassTextField)
                    .addComponent(oneDimensionMassLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(oneDimensionMassLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(oneDimensionMassTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33))
        );

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Two Cars Accelerating");

        jLabel2.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Two Cars w/ One Accelerating");

        jLabel3.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("One Car Accelerating");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(launchSimulation0)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 158, Short.MAX_VALUE)
                .addComponent(launchSimulation1)
                .addGap(170, 170, 170)
                .addComponent(launchSimulation2)
                .addGap(78, 78, 78))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(launchSimulation1)
                    .addComponent(launchSimulation2)
                    .addComponent(launchSimulation0))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void showErrorDialog(String errorMessage){
        JOptionPane.showMessageDialog(this, errorMessage, "Uh oh...", ERROR_MESSAGE);
    }
    
    private void launchSimulation2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchSimulation2ActionPerformed
        if(oneDimensionMassTextField.getText().equals("")){
            showErrorDialog("Please enter a value for the mass.");
            return;
        }
        
        double mass;
        try{
            mass = Double.parseDouble(oneDimensionMassTextField.getText());
        }
        catch(Exception e){
            showErrorDialog("Invalid input, sorry.");
            return;
        }
        
        if(mass < 0){
            showErrorDialog("I didn't know negative masses were possible.");
            return;
        }
        
        OneDimensionSimulationWindow oneDWindow = new OneDimensionSimulationWindow();
        oneDWindow.setVisible(true);
        oneDWindow.setMassOfObject(mass);
    }//GEN-LAST:event_launchSimulation2ActionPerformed

    private void launchSimulation0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_launchSimulation0ActionPerformed
        JTextField[] fields = {
            twoCarsAccelCoefficientTextField, twoCarsAccelMassTextField
        };
        String[] fieldNames = {
            "coefficient", "cart mass"
        };
        double[] data = new double[2];
        try{
            for(int i = 0; i < fields.length; i++){
                data[i] = Double.parseDouble(fields[i].getText());
                if(data[i] < 0){
                    showErrorDialog("Please enter a positive number for the " + fieldNames[i] + ".");
                    return;
                }
            }
        } catch(Exception e){
            showErrorDialog("Sorry, one of your inputs are invalid, please make sure you're only typing in numbers.");
            return;
        }
        
        CollisionSimulationWindow collisionWindow = new CollisionSimulationWindow();
        collisionWindow.setVisible(true);
        collisionWindow.setMassOfObject(data[1]);
    }//GEN-LAST:event_launchSimulation0ActionPerformed
//
//    public static void main(String args[]) {
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(SimulationPickerWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(SimulationPickerWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(SimulationPickerWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(SimulationPickerWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new SimulationPickerWindow().setVisible(true);                
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JButton launchSimulation0;
    private javax.swing.JButton launchSimulation1;
    private javax.swing.JButton launchSimulation2;
    private javax.swing.JLabel oneDimensionMassLabel;
    private javax.swing.JTextField oneDimensionMassTextField;
    private javax.swing.JLabel twoCarsAccelCoefficientLabel;
    private javax.swing.JTextField twoCarsAccelCoefficientTextField;
    private javax.swing.JLabel twoCarsAccelMassLabel;
    private javax.swing.JTextField twoCarsAccelMassTextField;
    private javax.swing.JLabel twoCarsOneAccelCoefficientLabel;
    private javax.swing.JTextField twoCarsOneAccelCoefficientTextField;
    private javax.swing.JLabel twoCarsOneAccelMassLabel;
    private javax.swing.JTextField twoCarsOneAccelMassTextField;
    // End of variables declaration//GEN-END:variables
}
