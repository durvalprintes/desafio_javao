package sge.domain;

import java.time.LocalDate;
import java.util.List;

import sge.persistence.Arquivo;

public record Turma (
  String codigo,
  LocalDate dtInicio,
  LocalDate dtFinal,
  Periodo periodo,
  int capacidade,
  String curso) {

  public int calcularCapacidadeAtual(List<Estudante> estudantes) {
    return capacidade - Math.toIntExact(estudantes.stream().filter(estudante -> estudante.turma().equals(codigo))
      .count());
  }

  public String imprimir(List<Estudante> estudantes) {
    return String.format("""
      Código: %s
      Data de Início: %s
      Data de Término: %s
      Capacidade atual: %d
      Código do Curso: %s""", 
      codigo, Arquivo.FORMATO_DATA.format(dtInicio), Arquivo.FORMATO_DATA.format(dtFinal), 
      this.calcularCapacidadeAtual(estudantes), 
      curso);
  }

}
