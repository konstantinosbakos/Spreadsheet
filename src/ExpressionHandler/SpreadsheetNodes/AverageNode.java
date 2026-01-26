package ExpressionHandler.SpreadsheetNodes;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;

import Spreadsheet.Spreadsheet;

public class AverageNode extends FunctionNode{
    public AverageNode(ArrayList<Node> children){
        super(children);
    }

    public ArrayList<Double> getValue(Spreadsheet spreadsheet){
        //Checks the values of its children and returns a list
        //with one value, the average.
        ArrayList<Double> value = new ArrayList<>();

        DoubleSummaryStatistics stats = children.stream()
                .flatMap(node -> node.getValue(spreadsheet).stream())
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();

        if (stats.getCount() == 0){
            //If no children are found, return NaN.
            value.add(Double.NaN);
        }
        else{
            value.add(stats.getAverage());
        }

        return value;
    }
}