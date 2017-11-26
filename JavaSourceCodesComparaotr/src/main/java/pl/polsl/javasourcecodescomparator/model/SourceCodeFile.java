package pl.polsl.javasourcecodescomparator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold sorce files data
 *
 * @author Dominik Rączka
 * @version 0.1
 */
public class SourceCodeFile {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Class final private fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Keyword to search author in source
     */
    private final String AUTHORDELIMETER = "@author";
    /**
     * Keyword to search version in source
     */
    private final String VERSIONDELIMETER = "@version";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Class private fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Author of source
     */
    private String Author;
    /**
     * Project name
     */
    private String ProjectName;
    /**
     * Name of source file
     */
    private String SourceFileName;
    /**
     * Version of source file
     */
    private String Version;
    /**
     * Content of source file
     */
    private List<String> SourceLinesList;
    /**
     * Number of lines in source file
     */
    private int LinesNumber;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public String getAuthor() {
        return Author;
    }
    public void setAuthor(String author) {
        Author = author;
    }
    public String getProjectName() {
        return ProjectName;
    }
    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }
    public String getSourceFileName() {
        return SourceFileName;
    }
    public void setSourceFileName(String sourceFileName) {
        SourceFileName = sourceFileName;
    }
    public String getVersion() {
        return Version;
    }
    public void setVersion(String version) {
        Version = version;
    }
    public List<String> getSourceLinesList() {
        return SourceLinesList;
    }
    public void setSourceLinesList(List<String> sourceLinesList) {
        SourceLinesList = sourceLinesList;
    }
    public int getLinesNumber() {
        return LinesNumber;
    }
    public void setLinesNumber(int linesNumber) {
        LinesNumber = linesNumber;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor. Initializes object
     */
    public SourceCodeFile(){
        SourceLinesList = new ArrayList<>();
        LinesNumber = SourceLinesList.size();
    }

    /**
     * Constructor to initialize object with full source file
     * @param listToInitialize source file content
     */
    public SourceCodeFile(List<String> listToInitialize){
        for(String sourceLine : listToInitialize) {
            parseInfo(sourceLine);
        }
        SourceLinesList.addAll(listToInitialize);
        LinesNumber = SourceLinesList.size();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Adds and parses line of source code
     * @param sourceLine
     */
    public void addSourceLine(String sourceLine){
        parseInfo(sourceLine);
        SourceLinesList.add(sourceLine);
        LinesNumber = SourceLinesList.size();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public override methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Converts object to string
     * @return
     */
    @Override
    public String toString() {
        String result = "";
        for(String line : SourceLinesList){
            result += line + "\n";
        }

        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Parses information from source line
     * @param lineToParse line of code to parse
     */
    private void parseInfo(String lineToParse){
        if(lineToParse.contains(AUTHORDELIMETER)){
            Author = lineToParse.substring(lineToParse.indexOf(AUTHORDELIMETER) + AUTHORDELIMETER.length() + 1);
        } else if(lineToParse.contains(VERSIONDELIMETER)){
            Version = lineToParse.substring(lineToParse.indexOf(VERSIONDELIMETER) + VERSIONDELIMETER.length() + 1);
        }
    }
}
