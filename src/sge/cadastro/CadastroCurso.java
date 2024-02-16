package sge.cadastro;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import sge.domain.Curso;
import sge.domain.Nivel;
import sge.domain.TipoCadastro;
import sge.exception.SgeException;
import sge.persistence.Arquivo;

public class CadastroCurso implements Cadastro {

  private static final String PATH_CURSOS = "data/cursos.csv";
  private Curso novoCurso;

  @Override
  public void validar(Map<String, String> campos) throws SgeException {
    var cargaHoraria = campos.get("cargaHoraria");
    if (!Objects.requireNonNull(cargaHoraria, "Carga horária é obrigatória.").matches("\\d+") ||
      Integer.parseInt(cargaHoraria) < 1) {
      throw new SgeException("Carga horária é inválida.");
    }

    var nivelEnum = Arrays
      .stream(Nivel.values())
      .filter(nivel ->
        nivel.name().equals(Objects.requireNonNull(campos.get("nivel"), "Nivel é obrigatório.")))
      .findAny()
      .orElseThrow(() -> new SgeException("Nivel é inválido."));

    if (this.carregarLista()
      .stream()
      .map(Curso.class::cast)
      .anyMatch(curso ->
        curso.codigo().equals(Objects.requireNonNull(campos.get("codigo"), "Código é obrigatório.")))) {
      throw new SgeException("Código de curso já cadastrado.");
    }

    this.novoCurso = new Curso(campos.get("codigo"),
      Objects.requireNonNull(campos.get("nome"), "Nome é obrigatório."),
      Integer.parseInt(cargaHoraria),
      nivelEnum);
  }

  @Override
  public List<TipoCadastro> carregarLista() throws SgeException {
    return Arquivo.carregarArquivo(PATH_CURSOS)
      .stream()
      .map(campo -> (TipoCadastro)
        new Curso(campo[0], campo[1], Integer.parseInt(campo[2]), Nivel.valueOf(campo[3])))
      .toList();
  }

  @Override
  public void salvar() throws SgeException {
    Arquivo.salvarLinha(PATH_CURSOS, String.format("%s,%s,%d,%s%n",
      this.novoCurso.codigo(),
      this.novoCurso.nome(),
      this.novoCurso.cargaHoraria(),
      this.novoCurso.nivel()));
  }

}
