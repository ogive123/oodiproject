/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package oodiproject;

import java.time.LocalDateTime;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
/**
 *
 * @author VICTUS
 */
public class RenewBookFrame extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RenewBookFrame.class.getName());

    private User loggedInOperator;
    private Patron targetPatron;
    private DefaultListModel<String> listModel;
    private List<Loan> activeLoans;
    /**
     * Creates new form RenewBookFrame
     */
    public RenewBookFrame() {
        initComponents();
        this.listModel = new DefaultListModel<>();
        lstBorrowedBooks.setModel(listModel);
    }
    
    public RenewBookFrame(User operator, Patron patron) {
        initComponents();
        this.loggedInOperator = operator;
        this.targetPatron = patron;
        this.listModel = new DefaultListModel<>();
        lstBorrowedBooks.setModel(listModel);

        configureUserAccess();
    }
    
    private void configureUserAccess() {
        if (loggedInOperator instanceof Patron) {
            targetPatron = (Patron) loggedInOperator;
            lblPatronInfo.setText("Patron: " + targetPatron.getUsername() + " (ID: " + targetPatron.getUserID() + ")");
            
            double liveFines = targetPatron.getBorrowingHistory().calculateUnpaidFines();
            double totalOutstandingFees = targetPatron.getCurrentFees() + liveFines;
            double threshold = LibraryBookBorrowingSystem.globalSettings.getFineThreshold();
            
            if (totalOutstandingFees >= threshold) {
                JOptionPane.showMessageDialog(this, 
                    "Sorry, you may not perform this action until your fines are cleared.", 
                    "Action Blocked (E2)", 
                    JOptionPane.WARNING_MESSAGE);
                
                new ManageLoansFrame(loggedInOperator, targetPatron).setVisible(true);
                this.dispose();
                return;
            }
            populateLoanList();
            
        } else if (loggedInOperator instanceof Librarian || loggedInOperator instanceof Admin) {
            if (targetPatron == null) {
                String inputID = JOptionPane.showInputDialog(this, 
                    "Enter target Patron Account User ID:", 
                    "Select Patron Target", 
                    JOptionPane.QUESTION_MESSAGE);
                
                if (inputID == null || inputID.trim().isEmpty()) {
                    new ManageLoansFrame(loggedInOperator, null).setVisible(true);
                    this.dispose();
                    return;
                }
                
                try {
                    int targetPatronID = Integer.parseInt(inputID.trim());
                    for (User u : LibraryBookBorrowingSystem.userDatabase) {
                        if (u instanceof Patron && u.getUserID() == targetPatronID) {
                            targetPatron = (Patron) u;
                            break;
                        }
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid ID Format. Must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
                    new ManageLoansFrame(loggedInOperator, null).setVisible(true);
                    this.dispose();
                    return;
                }
                
                if (targetPatron == null) {
                    JOptionPane.showMessageDialog(this, "No registered profile records match User ID.", "Patron Not Found", JOptionPane.ERROR_MESSAGE);
                    new ManageLoansFrame(loggedInOperator, null).setVisible(true);
                    this.dispose();
                    return;
                }
            }
            
            lblPatronInfo.setText("Staff Operator active for Patron: " + targetPatron.getUsername() + " (ID: " + targetPatron.getUserID() + ")");
            populateLoanList();
        }
    }
    
    private void populateLoanList() {
        listModel.clear();
        if (targetPatron != null && targetPatron.getBorrowingHistory() != null) {
            activeLoans = targetPatron.getBorrowingHistory().getLoans();
            
            if (activeLoans.isEmpty()) {
                listModel.addElement("No active borrowed items found linked to this account.");
                btnRenew.setEnabled(false);
            } else {
                btnRenew.setEnabled(true);
                for (Loan loan : activeLoans) {
                    listModel.addElement(loan.getBorrowedBook().getTitle() + 
                        " [ID: " + loan.getBorrowedBook().getBookID() + "]" +
                        " | Due: " + loan.getDueDate().toLocalDate() + 
                        " | Renewals: " + loan.getCurrentRenewalNo());
                }
            }
        }
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnBack = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        lblPatronInfo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstBorrowedBooks = new javax.swing.JList<>();
        lblDuration = new javax.swing.JLabel();
        txtDuration = new javax.swing.JTextField();
        btnRenew = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnBack.setText("BACK");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblTitle.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblTitle.setText("RENEW BORROWED BOOKS");

        lblPatronInfo.setText("Active Patron:");

        lstBorrowedBooks.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lstBorrowedBooks);

        lblDuration.setText("Renewal Duration (Days):");

        txtDuration.setText("7");

        btnRenew.setText("CONFIRM");
        btnRenew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRenewActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblPatronInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBack)))
                .addGap(14, 14, 14))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(101, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(lblDuration)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(btnRenew)))
                .addGap(102, 102, 102))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(btnBack))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPatronInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDuration)
                    .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnRenew)
                .addGap(41, 41, 41))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        new ManageLoansFrame(loggedInOperator, targetPatron).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRenewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRenewActionPerformed
        int selectedIndex = lstBorrowedBooks.getSelectedIndex();
        if (selectedIndex == -1 || activeLoans == null || activeLoans.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a book item from your borrowed records list.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String durationStr = txtDuration.getText().trim();
        int duration;
        try {
            duration = Integer.parseInt(durationStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid entry. Renewal duration must be an integer.", "Invalid Input (E4)", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Loan selectedLoan = activeLoans.get(selectedIndex);

        if (loggedInOperator instanceof Patron) {
            
            if (selectedLoan.getCurrentRenewalNo() >= 5) {
                JOptionPane.showMessageDialog(this, 
                    "Renewal Limit Reached! You cannot exceed 5 total renewals for a single item checkout.", 
                    "Limit Reached (E3)", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (duration < 1 || duration > 14) {
                JOptionPane.showMessageDialog(this, 
                    "Invalid Duration! Extension request intervals must span between 1 and 14 days.", 
                    "Invalid Duration (E4)", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            selectedLoan.renewLoan(duration, LibraryBookBorrowingSystem.globalSettings);
        } else {
            selectedLoan.setDueDate(selectedLoan.getDueDate().plusDays(duration));
            selectedLoan.setCurrentRenewalNo(selectedLoan.getCurrentRenewalNo() + 1);
        }

        LibraryBookBorrowingSystem.saveData();

        JOptionPane.showMessageDialog(this, 
            "Success! The loan duration for \"" + selectedLoan.getBorrowedBook().getTitle() + "\" has been extended.\n" +
            "New Due-Date Deadline: " + selectedLoan.getDueDate().toLocalDate() + "\n" +
            "Current Cumulative Renewals Count: " + selectedLoan.getCurrentRenewalNo(), 
            "Transaction Success Acknowledgment", 
            JOptionPane.INFORMATION_MESSAGE);

        populateLoanList();
    }//GEN-LAST:event_btnRenewActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new RenewBookFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRenew;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDuration;
    private javax.swing.JLabel lblPatronInfo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JList<String> lstBorrowedBooks;
    private javax.swing.JTextField txtDuration;
    // End of variables declaration//GEN-END:variables
}
