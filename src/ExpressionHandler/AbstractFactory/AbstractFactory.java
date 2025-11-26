package ExpressionHandler.AbstractFactory;


import ExpressionHandler.AbstractNodes.AbstractNode;

import java.util.ArrayList;

public interface AbstractFactory {
    AbstractNode makeConstant(String content);
    AbstractNode makeCell(String content);
    AbstractNode makeOperator(String operator, AbstractNode left, AbstractNode right);
    AbstractNode makeFunction(String name, ArrayList<AbstractNode> args);
}
