package pl.polsl.javasourcecodescomparator.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to hold source files data
 *
 * @author Dominik Rączka
 * @version 0.9
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
     * Pure source code - only code lines
     */
    private List<SourceLine> PureSourceLinesList;
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
    public List<SourceLine> getPureSourceLinesList() {
        return PureSourceLinesList;
    }
    public void setPureSourceLinesList(List<SourceLine> pureSourceLinesList) {
        PureSourceLinesList = pureSourceLinesList;
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
        PureSourceLinesList = new ArrayList<>();
        LinesNumber = SourceLinesList.size();
        SourceFileInfo = new SourceFileInfo();
    }

    /**
     * Constructor to initialize object with full source file
     * @param listToInitialize source file content
     */
    public SourceCodeFile(List<SourceLine> listToInitialize){
        SourceLinesList = new ArrayList<>(listToInitialize);
        PureSourceLinesList = new ArrayList<>();

        for(SourceLine sourceLine : listToInitialize) {
            parseInfo(sourceLine.SourceLineContent);
        }

        LinesNumber = SourceLinesList.size();
        extractPureSource();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Adds and parses line of source code
     * @param sourceLine
     */
    public void addSourceLine(SourceLine sourceLine){
        parseInfo(sourceLine.SourceLineContent);
        if(sourceLine.processSourceLine()){
            SourceLinesList.add(sourceLine);
        }
        LinesNumber = SourceLinesList.size();
    }

    /**
     * Extracts "pure" code from source file
     */
    public void extractPureSource(){
        for(SourceLine sourceLine : SourceLinesList){
            String lineContent = sourceLine.SourceLineContent;
            lineContent = lineContent.replaceAll("   ", "");
            while(lineContent.startsWith(" ")){
                lineContent = lineContent.substring(1);
            }
            if(!lineContent.startsWith("/*") && !lineContent.startsWith("*")
                    && !lineContent.startsWith("*/") && !lineContent.startsWith("/")
                    && (lineContent.length() != 0) && !lineContent.startsWith("@")){
                PureSourceLinesList.add(sourceLine);
            }
        }
    }

    public void clearEmptyLines(){
        List<SourceLine> listToSave = new ArrayList<>();

        for(SourceLine sourceLine : SourceLinesList){
            if(sourceLine.SourceLineContent.length() != 0){
                listToSave.add(sourceLine);
            }
        }

        SourceLinesList.clear();
        SourceLinesList.addAll(listToSave);
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
            result += sourceLine.SourceLineContent + "\n";
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
        if(SourceFileInfo.AuthorName.equals("none") && lineToParse.contains(AUTHORDELIMETER)){
            SourceFileInfo.AuthorName = lineToParse.substring(lineToParse.indexOf(AUTHORDELIMETER) + AUTHORDELIMETER.length() + 1);
        } else if(SourceFileInfo.Version.equals("none") && lineToParse.contains(VERSIONDELIMETER)){
            SourceFileInfo.Version = lineToParse.substring(lineToParse.indexOf(VERSIONDELIMETER) + VERSIONDELIMETER.length() + 1);
        }
    }
}
