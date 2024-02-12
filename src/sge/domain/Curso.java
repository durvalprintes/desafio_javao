package sge.domain;

public record Curso(
  String codigo,
  String nome,
  int cargaHoraria,
  Nivel nivel) implements TipoCadastro {

  @Override
  public String toString() {
    return STR."""
      Código: \{codigo}
      Nome: \{nome}
      Carga Horária: \{cargaHoraria}h
      Nível: \{nivel}""";
  }

}
