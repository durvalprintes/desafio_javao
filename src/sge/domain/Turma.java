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
    return STR."""
      Código: \{codigo}
      Data de Início: \{Arquivo.FORMATO_DATA.format(dtInicio)}
      Data de Término: \{ Arquivo.FORMATO_DATA.format(dtFinal)}
      Código do Curso: \{curso}
      Capacidade máxima: \{capacidade}
      Vagas disponíveis: \{new CadastroEstudante().calcularVagaDisponivel(codigo, capacidade)}""";
  }

}
