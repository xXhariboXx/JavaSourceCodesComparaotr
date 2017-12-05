package pl.polsl.javasourcecodescomparator.model;

/**
 * Class that represents source file information
 *
 * @author Dominik Rączka
 * @version 0.9
 */
public class SourceFileInfo {
    /**
     * Name of source file
     */
    public String FileName;
    /**
     * Name of author of the source file
     */
    public String AuthorName;
    /**
     * Version of source file
     */
    public String Version;
    /**
     * Project name of source file
     */
    public String ProjectName;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Empty constructor. Initializes object
     */
    public SourceFileInfo(){
        FileName = "none";
        AuthorName = "none";
        Version = "none";
        ProjectName = "none";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public override methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "";

        result += "Project name: " + ProjectName + "\n";
        result += "File name: " + FileName + ", Author name: " + AuthorName + ", version: " + Version;

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if(((SourceFileInfo) obj) != null){
            SourceFileInfo objToCompare = (SourceFileInfo) obj;
            result = objToCompare.AuthorName.equals(AuthorName) &&
                    objToCompare.FileName.equals(FileName) &&
                    objToCompare.Version.equals(Version) &&
                    objToCompare.ProjectName.equals(ProjectName);
        }

        return result;
    }
}
