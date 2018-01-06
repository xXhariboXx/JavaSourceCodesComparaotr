package pl.polsl.javasourcecodescomparator.controller;

import pl.polsl.javasourcecodescomparator.model.ArchiveOperator;
import pl.polsl.javasourcecodescomparator.model.SourceComparator;
import pl.polsl.javasourcecodescomparator.view.MainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

/**
 * Controller class of application. Controls workflow of all modules
 * @author Dominik Rączka
 * @version 1.0
 */
public class Controller {
    /**
     * ArchiveOperator object to read input file
     */
    private ArchiveOperator archiveOperator;
    /**
     * SourceComparator object to make comparison
     */
    private SourceComparator sourceComparator;
    /**
     * Main view of application
     */
    private MainView mainView;
    /**
     * Main frame of application
     */
    private JFrame mainFrame;
    /**
     * Path to directory to process
     */
    private String directoryPath;
    /**
     * Action listener
     */
    private ActionListener actionListener;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Default empty constructor. Initialize all fields
     */
    public Controller(){
        initializeFields();
    }
    /**
     * Constructor with command line arguments.
     * @param args Args can set default directoryPath
     */
    public Controller(String[] args){
        initializeFields();

        if(args.length == 1){
            directoryPath = args[0];
        } else {
            directoryPath = "none";
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Main loop of application
     */
    public void run(){
        try {
            createJFrame();
            actionListener = new ActionListener() {
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Creates new main frame
     */
    private void createJFrame(){
        mainFrame = new JFrame("Source comparator");
        mainFrame.setContentPane(mainView.getMainPanel());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainView.getSimilarityPercentageTextField().setText(String.valueOf(sourceComparator.getMinimumSimilarityPercentage()));
    }
    /**
     * Initialize all fields and objects
     */
    private void initializeFields(){
        archiveOperator = new ArchiveOperator();
        sourceComparator = new SourceComparator();
        mainView = new MainView();
        directoryPath = "none";
    }
    /**
     * Reads input fiels and compare sources inside it
     * @param pathToDirectory path to input ZIP file
     */
    private void compareSources(String pathToDirectory) {
        directoryPath = pathToDirectory.replaceAll(".zip", "");

        if (!directoryPath.equals("none")) {
            sourceComparator.clearComparatorData();
            if(archiveOperator.readArchive(pathToDirectory)) {
                printExceptions();
                System.out.println(archiveOperator.getProjectsNamesString());
                System.out.println(archiveOperator.getErrorMessagesReport());

                sourceComparator.setMinimumSimilarityPercentage(mainView.getSimilarityPercentageTextField().getText());
                sourceComparator.setSourceFilesToCompareList(archiveOperator.getSourceCodeFiles());
                sourceComparator.compareAllFiles(mainView.getSameProjectCheckBox().isSelected(), mainView.getSameAuthorCheckBox().isSelected());

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
    /**
     * Prints data reading exceptions report
     */
    private void printExceptions(){
        String exceptionsString = "No errors!";

        if(!archiveOperator.getErrorMessagesReport().isEmpty()) {
            exceptionsString = archiveOperator.getErrorMessagesReport();
        }
        JOptionPane.showMessageDialog(mainFrame, exceptionsString);
    }
    /**
     * Print final comparison report
     */
    private void printReport(){
        PrintWriter fileHandler = null;

        try {
            fileHandler = new PrintWriter(directoryPath + ".txt");
        }catch (Exception e)
        {
            JOptionPane.showMessageDialog(mainFrame, "File writing failure");
            return;
        }

        fileHandler.write(sourceComparator.getTotalResultString().replaceAll("\n", "\r\n"));
        fileHandler.close();
    }
}
