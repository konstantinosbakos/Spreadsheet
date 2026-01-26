package DataStructure;

import java.util.List;
import java.util.ArrayList;

import SpreadsheetCell.*;

public abstract class DataStructure{
    abstract public Cell setCell(Cell newCell);
    abstract public Cell getCell(String coords);
    abstract public void deleteCell(String coords);

    abstract public ArrayList<Integer> getSortedRowKeys();
    abstract public ArrayList<Cell> getRange(String range);

    public Cell createCell(String coords, String content){
        /*
         * Creates a Cell based on the content provided.
         * '==' at the beginning of the content is special
         * cased to create GhostCells.
         * '=' is special cased for FormulaCells.
         */
        Cell newCell;

        String[] split_coords = splitCoords(coords);
        String   col          = split_coords[0];
        String   row          = split_coords[1];

        boolean isNumber  = true;
        boolean isFormula = true;
        boolean isGhost   = true;

        try{Double.parseDouble(content);}
        catch(NumberFormatException _){isNumber = false;}

        if(content.length() <= 1){
            isGhost   = false;
            isFormula = false;
        }
        else{
            if(content.charAt(0) != '=' || content.charAt(1) != '='){
                isGhost   = false;
            }

            if(content.charAt(0) != '=' || (content.charAt(0) == '=' && isGhost)){
                isFormula = false;
            }
        }

        if(isFormula){
            newCell = new FormulaCell(col, row);
        }
        else if(isGhost){
            content = "0";
            newCell = new GhostCell(col, row);
        }
        else if(isNumber){
            newCell = new NumberCell(col, row);
        }
        else{
            newCell = new TextCell(col, row);
        }

        return newCell.setCellContent(content) ? newCell : null;
    }

    public static int colToNumber(String col){
        int num = 0;

        for(int i = 0; i < col.length(); i++){
            num = num * 26 + (col.charAt(i) - 'A' + 1);
        }

        return num - 1;
    }

    public static int getMaxRowElements(String LastElement){
        int maxElem = 0;

        for(int i = 0; i < LastElement.length(); i++){
            maxElem = maxElem * 26 + (LastElement.charAt(i) - 'A' + 1);
        }

        return maxElem;
    }

    public static String nextCol(String col){
        /*
         * This is a function that follows the
         * Semi-alphabetic order of Spreadsheets.
         * For example, if we have col='Z', the
         * next column will be 'AA'.
         */
        char[] chars = col.toCharArray();
        int        i = chars.length - 1;

        while(i >= 0){
            if(chars[i] != 'Z'){
                chars[i]++;

                break;
            }
            else{
                chars[i] = 'A';
                i--;
            }
        }

        if(i < 0){
            return "A" + new String(chars);
        }
        else{
            return new String(chars);
        }
    }

    public static String[] splitRange(String range){
        //"A1:B1" -> "A1","B1"
        int index = 0;

        while(index < range.length()){
            if(range.charAt(index) == ':'){
                break;
            }

            index++;
        }

        String from = range.substring(0, index);
        String to   = range.substring(index + 1);

        return new String[]{from, to};
    }

    public static List<String> getRangeCellCoords(String range){
        //Returns all cells that exist in a range (existing or not).
        String[] split_range = splitRange(range);
        String   from        = split_range[0];
        String   to          = split_range[1];

        String[] splitFrom   = splitCoords(from);
        String[] splitTo     = splitCoords(to);

        String fromCol  = splitFrom[0];
        int    fromRow  = Integer.parseInt(splitFrom[1]);
        String toCol    = splitTo[0];
        int    toRow    = Integer.parseInt(splitTo[1]);
        int    toColNum = colToNumber(toCol);

        List<String> rangeAllCells = new ArrayList<>();

        int i = colToNumber(fromCol);
        do{
            for(int j=fromRow; j<=toRow; j++){
                rangeAllCells.add(numberToCol(i) + j);
            }

            i++;
        } while(i<=toColNum);

        return rangeAllCells;
    }

    public static List<String> getColumnsBetween(String start, String end){
        int startNum = colToNumber(start);
        int endNum   = colToNumber(end);

        List<String> columns = new ArrayList<>();

        for (int i = startNum; i <= endNum; i++){
            columns.add(numberToCol(i));
        }

        return columns;
    }

    protected static String[] splitCoords(String coords){
        int index = 0;

        while(index < coords.length() && Character.isLetter(coords.charAt(index))){
            index++;
        }

        String col = coords.substring(0, index);
        String row = coords.substring(index);

        return new String[]{col, row};
    }

    protected static boolean isColSmaller(String col1, String col2){
        return DataStructure.colToNumber(col1) < DataStructure.colToNumber(col2);
    }

    private static String numberToCol(int num){
        /*
         * Following the Spreadsheet
         * semi-alphabetic order, turn a number
         * to a spreadsheet column.
         */
        String col = "";

        num++;

        while (num > 0){
            int rem = num % 26;

            if (rem == 0){
                rem = 26;
            }

            char c = (char) ('A' + rem - 1);

            col = c + col;
            num = (num - rem) / 26;
        }

        return col;
    }
}
