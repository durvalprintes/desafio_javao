package sge.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sge.domain.Curso;
import sge.domain.Endereco;
import sge.domain.Estudante;
import sge.domain.Nivel;
import sge.domain.Periodo;
import sge.domain.Turma;
import sge.exception.SgeException;

public class Arquivo {

  public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private static final String PATH_CURSOS = "data/cursos.csv";
  private static final String PATH_TURMAS = "data/turmas.csv";
  private static final String PATH_ESTUDANTES = "data/estudantes.csv";

  private Arquivo() {}

  public static List<Curso> carregarCursosCadastrados() throws SgeException {
    var cursos = new ArrayList<Curso>();
    carregarArquivo(PATH_CURSOS).forEach(campo -> 
      cursos.add(
        new Curso(campo[0], campo[1], Integer.parseInt(campo[2]), Nivel.valueOf(campo[3]))));
    return cursos;
  }

  public static List<Turma> carregarTurmasCadastradas() throws SgeException {
    var turmas = new ArrayList<Turma>();
    carregarArquivo(PATH_TURMAS).forEach(campo -> 
      turmas.add(
        new Turma(campo[0], LocalDate.parse(campo[1], FORMATO_DATA), 
        LocalDate.parse(campo[2], FORMATO_DATA), Periodo.valueOf(campo[3]), 
        Integer.parseInt(campo[4]), campo[5])));
    return turmas;
  }

  public static List<Estudante> carregarEstudantesCadastrados() throws SgeException {
    var estudantes = new ArrayList<Estudante>();
    carregarArquivo(PATH_ESTUDANTES).forEach(campo -> 
      estudantes.add(
        new Estudante(campo[0], campo[1], campo[2], LocalDate.parse(campo[3], FORMATO_DATA), campo[4], 
          new Endereco(campo[5], campo[6], campo[7]), campo[8])));
    return estudantes;
  }

  private static List<String[]> carregarArquivo(String path) throws SgeException {
    var campos = new ArrayList<String[]>();
    try {
      var arquivo = Path.of(path);
      if (!Files.exists(arquivo)) {
        Files.createDirectories(arquivo.getParent());
        Files.createFile(arquivo);
      } else {
        var linhas = Files.readAllLines(arquivo);
        linhas.forEach(linha -> campos.add(linha.split(",")));
      }
    } catch (Exception e) {
      throw new SgeException(String.format("%s %s. %s%n", "Erro ao carregar dados cadastrados. Arquivo", 
        path, e.getMessage()));
    }
    return campos;
  }

  public static void salvarCurso(Curso curso) throws SgeException {
    salvarLinha(PATH_CURSOS, String.format("%s,%s,%s,%s%n", 
      curso.codigo(), curso.nome(), curso.cargaHoraria(), curso.nivel()));
  }

  public static void salvarTurma(Turma turma) throws SgeException {
    salvarLinha(PATH_TURMAS, String.format("%s,%s,%s,%s,%s,%s%n", 
      turma.codigo(), FORMATO_DATA.format(turma.dtInicio()), FORMATO_DATA.format(turma.dtFinal()), turma.periodo(), 
      turma.capacidade(), turma.curso()));
  }

  public static void salvarEstudante(Estudante estudante) throws SgeException {
    salvarLinha(PATH_ESTUDANTES, String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s%n", estudante.nome(), estudante.cpf(), 
      estudante.email(), FORMATO_DATA.format(estudante.dtNascimento()), estudante.telefone(), 
      estudante.endereco().cep(), estudante.endereco().logradouro(), estudante.endereco().numero(), estudante.turma()));
  }

  private static void salvarLinha(String path, String linha) throws SgeException {
    try {
      Files.writeString(Path.of(path), linha, StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new SgeException(String.format("%s %s. %s%n", "Erro ao salvar linha no arquivo ", path, 
        e.getMessage()));
    }
  }

}
