package sge.domain;

import java.time.LocalDate;

import sge.persistence.Arquivo;

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
      Nome: \{nome}
      CPF: \{cpf}
      E-mail: \{email}
      Data de Nascimento: \{Arquivo.FORMATO_DATA.format(dtNascimento)}
      Telefone: \{telefone}
      Endereço: \{endereco}
      Código da Turma: \{turma}""";
  }

}
