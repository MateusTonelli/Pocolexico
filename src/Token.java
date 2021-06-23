public class Token {
    private int tokenSequence;

    private int startIndex;

    private int endIndex;

    private TokenType tokenType;

    private String lexema;

    public Token(int tokenSequence, int startIndex, int endIndex, String lexema, TokenType tokenType) {
        this.tokenSequence = tokenSequence;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.tokenType = tokenType;
        this.lexema = lexema;
    }

    public int getTokenSequence() {
        return tokenSequence;
    }

    public int getBegin() {
        return startIndex;
    }

    public int getEnd() {
        return endIndex;
    }

    public String getLexema() {
        return lexema;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    @Override
    public String toString() {
        return getLexema();
    }
}
