package sge.cadastro;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import sge.domain.Endereco;
import sge.domain.Estudante;
import sge.domain.TipoCadastro;
import sge.domain.Turma;
import sge.exception.SgeException;
import sge.persistence.Arquivo;

public class CadastroEstudante implements Cadastro {

  private static final String PATH_ESTUDANTES = "data/estudantes.csv";
  private Estudante novoEstudante;

  @Override
  public Map<String, String> informarCampos() {
    return new LinkedHashMap<>() {{
      put("nome", "o nome");
      put("cpf", "o cpf");
      put("email", "o email");
      put("dtNascimento", "a data de nascimento (dd/mm/yyyy)");
      put("telefone", "o telefone");
      put("cep", "o cep do endereço");
      put("logradouro", "o logradouro do endereço");
      put("numero", "o numero do endereço");
      put("codigoTurma", "o código da turma");
    }};
  }

  @Override
  public void validar(Map<String, String> campos) throws SgeException {
    if (this.carregarLista()
      .stream()
      .map(Estudante.class::cast)
      .anyMatch(estudante ->
        estudante.cpf().equals(Objects.requireNonNull(campos.get("cpf"), "CPF é obrigatório.")) ||
        estudante.email().equals(Objects.requireNonNull(campos.get("email"), "EMAIL é obrigatório.")))) {
      throw new SgeException("CPF e/ou EMAIl já cadastrado.");
    }

    if (!Objects.requireNonNull(campos.get("dtNascimento"), "Data de nascimento é obrigatória.")
      .matches("\\d{2}/\\d{2}/\\d{4}")) {
      throw new SgeException("Data de nascimento com formato inválido.");
    }

    var cadastroTurma = new CadastroTurma();
    if (cadastroTurma.carregarLista()
      .stream()
      .map(Turma.class::cast)
      .noneMatch(turma ->
        turma.codigo().equals(
          Objects.requireNonNull(campos.get("codigoTurma"), "Código de Turma é obrigatório.")) &&
        LocalDate.now().isBefore(turma.dtInicio()) &&
        this.calcularVagaDisponivel(turma.codigo(), turma.capacidade()) > 0)) {
      throw new SgeException("Código de turma não encontrado ou, sem vaga disponível ou, turma já iniciada.");
    }

    this.novoEstudante = new Estudante(
      Objects.requireNonNull(campos.get("nome"), "Nome é obrigatória."),
      campos.get("cpf"),
      campos.get("email"),
      LocalDate.parse(campos.get("dtNascimento"), Arquivo.FORMATO_DATA),
      Objects.requireNonNull(campos.get("telefone"), "Telefone é obrigatório."),
      new Endereco(
        Objects.requireNonNull(campos.get("cep"), "CEP é obrigatório."),
        Objects.requireNonNull(campos.get("logradouro"), "Logradouro é obrigatório."),
        Objects.requireNonNull(campos.get("numero"), "Número de endereço é obrigatório.")),
      campos.get("codigoTurma"));
  }

  @Override
  public void salvar() throws SgeException {
    Arquivo.salvarLinha(PATH_ESTUDANTES, STR."""
      \{this.novoEstudante.nome()},\
      \{this.novoEstudante.cpf()},\
      \{this.novoEstudante.email()},\
      \{Arquivo.FORMATO_DATA.format(this.novoEstudante.dtNascimento())},\
      \{this.novoEstudante.telefone()},\
      \{this.novoEstudante.endereco().cep()},\
      \{this.novoEstudante.endereco().logradouro()},\
      \{this.novoEstudante.endereco().numero()},\
      \{this.novoEstudante.turma()}
      """);
  }

  @Override
  public List<TipoCadastro> carregarLista() throws SgeException {
    return Arquivo.carregarArquivo(PATH_ESTUDANTES)
      .stream()
      .map(campo -> (TipoCadastro)
        new Estudante(campo[0], campo[1], campo[2], LocalDate.parse(campo[3], Arquivo.FORMATO_DATA), campo[4],
          new Endereco(campo[5], campo[6], campo[7]), campo[8])).toList();
  }

  public int calcularVagaDisponivel(String codigoTurma, int capacidadeTurma) {
    return capacidadeTurma - Math.toIntExact(
      this.carregarLista()
        .stream()
        .map(Estudante.class::cast)
        .filter(estudante -> estudante.turma().equals(codigoTurma))
        .count());
  }

}
