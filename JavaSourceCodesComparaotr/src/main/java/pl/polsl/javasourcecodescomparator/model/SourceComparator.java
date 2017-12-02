package pl.polsl.javasourcecodescomparator.model;

import com.sun.org.apache.bcel.internal.classfile.SourceFile;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dominik
 * @version 0.1
 */


public class SourceComparator {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Class private fields
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * All java source files to compare
     */
    private List<SourceCodeFile> SourceFilesToCompareList;

    private List<ResultData> ResultDataList;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters and setters
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<SourceCodeFile> getSourceFilesToCompareList() {
        return SourceFilesToCompareList;
    }
    public void setSourceFilesToCompareList(List<SourceCodeFile> sourceFilesToCompareList) {
        this.SourceFilesToCompareList = sourceFilesToCompareList;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public SourceComparator(){
        SourceFilesToCompareList = new ArrayList<>();
        ResultDataList = new ArrayList<>();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void compareAllFiles(){
        for(SourceCodeFile sourceFile : SourceFilesToCompareList){
            compareFileWithOthers(sourceFile);
        }
    }

    public void findLongestCommonParts(){
        for(ResultData resultData : ResultDataList){
            resultData.clearGarbageResults();
            resultData.findLongestCommonParts();
        }
    }

    public String getTotalResultString() {
        String result = "";

        for(ResultData resultData : ResultDataList){
            result += resultData.toString() + "\n";
        }

        return result;
    }

    public String getLongestCommonPartsString(){
        String result = "";

        for(ResultData resultData : ResultDataList){
            result += resultData.longestCommonPartsToString() + "\n";
        }

        return result;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Private methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void compareFileWithOthers(SourceCodeFile originSourceCodeFile){
        for(SourceCodeFile sourceFile : SourceFilesToCompareList){
            ResultData resultData = new ResultData();
            resultData.OriginSource = originSourceCodeFile.getSourceFileName();
            //if(sourceFile.getAuthor() != originSourceCodeFile.getAuthor()){
                resultData.SimilarSourcesList.add(sourceFile.getSourceFileName());
                resultData.MatchingLinesMap.put(sourceFile.getSourceFileName(), compareCodeFiles(originSourceCodeFile, sourceFile));
                ResultDataList.add(resultData);
            //}
        }
    }

    private List<MatchedLine> compareCodeFiles(SourceCodeFile originSource, SourceCodeFile sourceToCompare){
        List<MatchedLine> resultList = new ArrayList<>();

        int i = 0;
        for(String originLine : originSource.getSourceLinesList()){
            i++;
            int j = 0;
            for(String lineToCompare : sourceToCompare.getSourceLinesList()){
                j++;
                if(originLine.equals(lineToCompare)){
                    resultList.add(new MatchedLine(i, j, originLine));
                }
            }
        }

        return  resultList;
    }
}
