package ExpressionHandler.NodeFactory;

import ExpressionHandler.Node.*;

import java.util.ArrayList;

public class SpreadsheetFactory implements NodeFactory {

    @Override
    public Node makeConstant(String content) {
        try {
            double value = Double.parseDouble(content);
            return new ConstantNode(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid constant number: " + content, e);
        }    }

    @Override
    public Node makeCell(String content) {
        return new CellNode(content);
    }

    @Override
    public Node makeOperator(String operator, Node left, Node right) {
        return new OperatorNode(operator, left, right);
    }

    @Override
    public Node makeFunction(String name, ArrayList<Node> args) {
        return switch (name) {
            case "SUM" -> new SumNode(args);
            case "MIN" -> new MinNode(args);
            case "MAX" -> new MaxNode(args);
            case "AVG" -> new AverageNode(args);
            default -> throw new RuntimeException("Invalid function name: " + name);
        };
    }
}
