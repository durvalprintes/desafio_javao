package sge.cadastro;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import sge.Sistema;
import sge.domain.Periodo;
import sge.domain.Turma;
import sge.exception.SgeException;

public class CadastroTurma extends NovoCadastro<Turma> {

  public CadastroTurma() {
    arquivo = Sistema.CAMINHO_ARQUIVO_TURMA;
  }

  @Override
  protected void inicializarCampos(Map<String, String> campos) {
    campos.put(Sistema.CAMPO_CODIGO, "o código");
    campos.put(Sistema.CAMPO_DT_INICIO, "a data de início (dd/mm/yyyy)");
    campos.put(Sistema.CAMPO_DT_FINAL, "a data de término (dd/mm/yyyy)");
    campos.put("periodo", "o período (MATUTINO, VESPERTINO, NOTURNO, SABADO)");
    campos.put("capacidade", "a capacidade");
    campos.put(Sistema.CAMPO_CODIGO_CURSO, "o código do curso");
  }

  @Override
  protected Turma criarTipoCadastro(String[] campo) {
    return new Turma(campo[0], LocalDate.parse(campo[1], Sistema.FORMATO_DATA),
      LocalDate.parse(campo[2], Sistema.FORMATO_DATA), Periodo.valueOf(campo[3]), Integer.parseInt(campo[4]), campo[5],
      new CadastroEstudante().calcularVagaDisponivel(campo[0], Integer.parseInt(campo[4])));
  }

  @Override
  public void validar() throws SgeException {
    if (!Objects
      .requireNonNull(campos.get(Sistema.CAMPO_DT_INICIO), "Data início é obrigatória.")
      .matches(Sistema.DATA_VALIDA)) {
      throw new SgeException("Data início com formato inválido.");
    }

    if (!Objects
    .requireNonNull(campos.get(Sistema.CAMPO_DT_FINAL), "Data final é obrigatória.")
    .matches(Sistema.DATA_VALIDA)) {
      throw new SgeException("Data final com formato inválido.");
    }

    var periodoEnum = Arrays
      .stream(Periodo.values())
      .filter(periodo ->
        periodo.name().equals(Objects.requireNonNull(campos.get("periodo"), "Período é obrigatório.")))
      .findAny()
      .orElseThrow(() -> new SgeException("Periodo é inválido."));

    var capacidade = campos.get("capacidade");
    if (!Objects
      .requireNonNull(capacidade, "Carga horária é obrigatória.")
      .matches(Sistema.NUMERO_VALIDO) ||
      Integer.parseInt(capacidade) < 1) {
      throw new SgeException("Carga horária é inválida.");
    }

    if (this.listar()
      .stream()
      .anyMatch(turma ->
        turma.codigo().equals(
          Objects.requireNonNull(campos.get(Sistema.CAMPO_CODIGO), "Código é obrigatório.")))) {
      throw new SgeException("Código de turma já cadastrado.");
    }

    if (new CadastroCurso().listar()
      .stream()
      .noneMatch(curso ->
        curso.codigo().equals(
          Objects.requireNonNull(campos.get(Sistema.CAMPO_CODIGO_CURSO), "Código do Curso é obrigatório.")))) {
      throw new SgeException("Curso não encontrado.");
    }

    cadastro = new Turma(campos.get(Sistema.CAMPO_CODIGO),
      LocalDate.parse(campos.get(Sistema.CAMPO_DT_INICIO), Sistema.FORMATO_DATA),
      LocalDate.parse(campos.get(Sistema.CAMPO_DT_FINAL), Sistema.FORMATO_DATA),
      periodoEnum, Integer.parseInt(capacidade),
      campos.get(Sistema.CAMPO_CODIGO_CURSO), 0);
  }

}
