package ExpressionHandler.Node;

import Cell.Cell;
import Spreadsheet.Spreadsheet;

public class CellNode implements Node {
    String coords;

    public CellNode(String coords) {
        this.coords = coords;
    }

    public double getValue(Spreadsheet spreadsheet){
        return spreadsheet.getCell(coords).getCellValue();
    }
}
