/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.javasourcecodescomparator.main;

import pl.polsl.javasourcecodescomparator.model.ArchiveOperator;
import pl.polsl.javasourcecodescomparator.model.SourceComparator;

/**
 *
 * @author Dominik
 * @version 0.1
 */
public class Main {
    private static final double version = 0.1;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        ArchiveOperator operator = new ArchiveOperator();
        SourceComparator comparator = new SourceComparator();
        try {
            operator.readArchive("F:/TestFolder");
            comparator.setSourceFilesToCompareList(operator.getSourceFiles());
            comparator.comparePureSources();
            System.out.println(comparator.getTotalResultString());
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
}
