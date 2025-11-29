package SpreadsheetCell;

public class NumberCell extends Cell{
    double value;

    public NumberCell(String col, String row){
        super(col, row);
    }

    public String getCellContent(){
        return this.content;
    }

    public Boolean setCellContent(String content){
        if(content.isEmpty()){
            return false;
        }
        else{
            try{
                value = Double.parseDouble(content);
            } catch(NumberFormatException e){
                System.out.println("Please provide a number");

                return false;
            }

            this.content = content;

            return true;
        }
    }

    public double getCellValue(){
        return Double.parseDouble(this.content);
    }
}
