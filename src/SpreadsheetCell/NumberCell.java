package SpreadsheetCell;

public class NumberCell extends Cell{
    protected double value;

    public NumberCell(String col, String row){
        super(col, row);
    }

    public double getCellValue(){
        return this.value;
    }

    public boolean setCellContent(String content){
        if(content.isEmpty()){
            return false;
        }
        else{
            try{ //Check if the content is a number.
                value = Double.parseDouble(content);
            } catch(NumberFormatException e){
                //If it is not, return error and do not initialize the cell.
                System.err.println("Error: " + e.getMessage());

                return false;
            }

            this.content = content;

            return true;
        }
    }
}
