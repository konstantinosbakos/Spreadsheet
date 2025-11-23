package Spreadsheet;

import Cell.Cell;
import DataStructure.DataStructure;
import DataStructure.DoubleSkipListMap;

import java.io.*;
import java.util.*;

public class Spreadsheet{
    DoubleSkipListMap structure;

    Spreadsheet(){
        this.structure = new DoubleSkipListMap();
        structure.getRange("A5","C20");
    }

    public void import_S2V(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            String row = "1";
            String line;

            while((line = reader.readLine()) != null){
                String   col   = "A";
                String[] parts = line.split(";", -1);

                for(String part : parts){
                    if(!part.isEmpty()){
                        setCell(col+row,part);
                    }
                    col = structure.nextCol(col);
                }
                row = structure.nextRow(row);
            }
        }
    }

    public void export_S2V(String path) throws IOException {
        File     file = createAndOpen(path);
        String[] existingRows = structure.getSortedRowKeys();//sort not needed
        String   maxRow = existingRows[existingRows.length - 1];

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            for(int row = 1; row <= Integer.parseInt(maxRow); row++){
                String rowStr = Integer.toString(row);
                StringBuilder line = new StringBuilder();

                if(!Arrays.asList(existingRows).contains(rowStr)){
                    line.append(";");
                }
                else{
                    ArrayList<Cell> colList = structure.getRange(rowStr, rowStr);
                    System.out.println(colList);
                    int cellCount = 0;
                    int maxCellPosition = DataStructure.getMaxRowElements(colList.get(colList.size() - 1).getCol());

                    for (Cell currCell : colList) {
                        int cellRowPosition = DataStructure.colToNumber(currCell.getCol());

                        for (; cellCount < cellRowPosition; cellCount++) {
                            line.append(";");
                        }

                        line.append(currCell.getStringContent());
                        cellCount++;

                        if (cellCount != maxCellPosition) {
                            line.append(";");
                        }
                    }
                }

                writer.write(line.toString());
                writer.newLine();
            }
        }
    }

    public Cell getCell(String coords){
        return structure.getCell(coords);
    }

    public Cell setCell(String coords, String content){
        return structure.setCell(coords, content);
    }

    public void emptyCell(String coords){
        structure.emptyCell(coords);
    }

    public void printSpreadsheet(){
//        String[] rows = rowMap.keySet().toArray(new String[0]);
//
//        for(String row : rows){
//            ConcurrentSkipListMap<String, Cell> rowList = rowMap.get(row);
//
//            System.out.println("~~~");
//
//            for(Cell cell : rowList.values()){
//                System.out.print("[" + cell.getCoordinates() + "](" + cell.getStringContent() + ") ");
//            }
//            System.out.println();
//        }
    }

    private static File createAndOpen(String path) throws IOException {
        File file = new File(path);

        if(file.exists()) {
            if(!file.delete()){
                throw new IOException("Could not delete file: " + path);
            }
        }

        if(!file.createNewFile()){
            throw new IOException("Could not create file: " + path);
        }

        return file;
    }
}
