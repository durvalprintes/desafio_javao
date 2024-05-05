package sge.cadastro;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import sge.Sistema;
import sge.domain.Periodo;
import sge.domain.TipoCadastro;
import sge.domain.Turma;
import sge.exception.SgeException;
import sge.persistence.util.ArquivoUtil;

public class CadastroTurma extends NovoCadastro<Turma> {

  public CadastroTurma() {
    arquivo = ArquivoUtil.getTipoArquivo(Sistema.CAMINHO_ARQUIVO_TURMA, Turma.class);
  }

  @Override
  protected void inicializarCampos(Map<String, String> campos) {
    campos.put(Sistema.CAMPO_CODIGO, "o código");
    campos.put(Sistema.CAMPO_DT_INICIO, "a data de início");
    campos.put(Sistema.CAMPO_DT_FINAL, "a data de término");
    campos.put(Sistema.CAMPO_PERIODO, "o período");
    campos.put(Sistema.CAMPO_CAPACIDADE, "a capacidade");
    campos.put(Sistema.CAMPO_CURSO, "o curso");
    campos.put("capacidadeAtual", null);
  }

  @Override
  public void validar() throws SgeException {
    if (!operacao.equals(Operacao.EXCLUIR)) {
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
            periodo.name().equals(Objects.requireNonNull(campos.get(Sistema.CAMPO_PERIODO), "Período é obrigatório.")))
          .findAny()
          .orElseThrow(() -> new SgeException("Periodo é inválido."));

      var capacidade = campos.get(Sistema.CAMPO_CAPACIDADE);
      if (!Objects
          .requireNonNull(capacidade, "Carga horária é obrigatória.")
          .matches(Sistema.NUMERO_VALIDO) ||
          Integer.parseInt(capacidade) < 1) {
        throw new SgeException("Carga horária é inválida.");
      }

      if (this.verificarLista()
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
              Objects.requireNonNull(campos.get(Sistema.CAMPO_CURSO), "Código do Curso é obrigatório.")))) {
        throw new SgeException("Curso não encontrado.");
      }

      cadastro = new Turma(campos.get(Sistema.CAMPO_CODIGO),
          LocalDate.parse(campos.get(Sistema.CAMPO_DT_INICIO), Sistema.FORMATO_DATA),
          LocalDate.parse(campos.get(Sistema.CAMPO_DT_FINAL), Sistema.FORMATO_DATA),
          periodoEnum, Integer.parseInt(campos.get(Sistema.CAMPO_CAPACIDADE)),
          campos.get(Sistema.CAMPO_CURSO));
    }
  }

  @Override
  public Map<String, String> setCampos(TipoCadastro cadastro) {
    if (cadastro instanceof Turma(String codigo, LocalDate dtInicio, LocalDate dtFinal, Periodo periodo, int capacidade,
     String curso)) {
      campos.put(Sistema.CAMPO_CODIGO, codigo);
      campos.put(Sistema.CAMPO_DT_INICIO, Sistema.FORMATO_DATA.format(dtInicio));
      campos.put(Sistema.CAMPO_DT_FINAL, Sistema.FORMATO_DATA.format(dtFinal));
      campos.put(Sistema.CAMPO_PERIODO, periodo.name());
      campos.put(Sistema.CAMPO_CAPACIDADE, String.valueOf(capacidade));
      campos.put(Sistema.CAMPO_CURSO, curso);
      return campos;
    }
    throw new SgeException("Tipo de cadastro incorreto");
  }

}
