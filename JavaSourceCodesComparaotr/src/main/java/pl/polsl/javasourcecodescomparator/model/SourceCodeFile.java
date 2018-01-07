package pl.polsl.javasourcecodescomparator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold source files data
 *
 * @author Dominik Rączka
 * @version 1.0
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
     * Source file info
     */
    private SourceFileInfo SourceFileInfo;
    /**
     * Content of source file
     */
    private List<SourceLine> SourceLinesList;
    /**
     * Number of lines in source file
     */
    private int LinesNumber;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public SourceFileInfo getSourceFileInfo() {
        return SourceFileInfo;
    }
    public void setSourceFileInfo(SourceFileInfo sourceFileInfo) {
        SourceFileInfo = sourceFileInfo;
    }
    public List<SourceLine> getSourceLinesList() {
        return SourceLinesList;
    }
    public void setSourceLinesList(List<SourceLine> sourceLinesList) {
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
        SourceFileInfo = new SourceFileInfo();
    }

    /**
     * Constructor to initialize object with full source file
     * @param listToInitialize source file content
     */
    public SourceCodeFile(List<SourceLine> listToInitialize){
        SourceLinesList = new ArrayList<>(listToInitialize);

        for(SourceLine sourceLine : listToInitialize) {
            parseInfo(sourceLine.sourceLineContent);
        }

        LinesNumber = SourceLinesList.size();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Adds and parses line of source code
     * @param sourceLine
     */
    public void addSourceLine(SourceLine sourceLine){
        parseInfo(sourceLine.sourceLineContent);
        if(sourceLine.processSourceLine()){
            SourceLinesList.add(sourceLine);
        }
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
        for(SourceLine sourceLine : SourceLinesList){
            result += sourceLine.sourceLineContent + "\n";
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
        if(SourceFileInfo.authorName.equals("none") && lineToParse.contains(AUTHORDELIMETER)){
            SourceFileInfo.authorName = lineToParse.substring(lineToParse.indexOf(AUTHORDELIMETER) + AUTHORDELIMETER.length() + 1);
        } else if(SourceFileInfo.version.equals("none") && lineToParse.contains(VERSIONDELIMETER)){
            SourceFileInfo.version = lineToParse.substring(lineToParse.indexOf(VERSIONDELIMETER) + VERSIONDELIMETER.length() + 1);
        }
    }
}
