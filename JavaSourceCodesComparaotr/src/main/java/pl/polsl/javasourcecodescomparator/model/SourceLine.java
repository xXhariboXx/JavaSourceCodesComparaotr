package pl.polsl.javasourcecodescomparator.model;

public class SourceLine {

    public String SourceLineContent;

    public int SourceLineIndex;

    public  boolean WasSourceLineMatched;

    public  SourceLine(String sourceLineContent, int sourceLineIndex){
        this.SourceLineContent = sourceLineContent;
        this.SourceLineIndex = sourceLineIndex;
        this.WasSourceLineMatched = false;
    }
}
