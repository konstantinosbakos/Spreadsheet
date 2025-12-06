package SpreadsheetCell;

import ExpressionHandler.AbstractFactory.AbstractFactory;
import ExpressionHandler.AbstractFactory.SpreadsheetFactory;
import ExpressionHandler.Parser.Parser;
import ExpressionHandler.SpreadsheetNodes.Node;
import Spreadsheet.Spreadsheet;

import java.util.*;

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
        boolean refIsSet = this.setReferences(spreadsheet);

        if(hasCycle() || !refIsSet){
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

        if(!upstream.isEmpty()){
            for(FormulaCell upstreamCell : upstream){
                upstreamCell.calculateFormula(spreadsheet);
            }
        }

        return true;
    }

    private boolean setReferences(Spreadsheet spreadsheet){
        List<String> references = parser.getCellReferences(content);

        this.downstream.clear();

        for(String coords : references){
            Cell cell =  spreadsheet.getCell(coords);

            if(cell != null && !cell.equals(this)){
                cell.addUpstream(this);
                this.addDownstream(cell);
            }
            else if(cell == null){
                if(Objects.equals(coords, this.getCol() + this.getRow())){
                    return false;
                }

                cell = spreadsheet.setCell(coords,"==0");

                cell.addUpstream(this);
                this.addDownstream(spreadsheet.getCell(coords));

            }
        }

        return true;
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
