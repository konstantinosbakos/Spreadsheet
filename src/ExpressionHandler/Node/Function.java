package ExpressionHandler.Node;

import java.util.ArrayList;

public abstract class Function implements Node {
    protected final ArrayList<Node> children;

    public Function(ArrayList<Node> children) {
        this.children = children;
    }
}
