package sge.domain;

public record Curso(
  String codigo,
  String nome,
  int cargaHoraria,
  Nivel nivel) implements TipoCadastro {

  @Override
  public String toString() {
    return String.format("""
      Código: %s
      Nome: %s
      Carga Horária: %sh
      Nível: %s""",
      codigo, nome, cargaHoraria, nivel);
  }

}
