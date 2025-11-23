package ExpressionHandler.Parser;

import ExpressionHandler.Node.Node;
import ExpressionHandler.Node.OperatorNode;
import ExpressionHandler.Tokenizer.Token;

import java.util.List;

public class Parser {
    int index;
    List<Token> tokens;

    public Parser(List<Token> tokens) {
        this.index  = 0;
        this.tokens = tokens;
    }

    public Node parseFactor(){
        Token token = advance();

        if (t instanceof ConstantToken)
            return new Constant(Double.parseDouble(t.text));
        if (t instanceof CellToken)
            return new Cell(t.text);
        if (t.text.equals("(")) {
            Node subExpr = parseExpression(); // parse inside parentheses
            consume(")");                     // make sure closing parenthesis exists
            return subExpr;
        }

        throw new RuntimeException("Unexpected token: " + t.text);

    }

    public Node parseTerm(){
        
    }

    public Node parseExpression() {
        Node left = parseTerm();

        while (match("+", "-")) {
            String op = previous().getText();
            Node right = parseTerm();
            left = new OperatorNode(op, left, right);
        }
        return left;
    }

    private boolean isAtEnd() {
        return index >= tokens.size();
    }

    private Token peek() {
        return tokens.get(index);
    }

    private Token previous() {
        return tokens.get(index - 1);
    }

    private Token advance() {
        if (!isAtEnd()) {
            index++;
        }

        return previous();
    }

    private boolean check(String expected) {
        return !isAtEnd() && peek().getText().equals(expected);
    }

    private boolean match(String... expected) {
        for (String e : expected) {
            if (check(e)) {
                advance();

                return true;
            }
        }

        return false;
    }

    private void consume(String expected) {
        if (!check(expected)) {
            throw new RuntimeException("Expected '" + expected + "' but got '" + peek().getText() + "'");
        }

        advance();
    }
}
