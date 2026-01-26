package ExpressionHandler.AbstractFactory;

import java.util.ArrayList;

import ExpressionHandler.SpreadsheetNodes.*;
import ExpressionHandler.AbstractNodes.AbstractNode;

public final class SpreadsheetFactory implements AbstractFactory {
    private static SpreadsheetFactory instance;

    private SpreadsheetFactory(){}

    public static SpreadsheetFactory getInstance(){
        if (instance == null){
            instance = new SpreadsheetFactory();
        }

        return instance;
    }

    @Override
    public AbstractNode makeCell(String content){
        return new CellNode(content);
    }

    @Override
    public AbstractNode makeConstant(String content){
        try {
            double value = Double.parseDouble(content);
            return new ConstantNode(value);
        } catch (NumberFormatException e){
            throw new RuntimeException("Invalid constant number: " + content, e);
        }
    }


    @Override
    public AbstractNode makeFunction(String name, ArrayList<AbstractNode> args){
        ArrayList<Node> spreadsheetArgs = new ArrayList<>();

        for (AbstractNode abstractNode : args){
            spreadsheetArgs.add((Node) abstractNode);
        }

        return switch (name){
            case "SUM" -> new SumNode(spreadsheetArgs);
            case "MIN" -> new MinNode(spreadsheetArgs);
            case "MAX" -> new MaxNode(spreadsheetArgs);
            case "AVG" -> new AverageNode(spreadsheetArgs);
            default    -> throw new RuntimeException("Invalid function name: " + name);
        };
    }

    @Override
    public AbstractNode makeOperator(String operator, AbstractNode left, AbstractNode right){
        return new OperatorNode(operator, (Node) left, (Node) right);
    }
}
