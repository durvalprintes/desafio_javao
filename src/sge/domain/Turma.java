package sge.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import sge.Sistema;

public record Turma(
    String codigo,
    LocalDate dtInicio,
    LocalDate dtFinal,
    Periodo periodo,
    int capacidade,
    String curso) implements TipoCadastro {

  @Override
  @JsonIgnore
  public String[] getValores() {
    return new String[] { codigo, Sistema.FORMATO_DATA.format(dtInicio), Sistema.FORMATO_DATA.format(dtFinal),
        periodo.name(), String.valueOf(capacidade), curso };
  }

  @Override
  @JsonIgnore
  public String getIdentificador() {
    return codigo;
  }

}
