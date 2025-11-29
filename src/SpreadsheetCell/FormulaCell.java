package SpreadsheetCell;

import ExpressionHandler.AbstractFactory.AbstractFactory;
import ExpressionHandler.AbstractFactory.SpreadsheetFactory;
import ExpressionHandler.Parser.Parser;
import ExpressionHandler.SpreadsheetNodes.Node;
import Spreadsheet.Spreadsheet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FormulaCell extends Cell{
    private double value;

    final Parser parser;
    final AbstractFactory factory;

    public FormulaCell(String col, String row){
        super(col, row);

        this.value   = 0;
        this.factory = new SpreadsheetFactory();
        this.parser  = new Parser(this.factory);
    }

    public double getCellValue(){
        return this.value;
    }

    public String getCellContent(){
        return this.content;
    }

    public Boolean setCellContent(String content){
        if(content.startsWith("=")){
            this.content = content;

            return true;
        }
        else{
            return false;
        }
    }

    private void internalCalculateFormula(Spreadsheet spreadsheet){
        this.setReferences(spreadsheet);

        if(hasCycle()){
            throw new IllegalStateException(
                    "Circular reference detected in cell " + this.getCol() + this.getRow() + "."
            );
        }

        this.value = ((Node) parser.getExpression(content)).getValue(spreadsheet);
    }

    public Boolean calculateFormula(Spreadsheet spreadsheet){
        try {
            this.internalCalculateFormula(spreadsheet);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());

            return false;
        }

        return true;
    }

    private void setReferences(Spreadsheet spreadsheet){
        List<String> references = parser.getCellReferences(content);

        this.downstream.clear();

        for(String coords : references){
            Cell cell =  spreadsheet.getCell(coords,"internal");

            if(cell != null){
                cell.addUpstream(this);
                this.addDownstream(cell);
            }
            else{
                cell = spreadsheet.setCell(coords,"==0");

                cell.addUpstream(this);
                this.addDownstream(spreadsheet.getCell(coords,"internal"));
            }
        }
    }

    public boolean hasCycle() {
        return dfs(this, new HashSet<>(), new HashSet<>());
    }

    private boolean dfs(Cell current, Set<Cell> path, Set<Cell> visited) {
        if(path.contains(current)) {
            return true;
        }

        if(visited.contains(current)) {
            return false;
        }

        path.add(current);

        for(Cell cell : current.getDownstream()) {
            if(dfs(cell, path, visited)) {
                return true;
            }
        }

        visited.add(current);
        path.remove(current);

        return false;
    }
}
