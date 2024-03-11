package sge.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
  @JsonIgnore
  public String[] getValores() {
    return new String[]{nome, cpf, email, Sistema.FORMATO_DATA.format(dtNascimento),telefone,endereco.cep(),
      endereco.logradouro(), endereco.numero(),turma};
  }

  @Override
  @JsonIgnore
  public String getIdentificador() {
    return cpf;
  }

}
