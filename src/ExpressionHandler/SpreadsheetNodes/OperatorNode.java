package ExpressionHandler.SpreadsheetNodes;

import java.util.Objects;
import java.util.ArrayList;

import Spreadsheet.Spreadsheet;

public class OperatorNode extends Node{
    private final Node   left;
    private final Node   right;
    private final String operator;

    public OperatorNode(String operator, Node left, Node right){
        super("Operator");

        this.left  = left;
        this.right = right;

        if (!"+-*/".contains(operator)){
            throw new IllegalArgumentException("Not a valid operator: " + operator);
        }
        else {
            this.operator = operator;
        }
    }

    public ArrayList<Double> getValue(Spreadsheet spreadsheet){
        ArrayList<Double> value = new ArrayList<>();

        if(Objects.equals(left.getType(), "Range") ||
           Objects.equals(right.getType(), "Range")){
            /*
             * This if-clause exists because cells and ranges are both
             * "cellNode". The only difference in handling between ranges
             * and cells is here, where ranges cannot/should not be able
             * to have operations performed on them; they should only
             * be contained in formulas.
             */
            throw new IllegalStateException("Ranges must be placed only in Functions.");
        }

        switch (operator){
            case "+" -> value.add(left.getValue(spreadsheet).getFirst() + right.getValue(spreadsheet).getFirst());
            case "-" -> value.add(left.getValue(spreadsheet).getFirst() - right.getValue(spreadsheet).getFirst());
            case "*" -> value.add(left.getValue(spreadsheet).getFirst() * right.getValue(spreadsheet).getFirst());
            case "/" -> value.add(left.getValue(spreadsheet).getFirst() / right.getValue(spreadsheet).getFirst());

            default -> throw new IllegalArgumentException();
        };

        return value;
    }
}
