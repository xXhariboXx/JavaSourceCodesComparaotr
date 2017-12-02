package pl.polsl.javasourcecodescomparator.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultData {

    public String OriginSource;

    public List<String> SimilarSourcesList;

    public Map<String, List<MatchedLine>> MatchingLinesMap;

    public Map<String, List<MatchedLine>> LongestCommonPartsMap;

    private int LongestLineLength;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ResultData(){
        SimilarSourcesList = new ArrayList<>();
        MatchingLinesMap = new HashMap<>();
        LongestCommonPartsMap = new HashMap<>();
        this.LongestLineLength = 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void findLongestCommonParts(){
        for(String similarSource : MatchingLinesMap.keySet()){
            MatchedLine previousMatchedLine = new MatchedLine();
            List<MatchedLine> longestMatchedLines = new ArrayList<>();

            for(MatchedLine matchedLine : MatchingLinesMap.get(similarSource)) {
                if(previousMatchedLine.OriginLineNumber == -1){
                    previousMatchedLine = matchedLine;
                } else{
                    if(previousMatchedLine.OriginLineNumber == (matchedLine.OriginLineNumber - 1)){
                        longestMatchedLines.add(previousMatchedLine);
                    } else if(longestMatchedLines.size() > 0 && longestMatchedLines.get(longestMatchedLines.size() - 1).equals(previousMatchedLine)){
                        longestMatchedLines.add(previousMatchedLine);
                    }
                    previousMatchedLine = matchedLine;
                }

                if(matchedLine.LineContent.length() > LongestLineLength){
                    LongestLineLength = matchedLine.LineContent.length();
                }
            }
            
            LongestCommonPartsMap.put(similarSource, longestMatchedLines);
        }
    }

    public String longestCommonPartsToString(){
        String result = "";

        result += "Origin source name: " + OriginSource + "\n";
        for(String similarSource : LongestCommonPartsMap.keySet()){
            result += "Similar source name: " + similarSource + "\n";
            for(MatchedLine matchedLine : LongestCommonPartsMap.get(similarSource)){
                matchedLine.LongestLineLength = LongestLineLength;
                result += matchedLine.toString() + "\n";
            }
        }

        return result;
    }

    public void clearGarbageResults(){
        for(Map.Entry<String, List<MatchedLine>> entry : MatchingLinesMap.entrySet()){
            entry.setValue(clearGarbageResultsFromSourceFile(entry.getValue()));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Public override methods
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public String toString() {
        String result = "";

        result += "Origin source name: " + OriginSource + "\n";
        for(String similarSource : MatchingLinesMap.keySet()){
            result += "Similar source name: " + similarSource + "\n";
            for(MatchedLine matchedLine : MatchingLinesMap.get(similarSource)){
                result += matchedLine.toString() + "\n";
            }
        }

        return result;
    }

    private List<MatchedLine> clearGarbageResultsFromSourceFile(List<MatchedLine> matchedLines){
        List<MatchedLine> nonGarbageLines = new ArrayList<>();
        MatchedLine previousMatchedLine = new MatchedLine();

        for(MatchedLine line : matchedLines){
            if(previousMatchedLine.OriginLineNumber == -1) {
                previousMatchedLine = line;
            }

            if((previousMatchedLine.OriginLineNumber == (line.OriginLineNumber - 1)) || !line.iSGarbage()) {
                nonGarbageLines.add(line);
            }

            previousMatchedLine = line;
        }

        return nonGarbageLines;
    }
}
