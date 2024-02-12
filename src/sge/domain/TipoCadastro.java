package sge.domain;

public sealed interface TipoCadastro permits Curso, Turma, Estudante {

    String toString();

}
