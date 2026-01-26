package SpreadsheetCell;

public class GhostCell extends Cell{
    /* While GhostCell is not strictly necessary
     * I decided to keep it in the code because
     * it plays an important role of identifying
     * cells that the user has not filled, but
     * are part of formulas. This could be useful
     * for efficient cleanups after formulas are
     * removed from the spreadsheet.
     */
    public GhostCell(String col, String row){
        super(col, row);
    }

    @Override
    public double getCellValue(){
        return 0;
    }

    @Override
    public boolean setCellContent(String content){
        this.content = "==0";

        return true;
    }
}
