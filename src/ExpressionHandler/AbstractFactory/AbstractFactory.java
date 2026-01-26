package ExpressionHandler.AbstractFactory;

import java.util.ArrayList;

import ExpressionHandler.AbstractNodes.AbstractNode;

public interface AbstractFactory {
    AbstractNode makeCell(String content);
    AbstractNode makeConstant(String content);
    AbstractNode makeFunction(String name, ArrayList<AbstractNode> args);
    AbstractNode makeOperator(String operator, AbstractNode left, AbstractNode right);
}
