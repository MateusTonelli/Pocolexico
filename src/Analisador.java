import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analisador {

    List<String> reservedWords = new ArrayList<String>();
    
    List<String> foundIdentifiersList = new ArrayList<String>();
    List<String> foundReservedwordsList = new ArrayList<String>();
    List<Token> foundTokensList = new ArrayList<Token>();
    List<String> foundSymbolsList = new ArrayList<String>();

    int tokenSequence = 0;

    String fileContent = "";

    String sourceResult = "";

    private Map<TokenType, String> regEx;

    private List<Token> result;

    public Analisador() {
        SetReservedWords();
        regEx = new TreeMap<TokenType, String>();
        launchRegEx();
        result = new ArrayList<Token>();
    }

    public void SetReservedWords() {
        reservedWords.add("programa");
        reservedWords.add("Var");
        reservedWords.add("inteiro");
        reservedWords.add("começo");
        reservedWords.add("finaliza");
        reservedWords.add("mensagem");
        reservedWords.add("lertela");
        reservedWords.add("atribui");
        reservedWords.add("enquanto");
        reservedWords.add("fimenquanto");
        reservedWords.add("compara");
        reservedWords.add("senao");
        reservedWords.add("fimcompara");
        reservedWords.add("literal");
        reservedWords.add("decimal");
        reservedWords.add("laço");
        reservedWords.add("fimlaço");
    }

    public void tokenize(String source) {
        int position = 0;
        Token token = null;

        sourceResult = source;

        for (TokenType tokenType : TokenType.values()) {
            if (!tokenType.isAuxiliary() && (tokenType == TokenType.comentario || tokenType == TokenType.constanteLiteral || tokenType == TokenType.constanteNumerica)) {
                getTokenFindingByType(source, tokenType);
            }
        }

        do {
            token = getTokenMatchingByType(sourceResult, position);

            if (token != null) {
                position = token.getEnd();

                result.add(token);

                if(token.getTokenType().isIdentifier()){
                    foundIdentifiersList.add(token.toString());
                    if (!foundSymbolsList.contains(token.toString())) {
                        foundSymbolsList.add(token.toString());
                    }
                }

                if(!token.getTokenType().isAuxiliary()){
                    foundTokensList.add(token);
                }

                if (isPalavraReservada(token)){
                    foundReservedwordsList.add(token.toString());
                }
            }
        } while (token != null && position != sourceResult.length());
        if (position != sourceResult.length()) {
            //log error
        }
    }

    private void getTokenFindingByType(String source, TokenType tokenType) {
        int currentIndex = 0;
        String lexema = "";

        Pattern p;
        p = Pattern.compile(regEx.get(tokenType));

        Matcher m = p.matcher(source);
        Matcher m2 = p.matcher(sourceResult);

        do {
            if (m.find(currentIndex)) {
                lexema = m.group(0);


                tokenSequence += 1;
                Token token = new Token(tokenSequence, source.indexOf(lexema, currentIndex), source.indexOf(lexema, currentIndex) + lexema.length(), lexema, tokenType);

                foundTokensList.add(token);

                currentIndex = source.indexOf(lexema, currentIndex) + lexema.length();

                sourceResult = m2.replaceAll("");
            }else {
                break;
            }
        } while (currentIndex != source.length());
    }

    private Token getTokenMatchingByType(String source, int fromIndex) {
        if (fromIndex < 0 || fromIndex >= source.length()) {
            throw new IllegalArgumentException("Index inválido!");
        }
        for (TokenType tokenType : TokenType.values()) {
            if (!(tokenType == TokenType.comentario || tokenType == TokenType.constanteLiteral || tokenType == TokenType.constanteNumerica)) {
                Pattern p = Pattern.compile(".{" + fromIndex + "}" + regEx.get(tokenType), Pattern.DOTALL);

                Matcher m = p.matcher(source);
                if (m.matches()) {
                    String lexema = m.group(1);
                    tokenSequence += 1;
                    return new Token(tokenSequence, fromIndex, fromIndex + lexema.length(), lexema, tokenType);
                }
            }
        }

        return null;
    }

    public void CarregarArquivo(){
        File file = null;
        JFileChooser chooser = new JFileChooser();
        int returnValue = chooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            if(file.exists()){
                try {
                    fileContent = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    System.out.println("Arquivo inválido");
                }
            }else{
                System.out.println("Arquivo inválido");
            }
        }else{
            System.out.println("Arquivo inválido");
        }
    }

    public void AnalisarArquivo(){
        tokenize(fileContent);
    }

    public boolean isPalavraReservada(Token token) {
        return reservedWords.contains(token.getLexema());
    }

    public void ListarIdentificadores() {
        if (foundIdentifiersList.size() < 0) {
            System.out.println("Não foram encontrados identificadores.");
        }
        else {
            System.out.println("------ Identificadores encontrados no código ------");

            for (int i = 0; i < foundIdentifiersList.size(); i++) {
                System.out.println(foundIdentifiersList.get(i).toString());
            }
        }
    }

    public void ListarPalavrasReservadasEncotradas() {
        if (foundReservedwordsList.size() < 0) {
            System.out.println("Não foram encontradas palavras reservadas.");
        }
        else {
            System.out.println("------ Palavras reservadas encontradas no código ------");

            for (int i = 0; i < foundReservedwordsList.size(); i++) {
                System.out.println(foundReservedwordsList.get(i).toString());
            }
        }
    }

    public void ListarTokens() {
        if (foundTokensList.size() < 0) {
            System.out.println("Não foram encontrados tokens.");
        }
        else {
            System.out.println("------ Tokens encontrados no código ------");

            var foundTokenListOrdered = foundTokensList.stream().sorted((item1,item2)-> item1.getTokenSequence()<item2.getTokenSequence()?-1:1);

            foundTokenListOrdered.forEach((t) -> {
                if (foundSymbolsList.contains(t.toString())) {
                    System.out.println("<" + t.toString().replaceAll("(\r|\n)", "") +", " + foundSymbolsList.indexOf(t.toString()) + ">");
                }else{
                    System.out.println("<" + t.toString().replaceAll("(\r|\n)", "") +",>");
                }
            });

//            for (int i = 0; i < foundTokensList.size(); i++) {
//
//                if (foundSymbolsList.contains(foundTokensList.get(i).toString())) {
//                    System.out.println("<" + foundTokensList.get(i).toString().replaceAll("(\r|\n)", "") +", " + foundSymbolsList.indexOf(foundTokensList.get(i).toString()) + ">");
//                }else{
//                    System.out.println("<" + foundTokensList.get(i).toString().replaceAll("(\r|\n)", "") +",>");
//                }
//            }
        }
    }

    public void ListarTabelaDeSimbolos() {
        if (foundSymbolsList.size() < 0) {
            System.out.println("Não foram encontrados símbolos.");
        }
        else {
            System.out.println("------ Tabela de símbolos ------");

            for (int i = 0; i < foundSymbolsList.size(); i++) {
                System.out.println(i + "   |   " +  foundSymbolsList.get(i).toString());
            }
        }
    }
    //endregion

    //Mapeando as expressões regulares dos tokens
    private void launchRegEx() {
        regEx.put(TokenType.fimLinha, "(\\r).*");
        regEx.put(TokenType.novaLinha, "(\\n).*");
        regEx.put(TokenType.espaçoEmBranco, "( ).*");
        regEx.put(TokenType.tab, "(\\t).*");
        regEx.put(TokenType.comentario, "\\=>(.|[\\r\\n])*?\\r\\n");
        regEx.put(TokenType.constanteLiteral, "((?:'|\\').*(?:'|\\'))");
        regEx.put(TokenType.constanteNumerica, "\\b(?<!\\.)\\d+(?!\\.)\\b");
        regEx.put(TokenType.programa, "\\b(programa)\\b.*");
        regEx.put(TokenType.Var, "\\b(Var)\\b.*");
        regEx.put(TokenType.inteiro, "\\b(inteiro)\\b.*");
        regEx.put(TokenType.começo, "\\b(começo)\\b.*");
        regEx.put(TokenType.finaliza, "\\b(finaliza)\\b.*");
        regEx.put(TokenType.mensagem, "\\b(mensagem)\\b.*");
        regEx.put(TokenType.lertela, "\\b(lertela)\\b.*");
        regEx.put(TokenType.atribui, "\\b(atribui)\\b.*");
        regEx.put(TokenType.enquanto, "\\b(enquanto)\\b.*");
        regEx.put(TokenType.fimenquanto, "\\b(fimenquanto)\\b.*");
        regEx.put(TokenType.compara, "\\b(compara)\\b.*");
        regEx.put(TokenType.senao, "\\b(senao)\\b.*");
        regEx.put(TokenType.fimcompara, "\\b(fimcompara)\\b.*");
        regEx.put(TokenType.literal, "\\b(literal)\\b.*");
        regEx.put(TokenType.decimal, "\\b(decimal)\\b.*");
        regEx.put(TokenType.laço, "\\b(laço)\\b.*");
        regEx.put(TokenType.fimlaço, "\\b(fimlaço)\\b.*");
        regEx.put(TokenType.soma, "(\\+{1}).*");
        regEx.put(TokenType.subtrai, "(\\-{1}).*");
        regEx.put(TokenType.multiplica, "(\\*).*");
        regEx.put(TokenType.divide, "(/).*");
        regEx.put(TokenType.doispontos, "(:).*");
        regEx.put(TokenType.igual, "(=).*");
        regEx.put(TokenType.diferente, "(<>).*");
        regEx.put(TokenType.maior, "(>).*");
        regEx.put(TokenType.menor, "(<).*");
        regEx.put(TokenType.abreParenteses, "(\\().*");
        regEx.put(TokenType.fechaParenteses, "(\\)).*");
        regEx.put(TokenType.identificador, "\\b([a-zA-Z]{1}[0-9a-zA-Z_]{0,50})\\b.*");
    }

}