package pl.polsl.javasourcecodescomparator.main;

import pl.polsl.javasourcecodescomparator.controller.Controller;

import javax.swing.*;

/**
 * Main class of application
 * @author Dominik Rączka
 * @version 1.0
 */
public class Main {
      /**
       * Main function of application
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                Controller controller = new Controller();
                controller.run();
            }
        });
    }
    
}
