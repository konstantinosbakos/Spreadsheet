package Cell;

public class FormulaCell extends Cell{
    private double value;

    public FormulaCell(String col, String row){
        super(col, row);
    }

    public double getCellValue(){
        return this.value;
    }

    public String getCellContent(){
        return this.content;
    }

    public Boolean setCellContent(String content){
        if(content.startsWith("=")){
            this.content = content;

            return calculateFormula();
        }
        else{
            return false;
        }

    }

    private Boolean calculateFormula(){
        double result = 0;

        this.value = result;

        return true;
    }
}
