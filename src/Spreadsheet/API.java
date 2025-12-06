package Spreadsheet;

import SpreadsheetCell.Cell;

import java.io.IOException;

public class API {
    private final Spreadsheet spreadsheet;

    public API() {
        this.spreadsheet = new Spreadsheet();
    }

    public void import_S2V(String path) throws IOException {
        spreadsheet.import_S2V(path);
    }

    public void export_S2V(String path) throws IOException {
        spreadsheet.export_S2V(path);
    }

    public Cell getCell(String coords){
        return spreadsheet.getCell(coords);
    };

    public Cell setCell(String coords, String content){
        return spreadsheet.setCell(coords, content);
    }

    public void emptyCell(String coords){
        spreadsheet.emptyCell(coords);
    }

    public void printSpreadsheet(){
        spreadsheet.printSpreadsheet();
    }
}
