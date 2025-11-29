package ExpressionHandler.SpreadsheetNodes;

import Spreadsheet.Spreadsheet;

import java.util.ArrayList;

public class SumNode extends FunctionNode {
    public SumNode(ArrayList<Node> children) {
        super(children);
    }

    public double getValue(Spreadsheet spreadsheet) {
        double  sum = 0;

        for(Node child : children){
            sum += child.getValue(spreadsheet);
        }

        return sum;
    }
}
