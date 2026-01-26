package Spreadsheet;

import java.io.*;
import java.util.*;

import SpreadsheetCell.Cell;
import SpreadsheetCell.FormulaCell;

import DataStructure.DataStructure;
import DataStructure.DoubleSkipListMap;

public class Spreadsheet{
    DataStructure structure;

    Spreadsheet(){
        this.structure = new DoubleSkipListMap();
    }

    /* ~~~ Basic Functions ~~~ Start ~~~*/

    public void import_S2V(String path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))){
            int    row = 1;
            String line;

            while((line = reader.readLine()) != null){
                String   col   = "A";
                String[] parts = line.split(";", -1);

                StringBuilder formulaInput = new StringBuilder();

                for(String part : parts){
                    if(!part.isEmpty()){
                        if((part.contains("(")|| !formulaInput.isEmpty()) && !part.contains(")")){
                            formulaInput.append(part).append(";");

                            continue;
                        }
                        else if(part.contains(")")){
                            formulaInput.append(part);

                            setCell(col+row,formulaInput.toString());

                            formulaInput = new StringBuilder();
                        }
                        else{
                            setCell(col+row,part);
                        }
                    }
                    col = DataStructure.nextCol(col);
                }
                row++;
            }
        }
    }

    public void export_S2V(String path) throws IOException {
        File               file         = createAndOpen(path);
        ArrayList<Integer> existingRows = structure.getSortedRowKeys();
        int                maxRow       = existingRows.getLast();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            for(int row = 1; row <= maxRow; row++){
                StringBuilder line   = new StringBuilder();

                if(!existingRows.contains(row)){
                    line.append(";");
                }
                else{
                    ArrayList<Cell> colList = structure.getRange(row + ":" + row);

                    int    cellCount       = 0;
                    int    maxCellPosition = DataStructure.getMaxRowElements(colList.getLast().getCol());
                    boolean printed        = false; //Check if it is only ghost cells.

                    for (Cell currCell : colList){
                        int cellRowPosition = DataStructure.colToNumber(currCell.getCol());

                        for (; cellCount < cellRowPosition; cellCount++){
                            line.append(";");
                        }

                        if(!Objects.equals(currCell.getCellContent(), "==0")){
                            line.append(currCell.getCellContent());
                            printed = true;
                        }
                        cellCount++;

                        if (cellCount != maxCellPosition){
                            line.append(";");
                        }
                    }

                    if(!printed){
                        line.append(";");
                    }
                    else{
                        printed = false;
                    }
                }

                writer.write(line.toString());
                writer.newLine();
            }
        }
    }

    public Cell getCell(String coords, boolean allowGhosts){
        if(!isValidCell(coords)){
            return null;
        }
        else{
            Cell retrievedCell = structure.getCell(coords);

            if(retrievedCell != null){
                if(Objects.equals(retrievedCell.getCellContent(), "==0") && !allowGhosts){
                    return null;
                }
            }

            return retrievedCell;
        }
    }

    public Cell setCell(String coords, String content){
        if(!isValidCell(coords)){
            return null;
        }

        //Create a new cell with the requested content.
        Cell newCell      = structure.createCell(coords, content);
        //Find the existing cell, if it exists.
        Cell existingCell = this.getCell(coords,true);

        if(newCell != null){
            if(existingCell != null){
                newCell.setUpstream(existingCell.getUpstream());
            }

            //Check if the new cell has a formula.
            int isUpdated = calculateNewCellFormula(newCell);

            if(isUpdated == 0 || isUpdated == 1){
                //If it has and it is calculated correctly or if it has not,
                if(existingCell != null){
                    //remove the existing cell from dependency lists,
                    for(Cell downstreamCell : existingCell.getDownstream()){
                        downstreamCell.removeUpstream((FormulaCell)existingCell);
                    }

                    for(Cell upstreamCell : existingCell.getUpstream()){
                        upstreamCell.removeDownstream(existingCell);
                    }

                    existingCell.deleteUpstream();
                    existingCell.deleteDownstream();

                    //and delete it.
                    structure.deleteCell(existingCell.getCellCoordinates());
                }

            }
            else if(isUpdated == -1){
                //if there is a formula but it is not correct,
                //remove the new cell from any dependency lists,
                for(Cell downstreamCell : newCell.getDownstream()){
                    downstreamCell.removeUpstream((FormulaCell)newCell);
                }

                for(Cell upstreamCell : newCell.getUpstream()){
                    upstreamCell.removeDownstream(newCell);
                }

                //delete it,
                newCell.deleteUpstream();
                newCell.deleteDownstream();

                //and return null.
                return null;
            }

            structure.setCell(newCell);

            if(newCell.getUpstream() != null){
                //Update the formulas (if any).
                for(FormulaCell upstreamCell : newCell.getUpstream()){
                    upstreamCell.calculateFormula(this);
                }
            }

            return newCell;
        }
        else{
            //If the new cell did not get initialized for some
            //reason, return null.
            return null;
        }
    }

    public void emptyCell(String coords){
        if(!isValidCell(coords)){
            return;
        }

        Cell ghostCell    = null;
        Cell existingCell = this.getCell(coords,true);

        if(existingCell != null){
            //If there is an existing cell,
            if(existingCell.getDownstream() != null){
                //remove it from the dependency lists of the downstream cells,
                for(Cell downstreamCell : existingCell.getDownstream()){
                    downstreamCell.removeUpstream((FormulaCell)existingCell);
                }

                existingCell.deleteDownstream();
            }

            if(existingCell.getUpstream() != null){
                //create a ghost cell to put in its place
                //if there are cells that use this cell upstream,
                ghostCell = structure.createCell(existingCell.getCellCoordinates(), "==0");

                ghostCell.setUpstream(existingCell.getUpstream());

                //update the upstream cells and remove the old cell from
                //their dependency list,
                for(FormulaCell upstreamCell : existingCell.getUpstream()){
                    upstreamCell.removeDownstream(existingCell);
                }
                existingCell.deleteUpstream();

                //and delete the old cell and add the new.
                structure.deleteCell(coords);
                structure.setCell(ghostCell);

                if(ghostCell.getUpstream() != null){
                    for(FormulaCell upstreamCell : ghostCell.getUpstream()){
                        upstreamCell.calculateFormula(this);
                    }
                }
            }
        }
    }

    public ArrayList<ArrayList<Cell>> getCellRows(){
        //Return sorted (excel-alphabetically) rows of cells for printing.
        ArrayList<Integer> rows = structure.getSortedRowKeys();
        
        ArrayList<ArrayList<Cell>> cellRows = new ArrayList<>();

        for (int row : rows){
            cellRows.add(structure.getRange(row + ":" + row));
        }
        
        return cellRows;
    }

    /* ~~~ Basic Functions ~~~  End  ~~~*/

    public ArrayList<Double> getCellValues(String coords){
        if(coords.contains(":")){
            ArrayList<Cell>   cells  = structure.getRange(coords);
            ArrayList<Double> values = new ArrayList<>();

            for(Cell cell : cells){
                values.add(cell.getCellValue());
            }

            return values;
        }
        else{
            ArrayList<Double> oneCell = new ArrayList<>();

            Cell cell = structure.getCell(coords);

            if(cell != null){
                oneCell.add(cell.getCellValue());
            }

            return oneCell;
        }
    }

    public static List<String> getRangeCellCoords(String range){
        return DataStructure.getRangeCellCoords(range);
    }

    private int calculateNewCellFormula(Cell newCell){
        if(newCell.getCellContent().length() <= 1){
            return 0;
        }

        if(newCell.getCellContent().charAt(0) == '=' &&
           newCell.getCellContent().charAt(1) != '='){
            if(((FormulaCell)newCell).calculateFormula(this)){
                return 1; //Formula calculated correctly.
            }
            else{
                return -1; //Formula had an error.
            }
        }

        return 0; //No formula found.
    }

    private void isValidCellInternal(String coords){
        if(!(coords.charAt(0) >= 'A'  && coords.charAt(0) <= 'Z')){
            //If the first element is not a letter, throw error.
            throw new RuntimeException("Invalid cell coordinates: " + coords);
        }

        boolean isLetter = true;
        boolean isNumber = false;

        for(int i=0; i<coords.length(); i++){
            if((coords.charAt(i) >= '0' && coords.charAt(i) <= '9')){
                //If it is number, make isNumber true (it marks the beginning of
                //the numbers.
                isNumber = true;
                isLetter = false;
            }

            if(!(coords.charAt(i) >= 'A'  && coords.charAt(i) <= 'Z')
                && !isNumber && isLetter){
                //If it should be a letter (and it is not), throw error.
                throw new RuntimeException("Invalid cell coordinates: " + coords);
            }

            if(!(coords.charAt(i) >= '0' && coords.charAt(i) <= '9')
                && isNumber && !isLetter){
                //If it is not a number (and it should), throw error.
                throw new RuntimeException("Invalid cell coordinates: " + coords);
            }
        }
    }

    private boolean isValidCell(String coords){
        try {
            isValidCellInternal(coords);

            return true;
        } catch (RuntimeException e){
            System.err.println(e.getMessage());

            return false;
        }
    }

    private static File createAndOpen(String path) throws IOException {
        File file = new File(path);

        if(file.exists()){
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
