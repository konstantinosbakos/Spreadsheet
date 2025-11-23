package ExpressionHandler.NodeFactory;


import ExpressionHandler.Node.Node;

import java.util.ArrayList;

public interface NodeFactory {
    Node makeConstant(String content);
    Node makeCell(String content);
    Node makeOperator(String operator, Node left, Node right);
    Node makeFunction(String name, ArrayList<Node> args);
}
