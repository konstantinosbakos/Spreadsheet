package SpreadsheetCell;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public abstract class Cell{
    protected String            content;
    protected List<Cell>        downstream; //Cells used in the formula of this Cell.
    protected List<FormulaCell> upstream;   //Cells that use this Cell in their formula.

    private final String col;
    private final String row;

    Cell(String col, String row){
        this.col = col;
        this.row = row;

        upstream   = new ArrayList<>();
        downstream = new ArrayList<>();
    }

    public void setDownstream(List<Cell> downstream){
        this.downstream = new ArrayList<>(downstream);
    }

    public void setUpstream(List<FormulaCell> upstream){
        this.upstream = new ArrayList<>(upstream);
    }

    public String getCol(){
        return col;
    }

    public String getRow(){
        return row;
    }

    public String getCellCoordinates(){
        return col + row;
    }

    public String  getCellContent(){
        return content;
    }

    public boolean treeEquals(Cell other){
        /* Used to check if two Cells are the same
         * object or if they have the same coordinates
         * (used in cycle detection).
         */
        if (this == other){
            return true;
        }

        return (Objects.equals(col, other.getCol()) &&
                Objects.equals(row, other.getRow()));
    }

    public boolean treeEquals(String other_coords){
        /* Used to detect if candidate coordinates for
         * cell, equal to the coordinates of this
         * existing node.
         */
        String[] coords = splitCoords(other_coords);

        return (Objects.equals(col, coords[0]) &&
                Objects.equals(row, coords[1]));
    }

    public boolean addDownstream(Cell downstreamCell){
        return downstream.add(downstreamCell);
    }

    public boolean removeDownstream(Cell downstreamCell){
        return downstream.remove(downstreamCell);
    }

    public boolean addUpstream(FormulaCell upstreamCell){
        if(upstream.contains(upstreamCell)){
            return false;
        }
        else{
            return upstream.add(upstreamCell);
        }
    }

    public void deleteUpstream(){
        upstream.clear();
    }

    public void deleteDownstream(){
        downstream.clear();
    }

    public boolean removeUpstream(FormulaCell upstreamCell){
        return upstream.remove(upstreamCell);
    }

    public List<Cell> getDownstream(){
        return downstream;
    }

    public List<FormulaCell> getUpstream(){
        return upstream;
    }

    public abstract double  getCellValue();
    public abstract boolean setCellContent(String content);

    protected static String[] splitCoords(String coords){
        //Splits the coordinates to Letters-Numbers.
        int index = 0;

        while(index < coords.length() && Character.isLetter(coords.charAt(index))){
            index++;
        }

        String col = coords.substring(0, index);
        String row = coords.substring(index);

        return new String[]{col, row};
    }
}
