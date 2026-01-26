package DataStructure;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

import SpreadsheetCell.*;

public class DoubleSkipListMap extends DataStructure {
    private final Map<String, ConcurrentSkipListMap<Integer, Cell>> colMap;
    private final Map<Integer, ConcurrentSkipListMap<String, Cell>> rowMap;

    public DoubleSkipListMap(){
        this.colMap = new HashMap<>();
        this.rowMap = new HashMap<>();
    }

    @Override
    public Cell getCell(String coords){
        if(colMap.isEmpty() && rowMap.isEmpty()){
            return null;
        }

        String[] split_coords = splitCoords(coords);
        String   col          = split_coords[0];
        int      row          = Integer.parseInt(split_coords[1]);

        int colMapSize = -1;
        int rowMapSize = -1;

        if(!colMap.isEmpty()){
            if(colMap.containsKey(col)){
                colMapSize = colMap.get(col).size();
            }
        }

        if(!rowMap.isEmpty()){
            if(rowMap.containsKey(row)){
                rowMapSize = rowMap.get(row).size();
            }
        }

        if(colMapSize <= rowMapSize){ //Check which is smaller to query.
            ConcurrentSkipListMap<Integer, Cell> SkipList = colMap.get(col);

            if(SkipList == null){
                return null;
            }
            else{
                return SkipList.get(row);
            }

        }
        else{
            ConcurrentSkipListMap<String, Cell> SkipList = rowMap.get(row);

            if(SkipList == null){
                return null;
            }
            else{
                return SkipList.get(col);
            }
        }
    }

    @Override
    public Cell setCell(Cell newCell){
        /*
         * Adds a new Cell to both Maps-SkipLists.
         * Returns added cell.
         * If a SkipList does not exist,
         * it creates and populates it.
         */
        ConcurrentSkipListMap<Integer, Cell> colList = colMap.computeIfAbsent(
                newCell.getCol(), _ -> new ConcurrentSkipListMap<>());

        colList.putIfAbsent(Integer.parseInt(newCell.getRow()), newCell);

        ConcurrentSkipListMap<String, Cell> rowList = rowMap.computeIfAbsent(
                Integer.parseInt(newCell.getRow()), _ -> new ConcurrentSkipListMap<>((a, b) -> {
                    if (a.length() != b.length()){
                        return Integer.compare(a.length(), b.length());
                    }
                    return a.compareTo(b);
                }));

        rowList.putIfAbsent(newCell.getCol(), newCell);

        return newCell;
    }

    @Override
    public void deleteCell(String coords){
        /*
         * Removes a Cell from both Maps-SkipLists.
         * If a SkipList ends up empty after the
         * removal, it deletes it.
         */
        String[] split_coords = splitCoords(coords);
        String   col          = split_coords[0];
        int      row          = Integer.parseInt(split_coords[1]);

        Cell retrievedCell = getCell(coords);

        if(retrievedCell == null){
            return;
        }

        ConcurrentSkipListMap<String, Cell>  rowList = rowMap.get(row);
        ConcurrentSkipListMap<Integer, Cell> colList = colMap.get(col);

        if (rowList != null){
            rowList.remove(col);

            if (rowList.isEmpty()){
                rowMap.remove(row);
            }
        }

        if (colList != null){
            colList.remove(row);

            if (colList.isEmpty()){
                colMap.remove(col);
            }
        }
    }

    @Override
    public ArrayList<Integer> getSortedRowKeys(){
        /*
         * Rows are unordered in the map,
         * this function orders them and returns
         * them.
         */
        ArrayList<Integer> rows = new ArrayList<>(rowMap.keySet());
        Collections.sort(rows);

        return rows;
    }

    @Override
    public ArrayList<Cell> getRange(String range){
        /*
         * Main Range retrieval function.
         * It can return square ranges as well.
         */
        String[] split_range = splitRange(range);
        String from          = split_range[0];
        String to            = split_range[1];

        String[] splitFrom   = splitCoords(from);
        String[] splitTo     = splitCoords(to);

        String fromCol = splitFrom[0];
        String fromRow = splitFrom[1];
        String toCol   = splitTo[0];
        String toRow   = splitTo[1];

        if(fromCol.equals(toCol) && !fromCol.isEmpty()){
            return getColRange(from,to);
        }
        else if(fromRow.equals(toRow) && !fromRow.isEmpty()){
            return getRowRange(from,to);
        }
        else{
            List<String> cols = getColumnsBetween(fromCol,toCol);
            ArrayList<Cell> cellsInRange = new ArrayList<>();

            for (String col : cols){
                cellsInRange.addAll(getRange(col + fromRow + ":" + col + toRow));
            }

            return cellsInRange;
        }
    }

    private ArrayList<Cell> getColRange(String from, String to){
        String[] splitFrom = splitCoords(from);
        String[] splitTo   = splitCoords(to);

        int toRow   = -1;
        int fromRow = -1;

        if(!splitFrom[1].isEmpty()){
            fromRow = Integer.parseInt(splitFrom[1]);
        }
        String fromCol = splitFrom[0];

        if(!splitTo[1].isEmpty()){
            toRow = Integer.parseInt(splitTo[1]);
        }
        String toCol = splitTo[0];

        ArrayList<Cell> col = new ArrayList<>();

        if(fromCol.equals(toCol)){
            if(fromRow == -1 && toRow == -1){
                ConcurrentSkipListMap<Integer, Cell> fromColMap = colMap.get(fromCol);

                if(fromColMap != null){
                    col.addAll(fromColMap.values());
                }
            }
            else if(fromRow < toRow){
                NavigableMap<Integer, Cell> subCol = colMap.get(fromCol).subMap(fromRow, true, toRow, true);

                col.addAll(subCol.values());
            }
        }

        return col;
    }

    private ArrayList<Cell> getRowRange(String from, String to){
        ArrayList<Cell> row = new ArrayList<>();

        String[] splitFrom = splitCoords(from);
        String[] splitTo   = splitCoords(to);

        int toRow   = -1;
        int fromRow = -1;

        if(!splitFrom[1].isEmpty()){
            fromRow = Integer.parseInt(splitFrom[1]);
        }
        String fromCol = splitFrom[0];

        if(!splitTo[1].isEmpty()){
            toRow = Integer.parseInt(splitTo[1]);
        }
        String toCol = splitTo[0];

        if(fromRow == toRow){
            if(fromCol.isEmpty() && toCol.isEmpty()){
                ConcurrentSkipListMap<String, Cell> fromRowMap = rowMap.get(fromRow);

                if(fromRowMap != null){
                    row.addAll(fromRowMap.values());
                }
            }
            else if(isColSmaller(fromCol,toCol)){
                NavigableMap<String, Cell> subRow = rowMap.get(fromRow).subMap(fromCol, true, toCol, true);

                row.addAll(subRow.values());
            }
        }

        return row;
    }
}
