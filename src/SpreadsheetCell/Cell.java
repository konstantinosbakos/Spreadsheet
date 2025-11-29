package SpreadsheetCell;

import java.util.ArrayList;
import java.util.List;

public abstract class Cell{
    protected String content;

    private final String col;
    private final String row;

    protected List<Cell> precedents;
    protected List<Cell> dependents;

    Cell(String col, String row){
        this.col = col;
        this.row = row;

        precedents = new ArrayList<>();
        dependents = new ArrayList<>();
    }

    public void updateDependents(List<Cell> dependents){
        this.dependents = dependents;
    }

    public Double getDoubleValue(){
        try{
            return Double.parseDouble(content);
        } catch(NumberFormatException e){
            System.out.println("Cell.Cell content is not a number: " + content);

            return null;
        }
    }

    public String getRow(){
        return row;
    }

    public String getCol(){
        return col;
    }

    public String getCoordinates(){
        return col + row;
    }

    public String getStringContent(){
        return content;
    }

    public Boolean addPrecedent(Cell precedent){
        return precedents.add(precedent);
    }

    public Boolean removePrecedent(Cell precedent){
        return precedents.remove(precedent);
    }

    public Boolean addDependent(Cell dependent){
        return dependents.add(dependent);
    }

    public Boolean removeDependent(Cell dependent){
        return dependents.remove(dependent);
    }

    public abstract String getCellContent();
    public abstract Boolean setCellContent(String content);
    public abstract double getCellValue();
}
