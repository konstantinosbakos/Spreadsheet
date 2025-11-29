package DataStructure;

import SpreadsheetCell.*;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class DoubleSkipListMap extends DataStructure {
    private final Map<String, ConcurrentSkipListMap<String, Cell>> colMap;
    private final Map<String, ConcurrentSkipListMap<String, Cell>> rowMap;

    public DoubleSkipListMap(){
        this.colMap = new HashMap<>();
        this.rowMap = new HashMap<>();
    }

    @Override
    public String[] getSortedRowKeys(){
        String[] rows = rowMap.keySet().toArray(new String[0]);

        Arrays.sort(rows);

        return rows;
    }

    @Override
    public Cell getCell(String coords) {
        String[] split_coords = splitCoords(coords);
        String col = split_coords[0];
        String row = split_coords[1];

        ConcurrentSkipListMap<String, Cell> cellCol = colMap.get(col);

        if(cellCol == null){
            return null;
        }
        else{
            return cellCol.get(row);
        }
    }

    @Override
    public void emptyCell(String coords) {
        String[] split_coords = splitCoords(coords);
        String col = split_coords[0];
        String row = split_coords[1];

        ConcurrentSkipListMap<String, Cell> rowList = colMap.get(col);
        ConcurrentSkipListMap<String, Cell> colList = rowMap.get(row);

        if(colList != null){
            colList.remove(row);

            if(colList.isEmpty()){
                colMap.remove(col);
            }
        }

        if(rowList != null){
            rowList.remove(col);

            if(rowList.isEmpty()){
                rowMap.remove(row);
            }
        }
    }

    @Override
    public Cell setCell(String coords, String content) {
        Cell newCell = createCell(coords, content);

        if(newCell == null){
            return null;
        }

        ConcurrentSkipListMap<String, Cell> colList = colMap.computeIfAbsent(
                newCell.getCol(), _ -> new ConcurrentSkipListMap<>());

        colList.computeIfAbsent(newCell.getRow(), _ -> newCell);

        ConcurrentSkipListMap<String, Cell> rowList = rowMap.computeIfAbsent(
                newCell.getRow(), _ -> new ConcurrentSkipListMap<>((a, b) -> {
                    if (a.length() != b.length()) {
                        return Integer.compare(a.length(), b.length());
                    }
                    return a.compareTo(b);
                }));

        rowList.computeIfAbsent(newCell.getCol(), _ -> newCell);

        return newCell;
    }

    private ArrayList<Cell> getColRange(String from, String to) {
        String[] splitFrom = splitCoords(from);
        String[] splitTo   = splitCoords(to);

        String fromRow = splitFrom[1];
        String fromCol = splitFrom[0];
        String toRow   = splitTo[1];
        String toCol   = splitTo[0];

        ArrayList<Cell> col = new ArrayList<>();

        if(fromCol.equals(toCol)){
            if(fromRow.isEmpty() && toRow.isEmpty()){
                col.addAll(colMap.get(fromCol).values());
            }
            else if(isRowSmaller(fromRow,toRow)){
                NavigableMap<String, Cell> subCol = colMap.get(fromCol).subMap(fromRow, true, toRow, true);

                col.addAll(subCol.values());
            }
        }

        return col;
    }

    private ArrayList<Cell> getRowRange(String from, String to) {
        ArrayList<Cell> row = new ArrayList<>();

        String[] splitFrom = splitCoords(from);
        String[] splitTo   = splitCoords(to);

        String fromCol = splitFrom[0];
        String fromRow = splitFrom[1];
        String toCol   = splitTo[0];
        String toRow   = splitTo[1];

        if(fromRow.equals(toRow)){
            if(fromCol.isEmpty() && toCol.isEmpty()){
                row.addAll(rowMap.get(fromRow).values());
            }
            else if(isColSmaller(fromCol,toCol)){
                NavigableMap<String, Cell> subRow = rowMap.get(fromRow).subMap(fromCol, true, toCol, true);

                row.addAll(subRow.values());
            }
        }

        return row;
    }

    @Override
    public ArrayList<Cell> getRange(String from, String to){
        String[] splitFrom = splitCoords(from);
        String[] splitTo   = splitCoords(to);

        String fromCol = splitFrom[0];
        String fromRow = splitFrom[1];
        String toCol   = splitTo[0];
        String toRow   = splitTo[1];

        System.out.println(fromCol);
        System.out.println(fromRow);
        System.out.println(toCol);
        System.out.println(toRow);

        if(fromCol.equals(toCol) && !fromCol.isEmpty()){
            return getColRange(from,to);
        }
        else if(fromRow.equals(toRow)){
            return getRowRange(from,to);
        }
        else{
            List<String> cols = getColumnsBetween(fromCol,toCol);
            ArrayList<Cell> cellsInRange = new ArrayList<>();

            for (String col : cols) {
                cellsInRange.addAll(getRange(col + fromRow,col + toRow));
            }

            return cellsInRange;
        }
    }

}
