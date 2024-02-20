package sge.domain;

import java.time.LocalDate;

import sge.Sistema;

public record Estudante (
  String nome,
  String cpf,
  String email,
  LocalDate dtNascimento,
  String telefone,
  Endereco endereco,
  String turma) implements TipoCadastro {

  @Override
  public String toString() {
    return STR."""
      Nome:\{nome}
      CPF:\{cpf}
      E-mail:\{email}
      Data de Nascimento:\{Sistema.FORMATO_DATA.format(dtNascimento)}
      Telefone:\{telefone}
      Endereço:\{endereco}
      Código da Turma:\{turma}""";
  }

}
