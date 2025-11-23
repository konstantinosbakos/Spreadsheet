package DataStructure;

import Cell.*;
import java.util.ArrayList;
import java.util.List;

public abstract class DataStructure {
    abstract Cell setCell(String coords, String content);
    abstract Cell getCell(String coords);
    abstract void emptyCell(String coords);

    abstract ArrayList<Cell> getRange(String from, String to);

    abstract String[] getSortedRowKeys();

    public String nextCol(String col) {
        char[] chars = col.toCharArray();
        int i = chars.length - 1;

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

    public static int getMaxRowElements(String LastElement){
        int maxElem = 0;

        for(int i = 0; i < LastElement.length(); i++){
            maxElem = maxElem * 26 + (LastElement.charAt(i) - 'A' + 1);
        }

        return maxElem;
    }

    private static String numberToCol(int num) {
        String col = "";

        num++;

        while (num > 0) {
            int rem = num % 26;

            if (rem == 0) {
                rem = 26;
            }

            char c = (char) ('A' + rem - 1);

            col = c + col;
            num = (num - rem) / 26;
        }

        return col;
    }

    public static int colToNumber(String col){
        int num = 0;

        for(int i = 0; i < col.length(); i++){
            num = num * 26 + (col.charAt(i) - 'A' + 1);
        }

        return num - 1;
    }

    public static List<String> getColumnsBetween(String start, String end) {
        int startNum = colToNumber(start);
        int endNum = colToNumber(end);

        List<String> columns = new ArrayList<>();

        for (int i = startNum; i <= endNum; i++) {
            columns.add(numberToCol(i));
        }

        return columns;
    }

    public String nextRow(String row) {
        int number = Integer.parseInt(row);

        number++;

        return Integer.toString(number);
    }


    protected static boolean isColSmaller(String col1, String col2) {
        return DataStructure.colToNumber(col1) < DataStructure.colToNumber(col2);
    }

    protected static boolean isRowSmaller(String row1, String row2) {
        return Integer.parseInt(row1) < Integer.parseInt(row2);
    }

    protected static String[] splitCoords(String coords) {
        int index = 0;

        while(index < coords.length() && Character.isLetter(coords.charAt(index))) {
            index++;
        }

        String col = coords.substring(0, index);
        String row = coords.substring(index);

        return new String[]{col, row};
    }

    protected Cell createCell(String coords, String content){
        Cell newCell;

        String[] split_coords = splitCoords(coords);
        String col = split_coords[0];
        String row = split_coords[1];
        boolean isNumber  = true;
        boolean isFormula = true;

        try{Double.parseDouble(content);}
        catch(NumberFormatException _){isNumber = false;}

        if(content.charAt(0) != '='){
            isFormula = false;
        }

        if(isFormula){
            newCell = new FormulaCell(col, row);
        }
        else if(isNumber){
            newCell = new NumberCell(col, row);
        }
        else{
            newCell = new TextCell(col, row);
        }

        return newCell.setCellContent(content) ? newCell : null;
    }

}
