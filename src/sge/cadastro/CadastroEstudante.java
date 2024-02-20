package sge.cadastro;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

import sge.Sistema;
import sge.domain.Endereco;
import sge.domain.Estudante;
import sge.exception.SgeException;
import sge.persistence.util.ArquivoUtil;

public class CadastroEstudante extends NovoCadastro<Estudante> {

  public CadastroEstudante() {
    arquivo = ArquivoUtil.getTipoArquivo(Sistema.CAMINHO_ARQUIVO_ESTUDANTE, Estudante.class);
  }

  @Override
  protected void inicializarCampos(Map<String, String> campos) {
    campos.put("nome", "o nome");
    campos.put(Sistema.CAMPO_CPF, "o CPF");
    campos.put(Sistema.CAMPO_EMAIL, "o E-MAIL");
    campos.put(Sistema.CAMPO_DT_NASCIMENTO, "a data de nascimento (dd/mm/yyyy)");
    campos.put("telefone", "o telefone");
    campos.put("cep", "o CEP do endereço");
    campos.put("logradouro", "o logradouro do endereço");
    campos.put("numero", "o número do endereço");
    campos.put(Sistema.CAMPO_CODIGO_TURMA, "o código da turma");
  }

  @Override
  public void validar() throws SgeException {
    if (this.listar()
      .stream()
      .anyMatch(estudante ->
        estudante.cpf().equals(
          Objects.requireNonNull(campos.get(Sistema.CAMPO_CPF), "CPF é obrigatório.")) ||
        estudante.email().equals(
          Objects.requireNonNull(campos.get(Sistema.CAMPO_EMAIL), "E-MAIL é obrigatório.")))) {
      throw new SgeException("CPF e/ou E-MAIL já cadastrado.");
    }

    if (!Objects.requireNonNull(campos.get(Sistema.CAMPO_DT_NASCIMENTO), "Data de nascimento é obrigatória.")
      .matches(Sistema.DATA_VALIDA)) {
      throw new SgeException("Data de nascimento com formato inválido.");
    }

    if (new CadastroTurma().listar()
      .stream()
      .noneMatch(turma ->
        turma.codigo().equals(
          Objects.requireNonNull(campos.get(Sistema.CAMPO_CODIGO_TURMA), "Código de Turma é obrigatório.")) &&
        LocalDate.now().isBefore(turma.dtInicio()) &&
        this.calcularVagaDisponivel(turma.codigo(), turma.capacidade()) > 0)) {
      throw new SgeException("Código de turma não encontrado ou, sem vaga disponível ou, turma já iniciada.");
    }

    cadastro = new Estudante(
      Objects.requireNonNull(campos.get("nome"), "Nome é obrigatória."),
      campos.get(Sistema.CAMPO_CPF),
      campos.get(Sistema.CAMPO_EMAIL),
      LocalDate.parse(campos.get(Sistema.CAMPO_DT_NASCIMENTO), Sistema.FORMATO_DATA),
      Objects.requireNonNull(campos.get("telefone"), "Telefone é obrigatório."),
      new Endereco(
        Objects.requireNonNull(campos.get("cep"), "CEP é obrigatório."),
        Objects.requireNonNull(campos.get("logradouro"), "Logradouro é obrigatório."),
        Objects.requireNonNull(campos.get("numero"), "Número de endereço é obrigatório.")),
      campos.get(Sistema.CAMPO_CODIGO_TURMA));
  }

  public int calcularVagaDisponivel(String codigoTurma, int capacidadeTurma) {
    return capacidadeTurma - Math.toIntExact(
      this.listar()
        .stream()
        .filter(estudante -> estudante.turma().equals(codigoTurma))
        .count());
  }

}
