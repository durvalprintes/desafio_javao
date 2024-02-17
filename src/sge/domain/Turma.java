package sge.domain;

import java.time.LocalDate;

import sge.Sistema;

public record Turma (
  String codigo,
  LocalDate dtInicio,
  LocalDate dtFinal,
  Periodo periodo,
  int capacidade,
  String curso,
  int capacidadeAtual) implements TipoCadastro {

  @Override
  public String toString() {
    return STR."""
      Código:\{codigo}
      Data de Início:\{Sistema.FORMATO_DATA.format(dtInicio)}
      Data de Término:\{Sistema.FORMATO_DATA.format(dtFinal)}
      Periodo:\{periodo}
      Código do Curso:\{curso}
      Capacidade máxima:\{capacidade}
      Vagas disponíveis:\{capacidadeAtual}""";
  }

  @Override
  public String salvar() {
    return STR."""
      \{codigo},\{Sistema.FORMATO_DATA.format(dtInicio)},{Sistema.FORMATO_DATA.format(dtFinal)},\{periodo},\
      \{capacidade},\{curso}
      """;
  }

}
