package ExpressionHandler.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "([+-]?\\d+(?:\\.\\d+)?)|" +       // number
                    "([A-Z][A-Z0-9]*)(?=\\()|" +       // function name
                    "([A-Z]+\\d+:[A-Z]+\\d+)|" +       // range (cell:cell)
                    "([A-Z]+\\d+)|" +                  // single cell
                    "([+\\-*/^])|" +                   // operators
                    "(\\()|" +                         // left paren
                    "(\\))"                            // right paren
    );

    boolean isUnaryMinus(Token prev, Token cur) {
        return cur.type == TokenType.OPERATOR && cur.getText().equals("-") &&
                (prev == null ||
                        prev.type == TokenType.OPERATOR ||
                        prev.type == TokenType.LPAREN);
    }

    public static List<Token> tokenize(String formula) {
        if (formula.startsWith("=")) {
            formula = formula.substring(1);
        }
        /*else error*/

        List<Token> tokens = new ArrayList<>();
        int pos = 0;

        while (pos < formula.length()) {
            // skip whitespace
            while (pos < formula.length() && Character.isWhitespace(formula.charAt(pos))) pos++;

            Matcher matcher = TOKEN_PATTERN.matcher(formula.substring(pos));
            if (matcher.lookingAt()) {
                String tokenText = matcher.group();
                pos += tokenText.length();

                if (matcher.group(1) != null) {
                    tokens.add(new Token(TokenType.NUMBER, tokenText));
                } else if (matcher.group(2) != null) {
                    tokens.add(new Token(TokenType.FUNCTION, tokenText));
                } else if (matcher.group(3) != null) {
                    tokens.add(new Token(TokenType.RANGE, tokenText));
                } else if (matcher.group(4) != null) {
                    tokens.add(new Token(TokenType.CELL, tokenText));
                } else if (matcher.group(5) != null) {
                    tokens.add(new Token(TokenType.OPERATOR, tokenText));
                } else if (matcher.group(6) != null) {
                    tokens.add(new Token(TokenType.LPAREN, tokenText));
                } else if (matcher.group(7) != null) {
                    tokens.add(new Token(TokenType.RPAREN, tokenText));
                }
            } else {
                throw new RuntimeException("Unrecognized token at: " + formula.substring(pos));
            }
        }

        return tokens;
    }

    public static String[] tokenizedValues(String formula) {
        return tokenize(formula).stream()
                .map(Token::getText)
                .toList().toArray(new String[0]);
    }
}
