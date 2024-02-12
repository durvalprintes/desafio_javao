package sge.cadastro;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import sge.domain.Periodo;
import sge.domain.TipoCadastro;
import sge.domain.Turma;
import sge.exception.SgeException;
import sge.persistence.Arquivo;

public class CadastroTurma implements Cadastro {

  private static final String PATH_TURMAS = "data/turmas.csv";
  private static final String FORMATO_DATA = "\\d{2}/\\d{2}/\\d{4}";
  private Turma novaTurma;

  @Override
  public Map<String, String> informarCampos() {
    return new LinkedHashMap<>() {{
      put("codigo", "o código");
      put("dtInicio", "a data de início (dd/mm/yyyy)");
      put("dtFinal", "a data de término (dd/mm/yyyy)");
      put("periodo", "o período (MATUTINO, VESPERTINO, NOTURNO, SABADO)");
      put("capacidade", "a capacidade (HORAS)");
      put("codigoCurso", "o código do curso");
    }};
  }

  @Override
  public void validar(Map<String, String> campos) throws SgeException {
    if (!Objects.requireNonNull(campos.get("dtInicio"), "Data início é obrigatória.").matches(FORMATO_DATA)) {
      throw new SgeException("Data início com formato inválido.");
    }

    if (!Objects.requireNonNull(campos.get("dtFinal"), "Data final é obrigatória.").matches(FORMATO_DATA)) {
      throw new SgeException("Data final com formato inválido.");
    }

    var periodoEnum = Arrays
      .stream(Periodo.values())
      .filter(periodo ->
        periodo.name().equals(Objects.requireNonNull(campos.get("periodo"), "Período é obrigatório.")))
      .findAny()
      .orElseThrow(() -> new SgeException("Periodo é inválido."));

    var capacidade = campos.get("capacidade");
    if (!Objects.requireNonNull(capacidade, "Carga horária é obrigatória.").matches("\\d+") ||
      Integer.parseInt(capacidade) < 1) {
      throw new SgeException("Carga horária é inválida.");
    }

    if (this.carregarLista()
      .stream()
      .map(Turma.class::cast)
      .anyMatch(turma ->
        turma.codigo().equals(Objects.requireNonNull(campos.get("codigo"), "Código é obrigatório.")))) {
      throw new SgeException("Código de turma já cadastrado.");
    }

    if (new CadastroCurso().carregarLista()
      .stream()
      .map(Turma.class::cast)
      .noneMatch(curso ->
        curso.codigo().equals(
          Objects.requireNonNull(campos.get("codigoCurso"), "Código do Curso é obrigatório.")))) {
      throw new SgeException("Curso não encontrado");
    }

    this.novaTurma = new Turma(campos.get("codigo"),
      LocalDate.parse(campos.get("dtInicio"), Arquivo.FORMATO_DATA),
      LocalDate.parse(campos.get("dtFinal"), Arquivo.FORMATO_DATA),
      periodoEnum, Integer.parseInt(capacidade),
      campos.get("codigoCurso"), 0);
  }

  @Override
  public void salvar() throws SgeException {
    Arquivo.salvarLinha(PATH_TURMAS, STR."""
      \{this.novaTurma.codigo()},\
      \{Arquivo.FORMATO_DATA.format(this.novaTurma.dtInicio())},\
      \{Arquivo.FORMATO_DATA.format(this.novaTurma.dtFinal())},\
      \{this.novaTurma.periodo()},\
      \{this.novaTurma.capacidade()},\
      \{this.novaTurma.curso()}
      """);
  }

  @Override
  public List<TipoCadastro> carregarLista() throws SgeException {
    return Arquivo.carregarArquivo(PATH_TURMAS)
      .stream()
      .map(campo -> (TipoCadastro)
        new Turma(campo[0], LocalDate.parse(campo[1], Arquivo.FORMATO_DATA),
          LocalDate.parse(campo[2], Arquivo.FORMATO_DATA), Periodo.valueOf(campo[3]),
          Integer.parseInt(campo[4]), campo[5], 0)).toList();
  }

}
