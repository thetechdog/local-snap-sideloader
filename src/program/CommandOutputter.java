package program;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandOutputter {
    public static String getOutput(String command){
        try {
            Process process = new ProcessBuilder("/bin/bash", "-c", command).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); //read from comm line
            String line; String output="";
            while((line= reader.readLine())!=null) output = output + line+'\n'; //reading the command line output line by line
            return output;
        } catch (IOException e) {JOptionPane.showMessageDialog(null, "An exception occurred at command execution:\n"+e.getClass().getName(),
                "Command Execution Error",JOptionPane.ERROR_MESSAGE);
            throw new RuntimeException(e);

        }


    }

}
