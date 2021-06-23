import javax.swing.*;
import java.io.File;

public class Main {

    public static void main(String[] args) {
        Analisador analisador = new Analisador();

        analisador.CarregarArquivo();
        analisador.AnalisarArquivo();
        analisador.ListarTabelaDeSimbolos();
        analisador.ListarIdentificadores();
        analisador.ListarTokens();
        analisador.ListarPalavrasReservadasEncotradas();
    }

}
