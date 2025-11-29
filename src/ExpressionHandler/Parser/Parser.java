package ExpressionHandler.Parser;

import ExpressionHandler.AbstractNodes.AbstractNode;
import ExpressionHandler.Tokenizer.Token;
import ExpressionHandler.Tokenizer.TokenType;
import ExpressionHandler.AbstractFactory.AbstractFactory;
import ExpressionHandler.Tokenizer.Tokenizer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/*
first tokenize
then shunting algorithm (postfix)
then create tree (recursion)

add errorNode?
 */

public class Parser {
    final Tokenizer tokenizer;
    final AbstractFactory abstractFactory;

    public Parser(AbstractFactory abstractFactory) {
        this.tokenizer       = new Tokenizer();
        this.abstractFactory = abstractFactory;
    }

    private static int precedence(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            default -> 0;
        };
    }

    private static List<Token> shuntingYard(List<Token> infixTokens) {
        List<Token>  postfixTokens = new ArrayList<>();
        Deque<Token> operatorStack = new ArrayDeque<>();

        for (Token token : infixTokens) {
            switch (token.getType()) {
                case TokenType.CELL:
                case TokenType.NUMBER:
                    postfixTokens.add(token);

                    break;
                case TokenType.OPERATOR:
                    while (true) {
                        Token top = operatorStack.peek();

                        if (top == null || top.getType() != TokenType.OPERATOR){
                            break;
                        }

                        String a = top.getText();
                        String b = token.getText();

                        if (precedence(a) < precedence(b)){
                            break;
                        }

                        postfixTokens.add(operatorStack.pop());
                    }

                    operatorStack.push(token);

                    break;
                case TokenType.LPAREN:
                    operatorStack.push(token);

                    break;
                case TokenType.RPAREN:
                    // pop until '(' is found
                    while (true) {
                        if (operatorStack.isEmpty()) {
                            throw new IllegalArgumentException("Mismatched parentheses [more ')'].");
                        }

                        Token top = operatorStack.pop();

                        if (top.getType() == TokenType.LPAREN) {
                            break; // matched
                        }

                        postfixTokens.add(top);
                    }

                    break;
                default:
                    throw new IllegalArgumentException("Unexpected token: " + token);
            }
        }

        while (!operatorStack.isEmpty()) {
            Token token = operatorStack.pop();
            if (token.getType() == TokenType.LPAREN) {
                // more '(' than ')'
                throw new IllegalArgumentException("Mismatched parentheses [more '(']");
            }

            postfixTokens.add(token);
        }

        return postfixTokens;
    }

    public AbstractNode getExpression(String expression) {
        List<Token> infixTokens = Tokenizer.tokenize(expression);
        List<Token> postfixTokens = shuntingYard(infixTokens);

        Deque<AbstractNode> stack = new ArrayDeque<>();

        for (Token token : postfixTokens) {
            switch (token.getType()) {
                case NUMBER -> {
                    stack.push(abstractFactory.makeConstant(token.getText()));
                }
                case CELL -> {
                    stack.push(abstractFactory.makeCell(token.getText()));
                }
                case OPERATOR -> {
                    AbstractNode right = stack.pop();
                    AbstractNode left  = stack.pop();

                    stack.push(abstractFactory.makeOperator(token.getText(), left, right));
                }
                default -> throw new IllegalStateException("Unexpected token: " + token);
            }
        }

        if (stack.size() != 1)
            throw new IllegalStateException("Invalid expression");

        return stack.pop();
    }
}
