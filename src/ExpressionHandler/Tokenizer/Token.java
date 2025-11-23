package ExpressionHandler.Tokenizer;

public class Token {
    String value;
    TokenType type;

    Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return type + "('" + value + "')";
    }

    public String getText() {
        return value;
    }
    public TokenType getType() {
        return type;
    }
}
