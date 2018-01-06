package pl.polsl.javasourcecodescomparator.model;

/**
 * Class that represents matched line in two source files
 *
 * @author Dominik Rączka
 * @version 1.0
 */
public class MatchedLine {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Class public fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Source line number in origin files
     */
    public Integer originLineNumber;
    /**
     * Source line number in compared file
     */
    public Integer comparedLineNumber;
    /**
     * Content of source line in both files
     */
    public String lineContent;
    /**
     * Length of the longest matched line in file. Used for better representing data in terminal
     */
    private int longestLineLength;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int getLongestLineLength() {
        return longestLineLength;
    }
    public void setLongestLineLength(int longestLineLength) {
        this.longestLineLength = longestLineLength;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor. Initializes object
     */
    public MatchedLine(){
        this.originLineNumber = -1;
        this.comparedLineNumber = -1;
        this.lineContent = "";
    }
    /**
     * Initializes object with data
     * @param originLineNumber number of line in origin file
     * @param comparedLineNumber number of line in compared file
     * @param lineContent content of the line
     */
    public MatchedLine(int originLineNumber, int comparedLineNumber, String lineContent){
        this.originLineNumber = originLineNumber;
        this.comparedLineNumber = comparedLineNumber;
        this.lineContent = lineContent;
        this.longestLineLength = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Checks if line is empty
     * @return true if line is empty
     */
    public boolean iSGarbage(){
        boolean bIsGarbage = false;

        while (lineContent.startsWith(" ")){
            lineContent = lineContent.substring(1);
        }

        if(lineContent.length() <= 1){
            bIsGarbage = true;
        }

        return  bIsGarbage;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public override methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "";

        result += /*"Origin source content: " + */lineContent;
        int lineLength = lineContent.length();
        while(result.length() < longestLineLength){
            result += " ";
        }
        result += "|Line number in origin file: " + originLineNumber.toString() + ", line number in compared file: " + comparedLineNumber.toString();

        return result;
    }
}
