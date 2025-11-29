package ExpressionHandler.SpreadsheetNodes;

import Spreadsheet.Spreadsheet;

public class CellNode implements Node {
    String coords;

    public CellNode(String coords) {
        this.coords = coords;
    }

    public double getValue(Spreadsheet spreadsheet){
        return spreadsheet.getCell(coords,"internal").getCellValue();
    }
}
