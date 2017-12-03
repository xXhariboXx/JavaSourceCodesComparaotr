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
        clearGarbage();
    }

    public void findLongestCommonParts(){
        clearGarbage();
        for(ResultData resultData : ResultDataList){
            //resultData.findLongestCommonParts();
        }
    }

    public String getTotalResultString() {
        String result = "";

        for(ResultData resultData : ResultDataList){
            resultData.formatResultData();
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
            if(!areSourcesTheSame(originSourceCodeFile, sourceFile)){
                resultData.SimilarSourcesList.add(sourceFile.getSourceFileName());
                resultData.MatchingLinesMap.put(sourceFile.getSourceFileName(), compareCodeFiles(originSourceCodeFile, sourceFile));
                ResultDataList.add(resultData);
            }
        }
    }

    private boolean areSourcesTheSame(SourceCodeFile originSourceCodeFile, SourceCodeFile sourceCodeFileToCompare){
        return (originSourceCodeFile.getAuthor() == sourceCodeFileToCompare.getAuthor()) &&
                (originSourceCodeFile.getVersion() == sourceCodeFileToCompare.getVersion()) &&
                (originSourceCodeFile.getProjectName() == sourceCodeFileToCompare.getProjectName());
    }

    private List<MatchedLine> compareCodeFiles(SourceCodeFile originSource, SourceCodeFile sourceToCompare){
        List<MatchedLine> resultList = new ArrayList<>();
        int lastMatchedIndex = -1;

        for(SourceLine originSourceLine : originSource.getSourceLinesList()){

            for(SourceLine sourceLineToCompare : sourceToCompare.getSourceLinesList()){

                if (!sourceLineToCompare.WasSourceLineMatched ) {
                    if (originSourceLine.SourceLineContent.equals(sourceLineToCompare.SourceLineContent)) {
                        //lastMatchedIndex = sourceLineToCompare.SourceLineIndex;
                        sourceLineToCompare.WasSourceLineMatched = true;
                        resultList.add(new MatchedLine(originSourceLine.SourceLineIndex, sourceLineToCompare.SourceLineIndex, originSourceLine.SourceLineContent));
                        break;
                    }
                }
            }
        }

        return  resultList;
    }

    private void clearGarbage(){
        ArrayList<ResultData> listToSave = new ArrayList<>();

        for(ResultData resultData : ResultDataList){
            resultData.clearGarbageResults();
            if(resultData.haveMatchingLines()){
                listToSave.add(resultData);
            }
        }

        ResultDataList.clear();
        ResultDataList.addAll(listToSave);
    }
}
