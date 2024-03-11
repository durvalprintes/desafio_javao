package sge.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Curso(
  String codigo,
  String nome,
  int cargaHoraria,
  Nivel nivel) implements TipoCadastro {

  @Override
  @JsonIgnore
  public String[] getValores() {
    return new String[] {codigo,nome, String.valueOf(cargaHoraria), nivel.name()};
  }

  @Override
  @JsonIgnore
  public String getIdentificador() {
    return STR."\{codigo} - \{nome}";
  }

}
