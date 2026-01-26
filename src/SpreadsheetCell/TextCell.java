package SpreadsheetCell;

public class TextCell extends Cell{
    public TextCell(String col, String row){
        super(col, row);
    }

    public double getCellValue(){
        if(!content.isEmpty()){
            //Check if the content is a number.
            double value = 0;

            try{
                value = Double.parseDouble(content);
            } catch(NumberFormatException _){
                return Double.NaN;
            }

            return value;
        }
        else if(content.isEmpty()){
            //Check if it is empty.
            return 0;
        }
        else{
            //If neither of the previous cases is true,
            //return NaN.
            return Double.NaN;
        }
    }

    public boolean setCellContent(String content){
        if(content.isEmpty()){
            return false;
        }
        else{
            this.content = content;

            return true;
        }
    }
}
