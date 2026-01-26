package ExpressionHandler.SpreadsheetNodes;

import java.util.ArrayList;

import Spreadsheet.Spreadsheet;

public class SumNode extends FunctionNode {
    public SumNode(ArrayList<Node> children){
        super(children);
    }

    public ArrayList<Double> getValue(Spreadsheet spreadsheet){
        //Adds up the values of its children and returns a list
        //with one value, the sum.
        ArrayList<Double> value = new ArrayList<>();

        value.add(children.stream()
                .flatMap(node -> node.getValue(spreadsheet).stream())
                .mapToDouble(Double::doubleValue)
                .sum());

        return value;
    }
}
