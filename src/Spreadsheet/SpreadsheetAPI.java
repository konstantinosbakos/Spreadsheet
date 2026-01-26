package Spreadsheet;

import java.io.IOException;
import java.util.ArrayList;

import SpreadsheetCell.Cell;

public class SpreadsheetAPI {
    private final Spreadsheet spreadsheet;

    public SpreadsheetAPI(){
        this.spreadsheet = new Spreadsheet();
    }

    public void import_S2V(String path) throws IOException {
        spreadsheet.import_S2V(path);
    }

    public void export_S2V(String path) throws IOException {
        spreadsheet.export_S2V(path);
    }

    public Cell getCell(String coords){
        return spreadsheet.getCell(coords,false);
    };

    public Cell setCell(String coords, String content){
        return spreadsheet.setCell(coords, content);
    }

    public void emptyCell(String coords){
        spreadsheet.emptyCell(coords);
    }

    public ArrayList<ArrayList<Cell>> getCellRows(){
        return spreadsheet.getCellRows();
    }
}
