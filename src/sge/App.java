package sge;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import sge.domain.Curso;
import sge.domain.Endereco;
import sge.domain.Estudante;
import sge.domain.Item;
import sge.domain.Nivel;
import sge.domain.Periodo;
import sge.domain.Turma;
import sge.exception.SgeException;
import sge.persistence.Arquivo;

public class App {
  private static final String SEPARADOR = "-------------------------------";
  private static final Scanner LEITOR = new Scanner(System.in);

  private static List<Curso> cursos;
  private static List<Turma> turmas;
  private static List<Estudante> estudantes;

  public static void main(String[] args) {
    try {
      cursos = Arquivo.carregarCursosCadastrados();
      turmas = Arquivo.carregarTurmasCadastradas();
      estudantes = Arquivo.carregarEstudantesCadastrados();
    } catch (Exception e) {
      imprimeMensagemErrorDeSaida(e.getMessage());
    }

    var opcao = "0";
    imprimeEstruturaDoMenu(SEPARADOR);
    imprimeEstruturaDoMenu("SISTEMA DE GESTÃO ESCOLAR - SGE\n");
    imprimeEstruturaDoMenu("-------- MENU CADASTRO --------");
    while (!opcao.equals("7")) {
      imprimeEstruturaDoMenu(SEPARADOR);
      imprimeMenuCadastro();
      imprimeEstruturaDoMenu(SEPARADOR);
      imprimeMensagemDeLeitura("uma opção do menu");
      opcao = LEITOR.nextLine();
      imprimeEstruturaDoMenu(SEPARADOR);
      switch (opcao) {
        case "1" -> cadastrarCurso();
        case "2" -> cadastrarTurma();
        case "3" -> cadastrarEstudante();
        case "4" -> listarItens(cursos);
        case "5" -> listarItens(turmas);
        case "6" -> listarItens(estudantes);
        case "7" -> imprimeMensagemDeSaida("Encerrando cadastro...");
        default -> imprimeMensagemDeSaida("Opção desconhecida. Selecione uma opção válida.");
      }
      imprimeMensagemDeSaida("");
    }
  }

  public static void imprimeMenuCadastro() {
    imprimeOpcaoMenu(1, "Cadastrar um curso");
    imprimeOpcaoMenu(2, "Cadastrar uma turma");
    imprimeOpcaoMenu(3, "Cadastrar um estudante");
    imprimeOpcaoMenu(4, "Listar cursos");
    imprimeOpcaoMenu(5, "Listar turmas");
    imprimeOpcaoMenu(6, "Listar estudantes");
    imprimeOpcaoMenu(7, "Sair");
  }

  public static void cadastrarCurso() {
    imprimeEstruturaDoMenu("---------- NOVO CURSO ---------");
    imprimeEstruturaDoMenu(SEPARADOR);
    imprimeMensagemDeLeitura("o código");
    var codigo = LEITOR.nextLine().toUpperCase();
    imprimeMensagemDeLeitura("o nome");
    var nome = LEITOR.nextLine().toUpperCase();
    imprimeMensagemDeLeitura("a carga horária (horas)");
    var cargaHoraria = LEITOR.nextLine();
    imprimeMensagemDeLeitura("o nível (BASICO, INTERMEDIARIO, AVANCADO)");
    var nivel = LEITOR.nextLine().toUpperCase();
    imprimeEstruturaDoMenu(SEPARADOR);
    try {
      validarNovoCurso(codigo);
      var curso = new Curso(codigo, nome, Integer.parseInt(cargaHoraria), Nivel.valueOf(nivel));
      Arquivo.salvarCurso(curso);
      cursos.add(curso);
      imprimeMensagemDeSaida("Curso cadastrado com sucesso!");
    } catch (Exception e) {
      imprimeMensagemErrorDeSaida("Erro no cadastro do novo curso. Entrada incorreta. " + e.getMessage());
    }
  }

  private static void validarNovoCurso(String codigo) throws SgeException {
    if (cursos.stream().anyMatch(curso -> curso.codigo().equals(codigo))) {
      throw	new SgeException("Código de curso já cadastrado.");
    }
  }

  public static void cadastrarTurma() {
    imprimeEstruturaDoMenu("---------- NOVA TURMA ---------");
    imprimeEstruturaDoMenu(SEPARADOR);
    imprimeMensagemDeLeitura("o código");
    var codigo = LEITOR.nextLine().toUpperCase();
    imprimeMensagemDeLeitura("a data de início");
    var dtInicio = LEITOR.nextLine();
    imprimeMensagemDeLeitura("o data de término");
    var dtFinal = LEITOR.nextLine();
    imprimeMensagemDeLeitura("o período (MATUTINO, VESPERTINO, NOTURNO, SABADO)");
    var periodo = LEITOR.nextLine().toUpperCase();
    imprimeMensagemDeLeitura("a capacidade");
    var capacidade = LEITOR.nextLine();
    imprimeMensagemDeLeitura("o código do curso");
    var codigoCurso = LEITOR.nextLine().toUpperCase();
    imprimeEstruturaDoMenu(SEPARADOR);
    try {
      validarNovaTurma(codigo, codigoCurso);
      var turma = new Turma(codigo, LocalDate.parse(dtInicio, Arquivo.FORMATO_DATA), LocalDate.parse(dtFinal, 
        Arquivo.FORMATO_DATA), Periodo.valueOf(periodo), Integer.parseInt(capacidade), codigoCurso);
      Arquivo.salvarTurma(turma);
      turmas.add(turma);
      imprimeMensagemDeSaida("Turma cadastrada com sucesso!");
    } catch (Exception e) {
      imprimeMensagemErrorDeSaida("Erro no cadastro da nova turma. Entrada incorreta. " + e.getMessage());
    }
  }

  private static void validarNovaTurma(String codigo, String codigoCurso) throws SgeException {
    if (turmas.stream().anyMatch(turma -> turma.codigo().equals(codigo))) {
      throw	new SgeException("Código de turma já cadastrado.");
    }
    if (cursos.stream().noneMatch(curso -> curso.codigo().equals(codigoCurso))) {
      throw new SgeException("Curso não encontrado com o código " + codigoCurso);
    }
  }

  public static void cadastrarEstudante() {
    imprimeEstruturaDoMenu("------- NOVO ESTUDANTE --------");
    imprimeEstruturaDoMenu(SEPARADOR);
    imprimeMensagemDeLeitura("o nome");
    var nome = LEITOR.nextLine().toUpperCase();
    imprimeMensagemDeLeitura("o cpf");
    var cpf = LEITOR.nextLine();
    imprimeMensagemDeLeitura("o email");
    var email = LEITOR.nextLine().toUpperCase();
    imprimeMensagemDeLeitura("a data de nascimento");
    var dtNascimento = LEITOR.nextLine();
    imprimeMensagemDeLeitura("o telefone");
    var telefone = LEITOR.nextLine();
    imprimeMensagemDeLeitura("o cep do endereço");
    var cep = LEITOR.nextLine();
    imprimeMensagemDeLeitura("o logradouro do endereço");
    var logradouro = LEITOR.nextLine().toUpperCase();
    imprimeMensagemDeLeitura("o numero do endereço");
    var numero = LEITOR.nextLine().toUpperCase();
    imprimeMensagemDeLeitura("o código da turma");
    var codigoTurma = LEITOR.nextLine().toUpperCase();
    imprimeEstruturaDoMenu(SEPARADOR);
    try {
      validarNovoEstudante(cpf, email, codigoTurma);
      var estudante = new Estudante(nome, cpf, email, LocalDate.parse(dtNascimento, Arquivo.FORMATO_DATA), telefone, 
        new Endereco(cep, logradouro, numero), codigoTurma);
      Arquivo.salvarEstudante(estudante);
      estudantes.add(estudante);
      imprimeMensagemDeSaida("Estudante cadastrado com sucesso!");
    } catch (Exception e) {
      imprimeMensagemErrorDeSaida("Erro no cadastro do novo estudante. Entrada incorreta. " + e.getMessage());
    }
  }

  private static void validarNovoEstudante(String cpf, String email, String codigoTurma) throws SgeException {
    if (estudantes.stream()
      .anyMatch(estudante -> estudante.cpf().equals(cpf) || estudante.email().equals(email))) {
      throw new SgeException("CPF e/ou EMAIl já cadastrado.");
    }
    if (turmas.stream()
      .noneMatch(turma -> turma.codigo().equals(codigoTurma) && turma.calcularCapacidadeAtual(estudantes) > 0)) {
      throw new SgeException("Turma não encontrada com o código " + codigoTurma + " ou sem vagas disponíveis.");
    }
  }

  public static void listarItens(List<? extends Item> lista) {
    imprimeEstruturaDoMenu("---------- LISTAGEM -----------");
    imprimeEstruturaDoMenu(SEPARADOR);
    if (!lista.isEmpty()) {
      lista.forEach(item -> {
        switch (item) {
          case Turma turma ->          
            imprimeMensagemDeSaida(String.format("%s%n%s: %d", turma.imprimir(), 
              "Capacidade atual", turma.calcularCapacidadeAtual(estudantes)));
          default -> imprimeMensagemDeSaida(item.imprimir());
      }}); 
    } else {
      imprimeMensagemDeSaida("Nenhum item cadastrado.");
    }
  }

  public static void imprimeOpcaoMenu(int opcao, String descricao) {
    imprimeEstruturaDoMenu(String.format("%d - %s", opcao, descricao));
  }

  public static void imprimeEstruturaDoMenu(String estrutura) {
    System.out.println(estrutura);
  }

  public static void imprimeMensagemDeLeitura(String campo) {
    System.out.printf("Informe %s: ", campo);
  }

  public static void imprimeMensagemDeSaida(String mensagem) {
    System.out.printf(">> %s%n", mensagem);
  }

  public static void imprimeMensagemErrorDeSaida(String mensagem) {
    System.err.printf(">> %s%n", mensagem);
  }

}
