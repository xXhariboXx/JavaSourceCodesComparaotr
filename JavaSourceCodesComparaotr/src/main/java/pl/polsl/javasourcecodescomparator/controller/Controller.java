package pl.polsl.javasourcecodescomparator.controller;

import pl.polsl.javasourcecodescomparator.model.ArchiveOperator;
import pl.polsl.javasourcecodescomparator.model.SourceComparator;

/**
 *
 * @author Dominik
 * @version 0.1
 */

public class Controller {

    private ArchiveOperator archiveOperator;
    private SourceComparator sourceComparator;
    private String DirectoryPath;

    public Controller(){
        archiveOperator = new ArchiveOperator();
        sourceComparator = new SourceComparator();
        DirectoryPath = "none";
    }

    public Controller(String[] args){
        archiveOperator = new ArchiveOperator();
        sourceComparator = new SourceComparator();
        if(args.length == 1){
            DirectoryPath = args[0];
        } else {
            DirectoryPath = "none";
        }
    }

    public void run(){
        try {
            //archiveOperator.readArchive("F:\\GIT foldery\\JavaSourceCodesComparator\\JAVA_SSI-r1--19975");
            //operator.readArchive("F:\\TestFolder");
            if(!DirectoryPath.equals("none")) {
                archiveOperator.readArchive(DirectoryPath);

                System.out.println(archiveOperator.getProjectsNamesString());
                System.out.println(archiveOperator.getErrorMessagesReport());

                sourceComparator.setSourceFilesToCompareList(archiveOperator.getSourceFilesList());
                sourceComparator.compareAllFiles();

                System.out.println(sourceComparator.getTotalResultString());
            }
            else {
                System.out.println("Wrong input arguments");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
