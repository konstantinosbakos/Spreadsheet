package ExpressionHandler.SpreadsheetNodes;

import java.util.List;
import java.util.ArrayList;

import Spreadsheet.Spreadsheet;

public class ErrorNode extends Node {
    public ErrorNode(){
        super("Error");
    }

    public ArrayList<Double> getValue(Spreadsheet spreadsheet){
        //The error nodes returns only one value, NaN.
        return new ArrayList<>(List.of(Double.NaN));
    }
}
