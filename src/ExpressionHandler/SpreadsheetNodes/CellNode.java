package ExpressionHandler.SpreadsheetNodes;

import java.util.ArrayList;

import Spreadsheet.Spreadsheet;

public class CellNode extends Node{
    /*
     * CellNode is used both for Cells and
     * Ranges. Using getCellValues it gets an
     * array of values from the range. It does
     * not get the Cells themselves.
     */
    private final String coords;

    public CellNode(String coords){
        super((coords.contains(":") ? "Range" : "Cell"));

        this.coords = coords;
    }

    public ArrayList<Double> getValue(Spreadsheet spreadsheet){
        return spreadsheet.getCellValues(coords);
    }

}
