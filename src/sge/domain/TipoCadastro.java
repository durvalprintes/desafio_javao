package sge.domain;

public sealed interface TipoCadastro
    permits Curso, Turma, Estudante, Endereco {

    String toString();

    String salvar();

}
