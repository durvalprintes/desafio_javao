package sge;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
  public static final String SEPARADOR = "-------------------------------";
  public static final Scanner leitor = new Scanner(System.in);

  public static void main(String[] args) {
    List<Turma> turmas = new ArrayList<>();
    var opcao = "0";

    imprimeEstruturaDoMenu(SEPARADOR);
    imprimeEstruturaDoMenu("SISTEMA DE GESTÃO ESCOLAR - SGE\n");
    imprimeEstruturaDoMenu("-------- MENU CADASTRO --------");
    while (!opcao.equals("5")) {
      imprimeEstruturaDoMenu(SEPARADOR);
      imprimeMenuCadastro();
      imprimeEstruturaDoMenu(SEPARADOR);
      imprimeMensagemDeLeitura("uma opcão do menu");
      opcao = leitor.nextLine();
      imprimeEstruturaDoMenu(SEPARADOR);
      switch (opcao) {
        case "1" -> cadastraTurma(turmas);
        case "2" -> cadastraEstudante(turmas);
        case "3" -> listaTurmas(turmas);
        case "4" -> listaEstudantes(turmas);
        case "5" -> imprimeMensagemDeSaida("Encerrando cadastro...");
        default -> imprimeMensagemDeSaida("Opção desconhecida. Selecione uma opção válida.");
      }
      imprimeMensagemDeSaida("");
    }
  }

  public static void imprimeMenuCadastro() {
    imprimeOpcaoMenu(1, "Cadastrar uma turma");
    imprimeOpcaoMenu(2, "Cadastrar um estudante");
    imprimeOpcaoMenu(3, "Listar turmas");
    imprimeOpcaoMenu(4, "Listar estudantes");
    imprimeOpcaoMenu(5, "Sair");
  }

  public static void cadastraTurma(List<Turma> turmas) {
    imprimeEstruturaDoMenu("---------- NOVA TURMA ---------");
    imprimeEstruturaDoMenu(SEPARADOR);
    imprimeMensagemDeLeitura("o código");
    var codigo = leitor.nextLine().toUpperCase();
    imprimeMensagemDeLeitura("o nome");
    var nome = leitor.nextLine().toUpperCase();
    imprimeMensagemDeLeitura("a capacidade");
    var capacidade = Integer.parseInt(leitor.nextLine());
    imprimeEstruturaDoMenu(SEPARADOR);
    if (capacidade > 0) {
      turmas.add(new Turma(codigo, nome, capacidade));
      imprimeMensagemDeSaida("Turma cadastrada com sucesso!");
    } else {
      imprimeMensagemDeSaida(
          "Capacidade incorreta. Quantidade deve ser maior que zero. Cadastro de turma não finalizado.");
    }
  }

  public static void cadastraEstudante(List<Turma> turmas) {
    imprimeEstruturaDoMenu("------- NOVO ESTUDANTE --------");
    imprimeEstruturaDoMenu(SEPARADOR);
    if (!turmas.isEmpty()) {
      imprimeMensagemDeLeitura("o nome");
      var nome = leitor.nextLine().toUpperCase();
      imprimeMensagemDeLeitura("a idade");
      var idade = Integer.parseInt(leitor.nextLine());
      imprimeMensagemDeLeitura("o telefone");
      var telefone = leitor.nextLine();
      imprimeMensagemDeLeitura("o responsavel");
      var responsavel = leitor.nextLine().toUpperCase();
      imprimeMensagemDeLeitura("o cep do endereco");
      var cep = leitor.nextLine();
      imprimeMensagemDeLeitura("o logradouro do endereco");
      var logradouro = leitor.nextLine().toUpperCase();
      imprimeMensagemDeLeitura("o numero do endereco");
      var numero = leitor.nextLine().toUpperCase();
      imprimeMensagemDeLeitura("o codigo da turma");
      var codigo = leitor.nextLine().toUpperCase();
      imprimeEstruturaDoMenu(SEPARADOR);
      turmas.stream()
      .filter(turma -> turma.getCodigo().equals(codigo) && turma.getCapidade() > turma.getEstudantes().size())
      .findFirst()
      .ifPresentOrElse(turma -> {
        turma.getEstudantes().add(
          new Estudante(nome, idade, telefone, responsavel, new Endereco(cep, logradouro, numero)));
        imprimeMensagemDeSaida("Estudante cadastrado com sucesso!");
      }, () -> imprimeMensagemDeSaida("Turma com codigo " + codigo + 
        " não encontrada ou com a capacidade atingida. Cadastro de estudante não finalizado."));
    } else {
      imprimeMensagemDeSaida(
        "Nenhuma turma encontrada. Cadastre primeiramente uma turma. Cadastro de estudante não finalizado.");
      }
    }

    public static void listaTurmas(List<Turma> turmas) {
      imprimeEstruturaDoMenu("----- LISTAGEM DE TURMAS ------");
      imprimeEstruturaDoMenu(SEPARADOR);
      if (!turmas.isEmpty()) {
        turmas.forEach(turma -> imprimeMensagemDeSaida(turma.imprime()));
        imprimeMensagemDeSaida("Total de " + turmas.size() + " turma(s) cadastrada(s).");
      } else {
        imprimeMensagemDeSaida("Nenhuma turma cadastrada.");
      }
    }

    public static void listaEstudantes(List<Turma> turmas) {
    imprimeEstruturaDoMenu("--- LISTAGEM DE ESTUDANTES ----");
    imprimeEstruturaDoMenu(SEPARADOR);
    var total = turmas.parallelStream()
      .flatMap(turma -> turma.getEstudantes().stream())
      .peek(estudante -> imprimeMensagemDeSaida(estudante.imprime()))
      .count();

    if (total > 0) {
      imprimeMensagemDeSaida("Total de " + total + " estudante(s) cadastrado(s).");
    } else {
      imprimeMensagemDeSaida("Nenhum estudante cadastrado.");
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

}
