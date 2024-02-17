package sge.cadastro;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import sge.Sistema;
import sge.domain.Curso;
import sge.domain.Nivel;
import sge.exception.SgeException;

public class CadastroCurso extends NovoCadastro<Curso> {

  public CadastroCurso() {
    arquivo = Sistema.CAMINHO_ARQUIVO_CURSO;
  }

  @Override
  protected void inicializarCampos(Map<String, String> campos) {
    campos.put(Sistema.CAMPO_CODIGO, "o codigo");
    campos.put("nome", "o nome");
    campos.put(Sistema.CAMPO_CARGA_HORARIA, "a carga horária (HORAS)");
    campos.put("nivel", "o nível (BASICO, INTERMEDIARIO, AVANCADO)");
  }

  @Override
  protected Curso criarTipoCadastro(String[] campo) {
    return new Curso(campo[0], campo[1], Integer.parseInt(campo[2]), Nivel.valueOf(campo[3]));
  }

  @Override
  public void validar() throws SgeException {
    if (!Objects
      .requireNonNull(campos.get(Sistema.CAMPO_CARGA_HORARIA), "Carga horária é obrigatória.")
      .matches(Sistema.NUMERO_VALIDO) ||
      Integer.parseInt(campos.get(Sistema.CAMPO_CARGA_HORARIA)) < 1) {
      throw new SgeException("Carga horária é inválida.");
    }

    var nivelEnum = Arrays
      .stream(Nivel.values())
      .filter(nivel ->
        nivel.name().equals(Objects.requireNonNull(campos.get("nivel"), "Nivel é obrigatório.")))
      .findAny()
      .orElseThrow(() -> new SgeException("Nivel é inválido."));

    if (this.listar()
      .stream()
      .anyMatch(curso ->
        curso.codigo().equals(
          Objects.requireNonNull(campos.get(Sistema.CAMPO_CODIGO), "Código é obrigatório.")))) {
      throw new SgeException("Código de curso já cadastrado.");
    }

    cadastro = new Curso(campos.get(Sistema.CAMPO_CODIGO),
      Objects.requireNonNull(campos.get("nome"), "Nome é obrigatório."),
      Integer.parseInt(campos.get(Sistema.CAMPO_CARGA_HORARIA)),
      nivelEnum);
  }

}
