package pl.polsl.javasourcecodescomparator.exceptions;

import java.io.File;

/**
 * Exception to catch reading projects errors
 * @author Dominik Rączka
 * @version 1.0
 */
public class WrongFileExtensionException extends  Exception {
    /**
     * File name of error file
     */
    private String fileName;
    /**
     * Extension of the error file
     */
    private String fileExtension;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Default constructor
     * @param message message to put inside exception
     */
    public WrongFileExtensionException(String message){
        super(message);
        fileName = "Unknown";
        fileExtension = "Unknown";
    }
    /**
     * Customized constructor to include file data
     * @param wrongFile File class object with error
     */
    public WrongFileExtensionException(File wrongFile){
        super("Wrong file extension.");
        fileName = wrongFile.getName();
        fileExtension = wrongFile.getPath().substring(wrongFile.getPath().lastIndexOf('.') + 1);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public override methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Prints customized error message
     * @return customized message String
     */
    @Override
    public String getMessage() {
        return super.getMessage() + " " + fileName + " has \"" + fileExtension + "\"";
    }
}
