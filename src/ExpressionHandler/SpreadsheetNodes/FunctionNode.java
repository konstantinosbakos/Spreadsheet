package ExpressionHandler.SpreadsheetNodes;

import java.util.ArrayList;

public abstract class FunctionNode extends Node {
    protected final ArrayList<Node> children;

    public FunctionNode(ArrayList<Node> children){
        super("Function");

        this.children = children;
    }
}
