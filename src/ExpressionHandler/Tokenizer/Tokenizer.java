package ExpressionHandler.Tokenizer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "\\s*(?:" +
                    "([+-]?\\d+(?:\\.\\d+)?)|" +     // signed number
                    "([A-Z]+)(?=\\()|" +             // function name
                    "([A-Z]+\\d+:[A-Z]+\\d+)|" +     // range
                    "([A-Z]+\\d+)|" +                // cell
                    "([+\\-*/^])|" +                 // operator
                    "(\\()|" +                       // (
                    "(\\))" +                        // )
                    ")"
    );

    boolean isUnaryMinus(Token prev, Token cur) {
        return cur.type == TokenType.OPERATOR && cur.value.equals("-") &&
                (prev == null ||
                        prev.type == TokenType.OPERATOR ||
                        prev.type == TokenType.LPAREN);
    }

    public static List<Token> tokenize(String formula) {
        List<Token> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(formula);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group(1)));
            } else if (matcher.group(2) != null) {
                tokens.add(new Token(TokenType.OPERATOR, matcher.group(2)));
            } else if (matcher.group(3) != null) {
                tokens.add(new Token(TokenType.LPAREN, "("));
            } else if (matcher.group(4) != null) {
                tokens.add(new Token(TokenType.RPAREN, ")"));
            } else if (matcher.group(5) != null) {
                tokens.add(new Token(TokenType.RANGE, matcher.group(5)));
            } else if (matcher.group(6) != null) {
                tokens.add(new Token(TokenType.CELL, matcher.group(6)));
            } else if (matcher.group(7) != null) {
                tokens.add(new Token(TokenType.FUNCTION, matcher.group(7)));
            }
        }

        return tokens;
    }

    public static String[] tokenizedValues(String formula) {
        return tokenize(formula).stream()
                .map(token -> token.value)
                .toList().toArray(new String[0]);
    }
}
