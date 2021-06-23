public enum TokenType {
    fimLinha,
    novaLinha,
    espaçoEmBranco,
    tab,
    programa,
    Var,
    inteiro,
    começo,
    finaliza,
    mensagem,
    lertela,
    atribui,
    enquanto,
    fimenquanto,
    compara,
    senao,
    fimcompara,
    literal,
    decimal,
    laço,
    fimlaço,
    doispontos,
    soma,
    subtrai,
    multiplica,
    divide,
    diferente,
    menor,
    identificador,
    constanteNumerica,
    comentario,
    maior,
    igual,
    abreParenteses,
    fechaParenteses,
    constanteLiteral;


    public boolean isOperator() {
        return this == soma || this == subtrai || this == multiplica || this == divide
                || this == igual || this == diferente || this == maior || this == menor
                || this == abreParenteses || this == fechaParenteses;
    }
    
    public boolean isAuxiliary() {
        return this == fimLinha || this == novaLinha || this == espaçoEmBranco || this == comentario || this == tab ;
    }

    public boolean isIdentifier() {
        return this == identificador;
    }
}

