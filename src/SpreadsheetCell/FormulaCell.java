package SpreadsheetCell;

import ExpressionHandler.AbstractFactory.AbstractFactory;
import ExpressionHandler.AbstractFactory.SpreadsheetFactory;
import ExpressionHandler.Parser.Parser;
import ExpressionHandler.SpreadsheetNodes.Node;
import Spreadsheet.Spreadsheet;

public class FormulaCell extends Cell{
    private double value;

    final Parser parser;
    final AbstractFactory factory;

    public FormulaCell(String col, String row){
        super(col, row);

        this.value   = 0;
        this.factory = new SpreadsheetFactory();
        this.parser  = new Parser(this.factory);
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

            return true;
        }
        else{
            return false;
        }
    }

    public void calculateFormula(Spreadsheet spreadsheet){
        this.value = ((Node) parser.getExpression(content)).getValue(spreadsheet);
    }
}
