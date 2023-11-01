package sge.domain;

public record Curso(String codigo, String nome, int cargaHoraria, Nivel nivel) {

  public String imprimir() {
    return String.format("""
      Código: %s
      Nome: %s
      Carga Horaria: %sh
      Nível: %s""", codigo, nome, cargaHoraria, nivel);
  }

}
