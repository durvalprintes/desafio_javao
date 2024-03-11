package sge.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Endereco (
  String cep,
  String logradouro,
  String numero) implements TipoCadastro {

  @Override
  @JsonIgnore
  public String[] getValores() {
    return new String[] {cep, logradouro, numero};
  }

  @Override
  @JsonIgnore
  public String getIdentificador() {
    return cep;
  }

}
