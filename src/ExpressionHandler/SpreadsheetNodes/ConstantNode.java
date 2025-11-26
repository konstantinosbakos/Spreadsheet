package ExpressionHandler.SpreadsheetNodes;

import Spreadsheet.Spreadsheet;

public class ConstantNode implements Node {
    double value;

    public ConstantNode(double value) {
        this.value = value;
    }

    public double getValue(Spreadsheet spreadsheet) {
        return value;
    }
}
