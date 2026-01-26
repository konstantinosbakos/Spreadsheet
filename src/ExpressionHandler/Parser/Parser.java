package ExpressionHandler.Parser;

import java.util.List;
import java.util.Deque;
import java.util.ArrayList;
import java.util.ArrayDeque;

import ExpressionHandler.Tokenizer.Token;
import ExpressionHandler.Tokenizer.Tokenizer;
import ExpressionHandler.Tokenizer.TokenType;

import ExpressionHandler.AbstractNodes.AbstractNode;
import ExpressionHandler.SpreadsheetNodes.ErrorNode;
import ExpressionHandler.AbstractFactory.AbstractFactory;

public record Parser(AbstractFactory abstractFactory){
    private static int precedence(String op){
        return switch (op){
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 0;
        };
    }

    private static List<Token> shuntingYard(List<Token> infixTokens){
        List<Token>  postfixTokens = new ArrayList<>();
        Deque<Token> operatorStack = new ArrayDeque<>();

        if (infixTokens.size() == 1 && infixTokens.getFirst().type() == TokenType.RANGE){
            throw new IllegalStateException("Ranges must be placed only in Functions.");
        }

        for (Token token : infixTokens){
            switch (token.type()){
                case TokenType.CELL, TokenType.NUMBER, TokenType.RANGE:
                    postfixTokens.add(token);

                    break;
                case TokenType.FUNCTION, TokenType.LPAREN:
                    operatorStack.push(token);

                    break;
                case TokenType.OPERATOR:
                    while (true){
                        Token top = operatorStack.peek();

                        if (top == null || top.type() != TokenType.OPERATOR){
                            break;
                        }

                        String a = top.text();
                        String b = token.text();

                        if (precedence(a) < precedence(b)){
                            break;
                        }

                        postfixTokens.add(operatorStack.pop());
                    }

                    operatorStack.push(token);

                    break;

                case TokenType.RPAREN:
                    // Pop until '(' is found.
                    while (true){
                        if (operatorStack.isEmpty()){
                            throw new IllegalArgumentException("Mismatched parentheses [more ')'].");
                        }

                        Token top = operatorStack.pop();

                        if (top.type() == TokenType.LPAREN){
                            break; // Found.
                        }

                        postfixTokens.add(top);
                    }

                    if (!operatorStack.isEmpty()
                            && operatorStack.peek().type() == TokenType.FUNCTION){
                        postfixTokens.add(operatorStack.pop());
                    }

                    break;
                case ARG_SEPARATOR:
                    while (!operatorStack.isEmpty()
                            && operatorStack.peek().type() != TokenType.LPAREN){

                        postfixTokens.add(operatorStack.pop());
                    }

                    break;
                default:
                    throw new IllegalArgumentException("Unexpected token: " + token);
            }
        }

        while (!operatorStack.isEmpty()){
            Token token = operatorStack.pop();
            if (token.type() == TokenType.LPAREN){
                // More '(' than ')'.
                throw new IllegalArgumentException("Mismatched parentheses [more '(']");
            }

            postfixTokens.add(token);
        }

        return postfixTokens;
    }

    public AbstractNode getExpression(String expression){
        try {
            List<Token> infixTokens;

            try{
                infixTokens = Tokenizer.tokenize(expression);
            } catch (Exception e){
                System.err.println(e.getMessage());

                return null;
            }

            List<Token> postfixTokens = shuntingYard(infixTokens);

            Deque<AbstractNode> stack = new ArrayDeque<>();

            for (Token token : postfixTokens){
                switch (token.type()){
                    case NUMBER -> {
                        stack.push(abstractFactory.makeConstant(token.text()));
                    }
                    case CELL, RANGE -> {
                        stack.push(abstractFactory.makeCell(token.text()));
                    }
                    case OPERATOR -> {
                        AbstractNode right = stack.pop();
                        AbstractNode left  = stack.pop();

                        stack.push(abstractFactory.makeOperator(token.text(), left, right));
                    }
                    case FUNCTION -> {
                        ArrayList<AbstractNode> args = new ArrayList<>();

                        while (!stack.isEmpty()){
                            args.add(stack.pop());
                        }

                        stack.push(abstractFactory.makeFunction(token.text(), args));
                    }
                    default -> {
                        throw new IllegalStateException("Unexpected token: " + token);
                    }
                }
            }

            try {
                isValidExpr(stack);
            } catch (IllegalStateException e){
                System.err.println(e.getMessage());

                return new ErrorNode();
            }

            return stack.pop();
        } catch (Exception e){
            System.err.println("Invalid expression: " + e.getMessage());

            return new ErrorNode();
        }
    }

    public List<String> getCellReferences(String expression){
        List<Token> tokens = Tokenizer.tokenize(expression);
        List<String> cellReferences = new ArrayList<>();

        for (Token token : tokens){
            if (token.type() == TokenType.CELL ||
                    token.type() == TokenType.RANGE){

                cellReferences.add(token.text());
            }
        }

        return cellReferences;
    }

    private void isValidExpr(Deque<AbstractNode> stack){
        if (stack.size() != 1){
            throw new IllegalStateException("Invalid expression");
        }
    }
}
