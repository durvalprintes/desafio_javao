package sge.cadastro;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

import sge.Sistema;
import sge.domain.Endereco;
import sge.domain.Estudante;
import sge.domain.TipoCadastro;
import sge.exception.SgeException;
import sge.persistence.util.ArquivoUtil;

public class CadastroEstudante extends NovoCadastro<Estudante> {

  public CadastroEstudante() {
    arquivo = ArquivoUtil.getTipoArquivo(Sistema.CAMINHO_ARQUIVO_ESTUDANTE, Estudante.class);
  }

  @Override
  protected void inicializarCampos(Map<String, String> campos) {
    campos.put(Sistema.CAMPO_NOME, "o nome");
    campos.put(Sistema.CAMPO_CPF, "o CPF");
    campos.put(Sistema.CAMPO_EMAIL, "o E-MAIL");
    campos.put(Sistema.CAMPO_DT_NASCIMENTO, "a data de nascimento");
    campos.put(Sistema.CAMPO_TELEFONE, "o telefone");
    campos.put(Sistema.CAMPO_CEP_ENDERECO, "o CEP do endereço");
    campos.put(Sistema.CAMPO_LOGRADURO_ENDERECO, "o logradouro do endereço");
    campos.put(Sistema.CAMPO_NUMERO_ENDERECO, "o número do endereço");
  }

  @Override
  public void validar() throws SgeException {
    if (!operacao.equals(Operacao.EXCLUIR)) {
      if (this.verificarLista()
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
              Objects.requireNonNull(campos.get(Sistema.CAMPO_TURMA), "Código de Turma é obrigatório.")) &&
            LocalDate.now().isBefore(turma.dtInicio()))) {
        throw new SgeException("Código de turma não encontrado ou, turma já iniciada.");
      }

      cadastro = new Estudante(
          Objects.requireNonNull(campos.get(Sistema.CAMPO_NOME), "Nome é obrigatória."),
          campos.get(Sistema.CAMPO_CPF),
          campos.get(Sistema.CAMPO_EMAIL),
          LocalDate.parse(campos.get(Sistema.CAMPO_DT_NASCIMENTO), Sistema.FORMATO_DATA),
          Objects.requireNonNull(campos.get(Sistema.CAMPO_TELEFONE), "Telefone é obrigatório."),
          new Endereco(
              Objects.requireNonNull(campos.get(Sistema.CAMPO_CEP_ENDERECO), "CEP é obrigatório."),
              Objects.requireNonNull(campos.get(Sistema.CAMPO_LOGRADURO_ENDERECO), "Logradouro é obrigatório."),
              Objects.requireNonNull(campos.get(Sistema.CAMPO_NUMERO_ENDERECO), "Número de endereço é obrigatório.")));
    }
  }

  @Override
  public Map<String, String> setCampos(TipoCadastro cadastro) {
    if (cadastro instanceof Estudante(String nome, String cpf, String email, LocalDate dtNascimento, String telefone,
      Endereco(String cep, String logradouro, String numero))) {
      campos.put(Sistema.CAMPO_NOME, nome);
      campos.put(Sistema.CAMPO_CPF, cpf);
      campos.put(Sistema.CAMPO_EMAIL, email);
      campos.put(Sistema.CAMPO_DT_NASCIMENTO, Sistema.FORMATO_DATA.format(dtNascimento));
      campos.put(Sistema.CAMPO_TELEFONE, telefone);
      campos.put(Sistema.CAMPO_CEP_ENDERECO, cep);
      campos.put(Sistema.CAMPO_LOGRADURO_ENDERECO, logradouro);
      campos.put(Sistema.CAMPO_NUMERO_ENDERECO, numero);
      return campos;
    }
    throw new SgeException("Tipo de cadastro incorreto");
  }

}
