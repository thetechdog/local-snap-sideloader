package program;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main{
    private static String auth="";//used for password storage during current session
    public static void main(String[] args) {
        OSIntegrityChecker.OSCheck(); //check if OS is Linux
        OSIntegrityChecker.snapdInstallCheck(); //first snapd check
        OSIntegrityChecker.snapRuns(); //second snapd check

        LocalPackageInstaller LPI=new LocalPackageInstaller();
        LPI.setVisible(true);

    }
    public static String getAuth(){
        return auth;
    }

    public static void setAuth(String auth){
        Main.auth=auth;
    }


    public static int pendingActionDialog(JFrame frame, String actionCommand, String dialogMessage, String dialogTitle) throws InterruptedException {
        AtomicInteger exitCode = new AtomicInteger();
        AtomicBoolean operationFlag=new AtomicBoolean(); //used to determine when the command execution is done

        JDialog actionDialog = new JDialog(frame, dialogTitle, true);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(2,1,10,10));
        actionPanel.add(new JLabel(dialogMessage));
        actionPanel.add(progressBar);
        actionPanel.setBorder(new EmptyBorder(10,7,12,7));
        actionPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        actionDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        actionDialog.add(actionPanel);
        actionDialog.pack();
        actionDialog.setSize(300,100);
        actionDialog.setLocationRelativeTo(frame);

        Thread actionThread = new Thread(() -> {//thread running the command
            try{
                System.out.println("Thread execution started.");
                Process actionProcess = new ProcessBuilder("/bin/bash", "-c", actionCommand).start();
                exitCode.set(actionProcess.waitFor());

            }
            catch (Exception e){JOptionPane.showMessageDialog(frame,"An exception occurred:\n"+e.getClass().getName(),
                    "Process Error",JOptionPane.ERROR_MESSAGE);e.printStackTrace();}
            operationFlag.set(true);
            actionDialog.dispose();
            return;
        });
        actionThread.start();
        actionDialog.setVisible(true); //blocks further execution while visible
        actionThread.join(); //waiting to make sure the actionThread has finished
        if (operationFlag.get()) System.out.println("Thread has finished.");


        return exitCode.get();
    }

}