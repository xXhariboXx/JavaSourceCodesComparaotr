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

    public void comparePureSources(){
        for(SourceCodeFile sourceCodeFile : SourceFilesToCompareList){
            sourceCodeFile.extractPureSource();
            compareFileWithOthersPureSource(sourceCodeFile);
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
            resultData.OriginSource = originSourceCodeFile.getSourceFileInfo();
            if(!areSourcesFromTheSameProject(originSourceCodeFile, sourceFile) && !areSourcesTheSameVersion(originSourceCodeFile, sourceFile)){
                resultData.SimilarSourcesList.add(sourceFile.getSourceFileInfo().FileName);
                if(resultData.MatchingLinesMap.containsKey(sourceFile.getSourceFileInfo())){
                    resultData.MatchingLinesMap.get(sourceFile.getSourceFileInfo()).addAll(compareCodeFiles(originSourceCodeFile, sourceFile));
                } else{
                    resultData.MatchingLinesMap.put(sourceFile.getSourceFileInfo(), compareCodeFiles(originSourceCodeFile, sourceFile));
                }
                ResultDataList.add(resultData);
            }
        }
    }

    private void compareFileWithOthersPureSource(SourceCodeFile originSourceCodeFile){
        for(SourceCodeFile sourceFile : SourceFilesToCompareList){
            ResultData resultData = new ResultData();
            resultData.OriginSource = originSourceCodeFile.getSourceFileInfo();
            if(!areSourcesFromTheSameProject(originSourceCodeFile, sourceFile) && !areSourcesTheSameVersion(originSourceCodeFile, sourceFile)){
                resultData.SimilarSourcesList.add(sourceFile.getSourceFileInfo().FileName);
                if(resultData.MatchingLinesMap.containsKey(sourceFile.getSourceFileInfo())){
                    resultData.MatchingLinesMap.get(sourceFile.getSourceFileInfo()).addAll(compareCodeFilesPureSources(originSourceCodeFile, sourceFile));
                } else{
                    resultData.MatchingLinesMap.put(sourceFile.getSourceFileInfo(), compareCodeFilesPureSources(originSourceCodeFile, sourceFile));
                }
                ResultDataList.add(resultData);
            }
        }
    }

    private boolean areSourcesTheSame(SourceCodeFile originSourceCodeFile, SourceCodeFile sourceCodeFileToCompare){
        return originSourceCodeFile.getSourceFileInfo().equals(sourceCodeFileToCompare.getSourceFileInfo());
    }

    private  boolean areSourcesFromTheSameProject(SourceCodeFile originSourceCodeFile, SourceCodeFile sourceCodeFileToCompare){
        return originSourceCodeFile.getSourceFileInfo().ProjectName.equals(sourceCodeFileToCompare.getSourceFileInfo().ProjectName);
    }

    private  boolean areSourcesTheSameVersion(SourceCodeFile originSourceCodeFile, SourceCodeFile sourceCodeFileToCompare){
        return originSourceCodeFile.getSourceFileInfo().AuthorName.equals(sourceCodeFileToCompare.getSourceFileInfo().AuthorName) &&
                originSourceCodeFile.getSourceFileInfo().Version.equals(sourceCodeFileToCompare.getSourceFileInfo().Version);
    }

    private List<MatchedLine> compareCodeFiles(SourceCodeFile originSource, SourceCodeFile sourceToCompare){
        List<MatchedLine> resultList = new ArrayList<>();

        for(SourceLine originSourceLine : originSource.getSourceLinesList()){
            for(SourceLine sourceLineToCompare : sourceToCompare.getSourceLinesList()){
                if (!sourceLineToCompare.WasSourceLineMatched ) {
                    if (originSourceLine.SourceLineContent.equals(sourceLineToCompare.SourceLineContent)) {
                        sourceLineToCompare.WasSourceLineMatched = true;
                        resultList.add(new MatchedLine(originSourceLine.SourceLineIndex, sourceLineToCompare.SourceLineIndex, originSourceLine.SourceLineContent));
                        break;
                    }
                }
            }
        }

        return  resultList;
    }

    private List<MatchedLine> compareCodeFilesPureSources(SourceCodeFile originSource, SourceCodeFile sourceToCompare){
        List<MatchedLine> resultList = new ArrayList<>();

        for(SourceLine originSourceLine : originSource.getPureSourceLinesList()){
            for(SourceLine sourceLineToCompare : sourceToCompare.getPureSourceLinesList()){
                if (!sourceLineToCompare.WasSourceLineMatched ) {
                    if (originSourceLine.SourceLineContent.equals(sourceLineToCompare.SourceLineContent)) {
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
