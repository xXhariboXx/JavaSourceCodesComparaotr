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
    public Integer OriginLineNumber;
    /**
     * Source line number in compared file
     */
    public Integer ComparedLineNumber;
    /**
     * Content of source line in both files
     */
    public String LineContent;
    /**
     * Length of the longest matched line in file. Used for better representing data in terminal
     */
    private int LongestLineLength;

    public int getLongestLineLength() {
        return LongestLineLength;
    }

    public void setLongestLineLength(int longestLineLength) {
        LongestLineLength = longestLineLength;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor. Initializes object
     */
    public MatchedLine(){
        this.OriginLineNumber = -1;
        this.ComparedLineNumber = -1;
        this.LineContent = "";
    }
    /**
     * Initializes object with data
     * @param originLineNumber number of line in origin file
     * @param comparedLineNumber number of line in compared file
     * @param lineContent content of the line
     */
    public MatchedLine(int originLineNumber, int comparedLineNumber, String lineContent){
        this.OriginLineNumber = originLineNumber;
        this.ComparedLineNumber = comparedLineNumber;
        this.LineContent = lineContent;
        this.LongestLineLength = 0;
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

        while (LineContent.startsWith(" ")){
            LineContent = LineContent.substring(1);
        }

        if(LineContent.length() <= 1){
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

        result += /*"Origin source content: " + */LineContent;
        while((LineContent.length() + result.length()) < LongestLineLength){
            result += " ";
        }
        result += "|Line number in origin file: " + OriginLineNumber.toString() + ", line number in compared file: " + ComparedLineNumber.toString();

        return result;
    }
}
