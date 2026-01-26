package ExpressionHandler.SpreadsheetNodes;

import java.util.ArrayList;

import Spreadsheet.Spreadsheet;

public class MinNode extends FunctionNode {
    public MinNode(ArrayList<Node> children){
        super(children);
    }

    public ArrayList<Double> getValue(Spreadsheet spreadsheet){
        //Checks the values of its children and returns a list
        //with one value, the minimum.
        ArrayList<Double> value = new ArrayList<>();

        value.add(children.stream()
                .flatMap(node -> node.getValue(spreadsheet).stream())
                .min(Double::compare)
                .orElse(Double.NaN));

        return value;
    }
}
