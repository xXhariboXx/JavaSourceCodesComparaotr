package pl.polsl.javasourcecodescomparator.model;

/**
 * Class that represents source line and its information
 *
 * @author Dominik Rączka
 * @version 0.9
 */
public class SourceLine {
    /**
     * Content of source line
     */
    public String SourceLineContent;
    /**
     * Indes of source line
     */
    public int SourceLineIndex;
    /**
     * True if source line was matched by comparator
     */
    public  boolean WasSourceLineMatched;

    /**
     * Creates new SourceLine. Sets WasLineMatched to false by default
     * @param sourceLineContent content of source line to create
     * @param sourceLineIndex index of source line to create
     */
    public  SourceLine(String sourceLineContent, int sourceLineIndex){
        this.SourceLineContent = sourceLineContent;
        this.SourceLineIndex = sourceLineIndex;
        this.WasSourceLineMatched = false;
    }
}
