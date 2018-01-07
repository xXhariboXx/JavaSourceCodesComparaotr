package pl.polsl.javasourcecodescomparator.model;

/**
 * Class that represents source file information
 *
 * @author Dominik Rączka
 * @version 1.0
 */
public class SourceFileInfo {
    /**
     * Name of source file
     */
    public String fileName;
    /**
     * Name of author of the source file
     */
    public String authorName;
    /**
     * version of source file
     */
    public String version;
    /**
     * Project name of source file
     */
    public String projectName;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor. Initializes object
     */
    public SourceFileInfo(){
        fileName = "none";
        authorName = "none";
        version = "none";
        projectName = "none";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public override methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "";

        result += "Project name: " + projectName + "\n";
        result += "File name: " + fileName + ", Author name: " + authorName + ", version: " + version;

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if(((SourceFileInfo) obj) != null){
            SourceFileInfo objToCompare = (SourceFileInfo) obj;
            result = objToCompare.authorName.equals(authorName) &&
                    objToCompare.fileName.equals(fileName) &&
                    objToCompare.version.equals(version) &&
                    objToCompare.projectName.equals(projectName);
        }

        return result;
    }
}
