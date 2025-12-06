package SpreadsheetCell;

import Spreadsheet.Spreadsheet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Cell{
    protected String content;

    private final String col;
    private final String row;

    protected List<Cell> downstream;
    protected List<FormulaCell> upstream;

    Cell(String col, String row){
        this.col = col;
        this.row = row;

        upstream = new ArrayList<>();
        downstream = new ArrayList<>();
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

    public Boolean addUpstream(FormulaCell upstreamCell){
        if(upstream.contains(upstreamCell)){
            return false;
        }
        else{
            return upstream.add(upstreamCell);
        }
    }

    public void updateUpstream(Spreadsheet spreadsheet){
        for(FormulaCell upstreamCell : upstream){
            upstreamCell.calculateFormula(spreadsheet);
        }
    }

    public Boolean removeUpstream(FormulaCell upstreamCell){
        return upstream.remove(upstreamCell);
    }

    public Boolean addDownstream(Cell downstreamCell){
        return downstream.add(downstreamCell);
    }

    public Boolean removeDownstream(Cell downstreamCell){
        return downstream.remove(downstreamCell);
    }

    public List<FormulaCell> getUpstream(){
        return upstream;
    }

    public List<Cell> getDownstream(){
        return downstream;
    }

    public void setUpstream(List<FormulaCell> upstream){
        this.upstream = new ArrayList<>(upstream);
    }

    public void setDownstream(List<Cell> downstream){
        this.downstream = new ArrayList<>(downstream);
    }


    public abstract String getCellContent();
    public abstract Boolean setCellContent(String content);
    public abstract double getCellValue();

    @Override
    public boolean equals(Object object) {
        if (this == object){
            return true;
        }

        if (!(object instanceof Cell other)){
            return false;
        }

        return col.equals(other.getCol()) && row.equals(other.getRow());
    }

    @Override
    public int hashCode() {
        return Objects.hash(col, row);
    }
}
