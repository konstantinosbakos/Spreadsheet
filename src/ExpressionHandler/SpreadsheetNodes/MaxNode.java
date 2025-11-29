package ExpressionHandler.SpreadsheetNodes;

import Spreadsheet.Spreadsheet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class MaxNode extends FunctionNode {
    public MaxNode(ArrayList<Node> children) {
        super(children);
    }

    public double getValue(Spreadsheet spreadsheet) {
        return children.stream()
                .max(Comparator.comparing(node -> node.getValue(spreadsheet)))
                .orElseThrow(() ->
                        new NoSuchElementException("Max operation failed. List is empty")).getValue(spreadsheet);
    }
}
