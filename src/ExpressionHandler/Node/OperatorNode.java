package ExpressionHandler.Node;

import java.util.ArrayList;

public class OperatorNode implements Node {
    private final String operator;
    private final Node left;
    private final Node right;

    public OperatorNode(String operator, Node left, Node right){
        this.left = left;
        this.right = right;

        if (!"+-*/".contains(operator)) {
            throw new IllegalArgumentException("Not a valid operator: " + operator);
        }
        else {
            this.operator = operator;
        }
    }

    public double getValue(){
        return switch (operator) {
            case "+" -> left.getValue() + right.getValue();
            case "-" -> left.getValue() - right.getValue();
            case "*" -> left.getValue() * right.getValue();
            case "/" -> left.getValue() / right.getValue();
            default -> throw new IllegalArgumentException();
        };
    }
}
