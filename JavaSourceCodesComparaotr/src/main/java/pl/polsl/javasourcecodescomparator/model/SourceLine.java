package pl.polsl.javasourcecodescomparator.model;

/**
 * Class that represents source line and its information
 *
 * @author Dominik Rączka
 * @version 1.0
 */
public class SourceLine {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Class public fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Content of source line
     */
    public String sourceLineContent;
    /**
     * Content of processed lines - only important data
     */
    public String processedLineContent;
    /**
     * Index of source line
     */
    public int sourceLineIndex;
    /**
     * True if source line was matched by comparator
     */
    public  boolean wasSourceLineMatched;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Creates new SourceLine. Sets WasLineMatched to false by default
     * @param sourceLineContent content of source line to create
     * @param sourceLineIndex index of source line to create
     */
    public  SourceLine(String sourceLineContent, int sourceLineIndex){
        this.sourceLineContent = sourceLineContent;
        this.sourceLineIndex = sourceLineIndex;
        this.wasSourceLineMatched = false;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Process source line.
     * @return Returns true if source line is valuable
     */
    public boolean processSourceLine(){
        if(sourceLineContent.contains("{")) {
            sourceLineContent = sourceLineContent.replaceAll("\\{", "");
        }
        if(sourceLineContent.contains("}")) {
            sourceLineContent = sourceLineContent.replaceAll("}", "");
        }
        if(sourceLineContent.contains("\t")) {
            sourceLineContent = sourceLineContent.replaceAll("\t", "");
        }

        while(sourceLineContent.startsWith(" ")){
            sourceLineContent = sourceLineContent.substring(1);
        }

        processedLineContent = sourceLineContent;
        processedLineContent.toLowerCase();

        if(processedLineContent.contains("break")) {
            processedLineContent = processedLineContent.replaceAll("break;", "");
        }
        if(processedLineContent.contains("return")) {
            processedLineContent = processedLineContent.replaceAll("return;", "");
        }
        if(processedLineContent.contains("@author")){
            processedLineContent = processedLineContent.replaceAll("@author", "");
        }
        if(processedLineContent.contains("@test")){
            processedLineContent = processedLineContent.replaceAll("@test", "");
        }
        if(processedLineContent.contains("@override")){
            processedLineContent = processedLineContent.replaceAll("@Override", "");
        }
        if(processedLineContent.contains("try")){
            processedLineContent = processedLineContent.replaceAll("try", "");
        }
        if(processedLineContent.contains("else")){
            processedLineContent = processedLineContent.replaceAll("else", "");
        }

        return  validateSourceLine();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Validates if source line has any source
     * @return tru if have any source
     */
    private boolean validateSourceLine(){
        boolean result = true;

        if(processedLineContent.length() == 0){
            result = false;
        } else if(processedLineContent.startsWith("//")){
            result = false;
        } else if(processedLineContent.startsWith("/**")){
            result = false;
        } else if(processedLineContent.startsWith("/*")){
            result = false;
        } else if(processedLineContent.startsWith("*/")){
            result = false;
        } else if (processedLineContent.startsWith("*")){
            result = false;
        } else if(processedLineContent.contains("import")) {
            result = false;
        } else if(processedLineContent.contains("package")) {
            result = false;
        } else if(processedLineContent.length() == 1 && processedLineContent.equals(";")){
            result = false;
        }

        return  result;
    }
}
