package ExpressionHandler.SpreadsheetNodes;
import ExpressionHandler.AbstractNodes.AbstractNode;
import Spreadsheet.Spreadsheet;

public interface Node extends AbstractNode {
    double getValue(Spreadsheet spreadsheet);
}
