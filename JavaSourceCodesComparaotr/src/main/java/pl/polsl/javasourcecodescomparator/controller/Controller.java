package pl.polsl.javasourcecodescomparator.controller;

import pl.polsl.javasourcecodescomparator.model.ArchiveOperator;
import pl.polsl.javasourcecodescomparator.model.SourceComparator;
import pl.polsl.javasourcecodescomparator.view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

/**
 *
 * @author Dominik
 * @version 0.9
 */

public class Controller {

    private ArchiveOperator archiveOperator;
    private SourceComparator sourceComparator;
    private MainView mainView;
    private JFrame mainFrame;
    private String DirectoryPath;
    private ActionListener actionListener;

    public Controller(){
        initializeFields();
    }

    public Controller(String[] args){
        initializeFields();

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
            createJFrame();
            actionListener = new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent actionEvent)
                {
                    if(actionEvent.getSource() == mainView.getFileChooser()){
                            if(actionEvent.getActionCommand().equals(javax.swing.JFileChooser.APPROVE_SELECTION)) {
                                compareSources(mainView.getFileChooser().getSelectedFile().getPath());
                            } else if(actionEvent.getActionCommand().equals(JFileChooser.CANCEL_SELECTION)){
                                System.exit(0);
                        }
                    }
                }
            };
            mainView.getFileChooser().addActionListener(actionListener);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void createJFrame(){
        mainFrame = new JFrame("Source comparator");
        mainFrame.setContentPane(mainView.getMainPanel());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainView.getSimilarityPercentageTextField().setText(String.valueOf(sourceComparator.getMinimumSimilarityPercentage()));
    }

    private void initializeFields(){
        archiveOperator = new ArchiveOperator();
        sourceComparator = new SourceComparator();
        mainView = new MainView();
        DirectoryPath = "none";
    }

    private void compareSources(String pathToDirectory) {
        DirectoryPath = pathToDirectory.replaceAll(".zip", "");

        if (!DirectoryPath.equals("none")) {
            if(archiveOperator.readArchive(pathToDirectory)) {
                printExceptions();
                System.out.println(archiveOperator.getProjectsNamesString());
                System.out.println(archiveOperator.getErrorMessagesReport());

                sourceComparator.setMinimumSimilarityPercentage(mainView.getSimilarityPercentageTextField().getText());
                sourceComparator.setSourceFilesToCompareList(archiveOperator.getSourceFilesList());
                sourceComparator.compareAllFiles();

                System.out.println(sourceComparator.getTotalResultString());
                printReport();
                Object[] options = {"Yes",
                        "No, exit program"};
                int n = JOptionPane.showOptionDialog(mainFrame,
                        "Files comparison completed! \nWould you like to continue",
                        "Comparison completed",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[1]);
                if (n == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
            else{
                JOptionPane.showMessageDialog(mainFrame, "Input file invalid");
            }
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Wrong input arguments");
            System.out.println("Wrong input arguments");
        }
    }

    private void printExceptions(){
        String exceptionsString = "No errors!";

        if(!archiveOperator.getErrorMessagesReport().isEmpty()) {
            exceptionsString = archiveOperator.getErrorMessagesReport();
        }
        JOptionPane.showMessageDialog(mainFrame, exceptionsString);
    }

    private void printReport(){
        PrintWriter fileHandler = null;

        try {
            fileHandler = new PrintWriter(DirectoryPath + ".txt");
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(mainFrame, "File writing failure");
            return;
        }

        fileHandler.write(sourceComparator.getTotalResultString().replaceAll("\n", "\r\n"));
        fileHandler.close();
    }
}
