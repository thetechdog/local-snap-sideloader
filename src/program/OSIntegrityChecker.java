package program;

import javax.swing.*;

public class OSIntegrityChecker {
    public OSIntegrityChecker(){

    }
    public static void OSCheck(){
        if(!System.getProperty("os.name").startsWith("Linux")){
            System.out.println("This program can only run on Linux.");
            JOptionPane.showMessageDialog(null, "This program can only run on Linux.","Aw, snap!",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }

    }

    public static void snapdInstallCheck(){//check if snapd is installed
        if(CommandOutputter.getOutput("whereis snapd").equals("snapd:\n")){//first check
            System.out.println("snapd not found");
            JOptionPane.showMessageDialog(null, "Couldn't find snapd.\nInstall snapd and try again.\nThis program can't run and will now close.",
                    "Package Manager Missing",JOptionPane.WARNING_MESSAGE);
            System.exit(1);
        }
    }

    public static void snapRuns(){//check if snap command can run
        try {
            Process snapCheck = new ProcessBuilder("/bin/bash", "-c", "snap").start();
            int exitCode=snapCheck.waitFor();
            if(exitCode!=0){
                System.out.println("fatal: snap command not accessible");
                JOptionPane.showMessageDialog(null, "Can't execute snap command.\nCheck whether snapd is correctly installed, or if the program has the correct permissions.\nThis program can't run and will now close.",
                        "Package Manager Inaccessible",JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"An exception occurred:\n"+e.getClass().getName());e.printStackTrace();
        }
    }
}
//TODO: nothing?