/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databinner;
import NDL_JavaClassLib.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Balaji
 */
public class DataBinnerGUI extends javax.swing.JDialog {

    /**
     * Creates new form DataBinnerGUI
     */
    TraceData RawData,BinnedData;
    double binWindow;
    boolean sorted = true;
    private DataTrace_ver1 dataSet;
    private String[] FileNames;
    
    public DataBinnerGUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        browse = new javax.swing.JButton();
        binDataBtn = new javax.swing.JButton();
        binWidth = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        browse.setText("Click to Select Data Files");
        browse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseActionPerformed(evt);
            }
        });

        binDataBtn.setText("Bin Data");
        binDataBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                binDataBtnActionPerformed(evt);
            }
        });

        binWidth.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        binWidth.setText("0");

        jLabel1.setText("Enter the bin width");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(143, 143, 143)
                .addComponent(binDataBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(browse)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(89, 89, 89)
                        .addComponent(binWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(99, 99, 99))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(binWidth, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(26, 26, 26)
                .addComponent(browse)
                .addGap(56, 56, 56)
                .addComponent(binDataBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void browseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseActionPerformed
        // TODO add your handling code here:
        MultiFileDialog FileDialog = new MultiFileDialog(null,true);
        FileDialog.setVisible(true);
        FileReader reader = null;
        dataSet = new DataTrace_ver1();
        //File[] DataFiles;
        FileNames = FileDialog.getSelectionArray();
        
                
          
        
        
        
    }//GEN-LAST:event_browseActionPerformed

    private void binDataBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_binDataBtnActionPerformed
        // TODO add your handling code here:
        double xData = 0;
        double yData = 0;
        FileReader reader = null;
        for(String name : FileNames){
            File curFile = new File(name);
            
            if(curFile.exists()){
               
                try {
                    reader = new FileReader(curFile);
                    
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DataBinnerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
                int c = 0;
                String dataString = "";
                try {
                    if(reader!= null){
                        while(  (c = reader.read()) != -1 ){
                                  
                             switch (c){
                                 case '\t':
                                     try{
                                         xData = Double.parseDouble(dataString);
                                     }catch(NumberFormatException e){
                                         xData = 0;
                                     }
                                         dataString = "";
                                         break;
                                  case '\n':
                                        try{
                                             yData = Double.parseDouble(dataString) ;
                                        }catch(NumberFormatException e){
                                            yData = 0;
                                        }
                                         dataString = "";
                                         dataSet.addData(xData, yData);
                                         break;
                                 default:
                                     //if(c != '\r')
                                        
                                        dataString +=   (char)c;
                           }
                        } 
                        
                        }
                    } catch (IOException ex) {
                    Logger.getLogger(DataBinnerGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            
            
            
            }
            
            double binWnd = Double.parseDouble(this.binWidth.getText());
            DataTrace_ver1 binData = dataSet.binData(binWnd, true, true);
       
       //for(int i = 0 ; i < binData.getDataLength() ; i++){
            ArrayList<Double> DataX =  binData.getX();
            ArrayList<Double> DataY = binData.getY();
            /*ArrayList<Double> rwDatax = dataSet.getX();
            ArrayList<Double> rwDataY = dataSet.getY();*/
            FileWriter dataWriter;
            
            File fOut = new File(curFile.getParent() +"\\"+"Binned_"+curFile.getName());
            try {
                dataWriter = new FileWriter(fOut);
                int Idx = 0;
                for(Double d : DataX){
                    String out = "" + d +"\t" + DataY.get(Idx)+ "\t"+"\t"/*+rwDataY.get(Idx)+*/+"\n";
                    dataWriter.append(out);
                    System.out.print(out);
                    Idx++;
                }
                dataWriter.close();
                dataSet.clear();
            } catch (IOException ex) {
                Logger.getLogger(DataBinnerGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        }
        
        
       //}
        
        
    }//GEN-LAST:event_binDataBtnActionPerformed

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
            java.util.logging.Logger.getLogger(DataBinnerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataBinnerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataBinnerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataBinnerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DataBinnerGUI dialog = new DataBinnerGUI(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton binDataBtn;
    private javax.swing.JTextField binWidth;
    private javax.swing.JButton browse;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
