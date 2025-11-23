package Cell;

public class TextCell extends Cell{
    public TextCell(String col, String row) {
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
            this.content = content;

            return true;
        }
    }

    public double getCellValue(){
        if(content.isEmpty()){
            return 0;
        }
        else{
            return Double.NaN;
        }
    }
}
