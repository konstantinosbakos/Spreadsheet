package SpreadsheetCell;

import java.util.*;

import Spreadsheet.Spreadsheet;
import ExpressionHandler.Parser.Parser;
import ExpressionHandler.SpreadsheetNodes.Node;
import ExpressionHandler.AbstractFactory.SpreadsheetFactory;

public class FormulaCell extends Cell{
    private double value;
    private final Parser parser;

    public FormulaCell(String col, String row){
        super(col, row);

        this.value  = 0;
        this.parser = new Parser(SpreadsheetFactory.getInstance());
    }

    public double getCellValue(){
        return this.value;
    }

    public boolean setCellContent(String content){
        if(content.startsWith("=")){
            //Check if it is a formula (has to start with '=').
            this.content = noWhiteSpace(content);

            return true;
        }
        else{
            return false;
        }
    }

    public boolean calculateFormula(Spreadsheet spreadsheet){
        try {
            this.internalCalculateFormula(spreadsheet);
        } catch (IllegalStateException e){
            System.err.println("Error: " + e.getMessage());

            return false;
        }

        if(!upstream.isEmpty()){
            //If the cell is part of other formulas, recalculate them.
            for(FormulaCell upstreamCell : upstream){
                if (!upstreamCell.calculateFormula(spreadsheet)){
                    return false;
                }
            }
        }

        return true;
    }

    public boolean hasCycle(){
        //Checks if this cell exists in the dependency tree created by its downstream cells.
        return dfs(this, new HashSet<>(), new HashSet<>());
    }

    private void internalCalculateFormula(Spreadsheet spreadsheet){
        boolean refIsSet = this.setReferences(spreadsheet);

        if(hasCycle() || !refIsSet){
            throw new IllegalStateException(
                    "Circular reference detected in cell " + this.getCol() + this.getRow() + "."
            );
        }

        ArrayList<Double> expValue = new ArrayList<>();

        try {
            //Try to calculate the formula and
            expValue = ((Node) parser.getExpression(content)).getValue(spreadsheet);
        } catch (Exception e){
            //print the error message if an error occurs during the computation.
            System.err.println(e.getMessage());
        }

        if(!expValue.isEmpty()){
            //If the list of values returned is not empty, get the
            //first one and set it (the list always contains one value).
            this.value = expValue.getFirst();
        }
        else{
            //If the list is empty, set the value to NaN in order to
            //indicate an error.
            this.value = Double.NaN;
        }
    }

    private boolean setReferences(Spreadsheet spreadsheet){
        //Get references (Cells and Ranges) from the parser.
        List<String> references = parser.getCellReferences(content);

        //Clear downstream (if it has Cells from previous formulas).
        this.downstream.clear();

        for(String coords : references){
            if(coords.contains(":")){
                //If there is a range in the references, get the Cells.
                List<String> rangeCellCoords = Spreadsheet.getRangeCellCoords(coords);

                for(String rangeCellCoord : rangeCellCoords){
                    if(!setCellReference(rangeCellCoord, spreadsheet)){
                        return false;
                    };
                }
            }
            else{
                if(!setCellReference(coords, spreadsheet)){
                    return false;
                }
            }
        }

        return true;
    }

    private boolean dfs(Cell current, Set<Cell> path, Set<Cell> visited){
        /* A Depth-First-Search algorithm which returns
         * true if the current Cell (this) is found again
         * in the reference tree.
         */
        for(Cell item : path){
            if(item.treeEquals(current)){
                return true;
            }
        }

        for(Cell item : visited){
            if(item.treeEquals(current)){
                return false;
            }
        }

        path.add(current);

        for(Cell cell : current.getDownstream()){
            if(dfs(cell, path, visited)){
                return true;
            }
        }

        visited.add(current);
        path.remove(current);

        return false;
    }

    private boolean setCellReference(String coords, Spreadsheet spreadsheet){
        /*
         * This function sets the references for all
         * Cells in the formula and creates any
         * downstream Cells that do not exist as
         * GhostCells.
         */
        Cell cell =  spreadsheet.getCell(coords,true);

        if(cell != null){
            cell.addUpstream(this);
            this.addDownstream(cell);
        }
        else {
            if(this.treeEquals(coords)){
                //if the cell to be created is the existing one
                //(so there is a loop), return error.
                return false;
            }

            cell = spreadsheet.setCell(coords,"==0");

            //Add the current cell to the upstream of the new
            //ghost cell.
            cell.addUpstream(this);
            this.addDownstream(spreadsheet.getCell(coords,true));
        }

        return true;
    }

    private String noWhiteSpace(String content){
        //Remove all white space from the formula.
        StringBuilder newContent = new StringBuilder();

        for(int i=0; i<content.length(); i++){
            if(Character.isWhitespace(content.charAt(i))){
                continue;
            }
            else{
                newContent.append(content.charAt(i));
            }
        }

        return newContent.toString();
    }
}
