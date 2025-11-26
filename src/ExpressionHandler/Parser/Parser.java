package ExpressionHandler.Parser;

import ExpressionHandler.SpreadsheetNodes.Node;
import ExpressionHandler.SpreadsheetNodes.OperatorNode;
import ExpressionHandler.AbstractFactory.AbstractFactory;
import ExpressionHandler.AbstractFactory.SpreadsheetFactory;
import ExpressionHandler.Tokenizer.Token;

import java.util.List;

import static ExpressionHandler.Tokenizer.TokenType.*;

public class Parser {
    int index;
    List<Token> tokens;
    AbstractFactory nodeFactory;

    public Parser() {
        this.nodeFactory = new SpreadsheetFactory();
    }

    public Parser(List<Token> tokens) {
        this.index  = 0;
        this.tokens = tokens;
    }

    public Node parseFactor(){
        Token token = advance();

        switch (token.getType()) {
            case NUMBER:
                return nodeFactory.makeConstant(token.getText());

            case CELL:
                return nodeFactory.makeCell(token.getText());

            case LPAREN:
                Node expr = parseExpression();     // parse inside parentheses
                consume(RPAREN);         // ensure closing parenthesis
                return expr;

            case FUNCTION:
                // parse function arguments here if needed
                // return nodeFactory.makeFunction(token.getText(), args);
                throw new UnsupportedOperationException("Function parsing not implemented yet");

            default:
                throw new RuntimeException("Unexpected token: " + token.getText());
        }
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

    private boolean check(Token expected) {
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

    private void consume(Token expected) {
        if (!check(expected)) {
            throw new RuntimeException("Expected '" + expected + "' but got '" + peek().getText() + "'");
        }

        advance();
    }
}
