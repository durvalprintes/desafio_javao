package sge.domain;

import java.time.LocalDate;

import sge.cadastro.CadastroEstudante;
import sge.persistence.Arquivo;

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
    return String.format("""
      Código: %s
      Data de Início: %s
      Data de Término: %s
      Código do Curso: %s
      Capacidade máxima: %s
      Vagas disponíveis: %s""",
      codigo, Arquivo.FORMATO_DATA.format(dtInicio), Arquivo.FORMATO_DATA.format(dtFinal),
      curso, capacidade, new CadastroEstudante().calcularVagaDisponivel(codigo, capacidade));
  }

}
