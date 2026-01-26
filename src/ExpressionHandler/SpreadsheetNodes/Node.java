package ExpressionHandler.SpreadsheetNodes;

import java.util.ArrayList;

import Spreadsheet.Spreadsheet;
import ExpressionHandler.AbstractNodes.AbstractNode;

public abstract class Node implements AbstractNode {
    private final String type;

    protected Node(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public abstract ArrayList<Double> getValue(Spreadsheet spreadsheet);
}
