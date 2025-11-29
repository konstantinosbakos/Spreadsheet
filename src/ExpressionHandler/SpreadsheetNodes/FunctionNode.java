package ExpressionHandler.SpreadsheetNodes;

import java.util.ArrayList;

public abstract class FunctionNode implements Node {
    protected final ArrayList<Node> children;

    public FunctionNode(ArrayList<Node> children) {
        this.children = children;
    }
}
