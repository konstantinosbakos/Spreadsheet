package ExpressionHandler.SpreadsheetNodes;

import java.util.ArrayList;

import Spreadsheet.Spreadsheet;

public class MaxNode extends FunctionNode {
    public MaxNode(ArrayList<Node> children){
        super(children);
    }

    public ArrayList<Double> getValue(Spreadsheet spreadsheet){
        //Checks the values of its children and returns a list
        //with one value, the maximum.
        ArrayList<Double> value = new ArrayList<>();

        value.add(children.stream()
                .flatMap(node -> node.getValue(spreadsheet).stream())
                .max(Double::compare)
                .orElse(Double.NaN));

        return value;
    }
}
