package ExpressionHandler.Tokenizer;

public class Token {
    String text;
    TokenType type;

    Token(TokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    public String toString() {
        return type + "('" + text + "')";
    }

    public String getText() {
        return text;
    }

    public TokenType getType() {
        return type;
    }
}
