package ExpressionHandler.Tokenizer;

public record Token(TokenType type, String text){

    public String toString(){
        return type + "('" + text + "')";
    }
}
