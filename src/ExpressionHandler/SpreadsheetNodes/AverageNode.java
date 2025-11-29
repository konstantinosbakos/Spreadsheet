package ExpressionHandler.SpreadsheetNodes;

import Spreadsheet.Spreadsheet;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class AverageNode extends FunctionNode {
    public AverageNode(ArrayList<Node> children) {
        super(children);
    }

    public double getValue(Spreadsheet spreadsheet) {
        return children.stream()
                .mapToDouble(node -> node.getValue(spreadsheet))
                .average()
                .orElseThrow(() -> new NoSuchElementException("Average operation failed. List is empty"));
    }
}
