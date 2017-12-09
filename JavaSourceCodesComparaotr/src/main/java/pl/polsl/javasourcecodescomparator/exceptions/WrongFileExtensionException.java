package pl.polsl.javasourcecodescomparator.exceptions;

import java.io.File;

public class WrongFileExtensionException extends  Exception {

    private String FileName;

    private String FileExtension;

    public WrongFileExtensionException(String message){
        super(message);
    }

    public WrongFileExtensionException(File wrongFile){
        super("Wrong file extension.");
        FileName = wrongFile.getName();
        FileExtension = wrongFile.getPath().substring(wrongFile.getPath().lastIndexOf('.') + 1);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " " + FileName + " has \"" +  FileExtension + "\"";
    }
}
