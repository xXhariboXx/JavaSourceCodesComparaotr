/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.javasourcecodescomparator.main;

/**
 *
 * @author Dominik
 * @version 0.1
 */

import pl.polsl.javasourcecodescomparator.model.ArchiveOperator;
import pl.polsl.javasourcecodescomparator.model.SourceCodeFile;


public class Main {
    private static final double version = 0.1;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                Controller c = new Controller();
//                c.run();
//            }
//        });

        ArchiveOperator operator = new ArchiveOperator();
        try {
            operator.readArchive("F:/TestFolder");
            for(SourceCodeFile file : operator.getSourceFiles()){
                System.out.println(file.toString());
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
}