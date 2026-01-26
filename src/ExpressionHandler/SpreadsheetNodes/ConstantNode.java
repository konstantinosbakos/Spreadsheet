package ExpressionHandler.SpreadsheetNodes;

import java.util.ArrayList;

import Spreadsheet.Spreadsheet;

public class ConstantNode extends Node {
    ArrayList<Double> value;

    public ConstantNode(double value){
        super("Constant");

        this.value = new ArrayList<>();

        this.value.add(value);
    }

    public ArrayList<Double> getValue(Spreadsheet spreadsheet){
        return value;
    }
}
