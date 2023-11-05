package sge.domain;

public sealed interface Item permits Curso, Turma, Estudante {

    String imprimir();

}
