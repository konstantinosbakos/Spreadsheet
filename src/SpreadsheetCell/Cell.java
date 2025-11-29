package SpreadsheetCell;

import java.util.ArrayList;
import java.util.List;

public abstract class Cell{
    protected String content;

    private final String col;
    private final String row;

    protected List<Cell> upstream;
    protected List<Cell> downstream;

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

    public Boolean addUpstream(Cell upstreamCell){
        return upstream.add(upstreamCell);
    }

    public Boolean removeUpstream(Cell upstreamCell){
        return upstream.remove(upstreamCell);
    }

    public Boolean addDownstream(Cell downstreamCell){
        return downstream.add(downstreamCell);
    }

    public Boolean removeDownstream(Cell downstreamCell){
        return downstream.remove(downstreamCell);
    }

    public List<Cell> getUpstream(){
        return upstream;
    }

    public List<Cell> getDownstream(){
        return downstream;
    }

    public void setUpstream(List<Cell> upstream){
        this.upstream = upstream;
    }

    public void setDownstream(List<Cell> downstream){
        this.downstream = downstream;
    }


    public abstract String getCellContent();
    public abstract Boolean setCellContent(String content);
    public abstract double getCellValue();
}
