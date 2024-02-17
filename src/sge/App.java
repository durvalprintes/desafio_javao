package sge;

import java.util.Scanner;

import sge.cadastro.CadastroCurso;
import sge.cadastro.CadastroEstudante;
import sge.cadastro.CadastroTurma;
import sge.cadastro.NovoCadastro;
import sge.domain.TipoCadastro;

public class App {

  private static final String SEPARADOR = "-------------------------------";
  private static final Scanner LEITOR = new Scanner(System.in);

  public static void main(String[] args) {
    imprimeEstruturaDoMenu(SEPARADOR);
    imprimeEstruturaDoMenu("SISTEMA DE GESTÃO ESCOLAR - SGE\n");
    imprimeEstruturaDoMenu("-------- MENU CADASTRO --------");
    var opcao = "0";
    var cadastroCurso = new CadastroCurso();
    var cadastroTurma = new CadastroTurma();
    var cadastroEstudante = new CadastroEstudante();
    while (!opcao.equals("7")) {
      imprimeEstruturaDoMenu(SEPARADOR);
      imprimeMenuCadastro();
      imprimeEstruturaDoMenu(SEPARADOR);
      imprimeMensagemDeLeitura("uma opção do menu");
      opcao = LEITOR.nextLine();
      imprimeEstruturaDoMenu(SEPARADOR);
      switch (opcao) {
        case "1" -> cadastrar(cadastroCurso);
        case "2" -> cadastrar(cadastroTurma);
        case "3" -> cadastrar(cadastroEstudante);
        case "4" -> listarCadastro(cadastroCurso);
        case "5" -> listarCadastro(cadastroTurma);
        case "6" -> listarCadastro(cadastroEstudante);
        case "7" -> imprimeMensagemDeSaida("Encerrando cadastro...");
        default -> imprimeMensagemDeSaida("Opção desconhecida. Selecione uma opção válida.");
      }
      imprimeMensagemDeSaida("");
    }
  }

  private static void imprimeMenuCadastro() {
    imprimeOpcaoMenu(1, "Cadastrar um curso");
    imprimeOpcaoMenu(2, "Cadastrar uma turma");
    imprimeOpcaoMenu(3, "Cadastrar um estudante");
    imprimeOpcaoMenu(4, "Listar cursos");
    imprimeOpcaoMenu(5, "Listar turmas");
    imprimeOpcaoMenu(6, "Listar estudantes");
    imprimeOpcaoMenu(7, "Sair");
  }

  private static void cadastrar(NovoCadastro<? extends TipoCadastro> cadastro) {
    imprimeEstruturaDoMenu("-------- NOVO CADASTRO --------");
    imprimeEstruturaDoMenu(SEPARADOR);
    cadastro.getCampos()
      .entrySet().forEach(campo -> {
        imprimeMensagemDeLeitura(campo.getValue());
        campo.setValue(LEITOR.nextLine().toUpperCase());
      });
    imprimeEstruturaDoMenu(SEPARADOR);
    try {
      cadastro.validar();
      cadastro.salvar();
      imprimeMensagemDeSaida("Cadastro realizado com sucesso!");
    } catch (Exception e) {
      imprimeMensagemErrorDeSaida(STR."Erro no cadastro. Entrada incorreta. \{e.getMessage()}");
    }
  }

  private static void listarCadastro(NovoCadastro<? extends TipoCadastro> cadastro) {
    imprimeEstruturaDoMenu("---------- LISTAGEM -----------");
    imprimeEstruturaDoMenu(SEPARADOR);
    try {
      var lista = cadastro.listar();
      if (lista.isEmpty()) {
        imprimeMensagemDeSaida("Nenhum cadastro encontrado.");
        return;
      }
      lista.forEach(App::imprimeMensagemDeSaida);
    } catch (Exception e) {
      imprimeMensagemErrorDeSaida(STR."Erro na listagem. \{e.getMessage()}");
    }
  }

  private static void imprimeOpcaoMenu(int opcao, String descricao) {
    imprimeEstruturaDoMenu(STR."\{opcao} - \{descricao}");
  }

  private static void imprimeEstruturaDoMenu(String estrutura) {
    System.out.println(estrutura);
  }

  private static void imprimeMensagemDeLeitura(String campo) {
    System.out.print(STR."Informe \{campo}: ");
  }

  private static void imprimeMensagemDeSaida(Object mensagem) {
    System.out.println(STR.">> \{mensagem}");
  }

  private static void imprimeMensagemErrorDeSaida(String mensagem) {
    System.err.println(STR.">> \{mensagem}");
  }

}
