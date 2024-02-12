package sge;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import sge.cadastro.CadastroCurso;
import sge.cadastro.CadastroEstudante;
import sge.cadastro.CadastroTurma;
import sge.cadastro.Cadastro;
import sge.exception.SgeException;

public class App {

  private static final String SEPARADOR = "-------------------------------";
  private static final Scanner LEITOR = new Scanner(System.in);

  public static void main(String[] args) {
    try {
      var opcao = "0";
      var cadastroCurso = new CadastroCurso();
      var cadastroTurma = new CadastroTurma();
      var cadastroEstudante = new CadastroEstudante();
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
    } catch (Exception e) {
      imprimeMensagemErrorDeSaida(e.getMessage());
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

  private static void cadastrar(Cadastro cadastro) {
    imprimeEstruturaDoMenu("-------- NOVO CADASTRO --------");
    imprimeEstruturaDoMenu(SEPARADOR);
    var campos = new HashMap<String, String>();
    switch (cadastro) {
      case CadastroCurso _ -> cadastrarCurso(campos);
      case CadastroTurma _ -> cadastrarTurma(campos);
      case CadastroEstudante _ -> cadastrarEstudante(campos);
      default -> throw new SgeException("Cadastro não identificado.");
    }
    imprimeEstruturaDoMenu(SEPARADOR);
    try {
      cadastro.validar(campos);
      cadastro.salvar();
      imprimeMensagemDeSaida("Cadastro realizado com sucesso!");
    } catch (Exception e) {
      imprimeMensagemErrorDeSaida(STR."Erro no cadastro. Entrada incorreta. \{e.getMessage()}");
    }
  }

  private static void cadastrarCurso(Map<String, String> campos) {
    imprimeMensagemDeLeitura("o código");
    campos.put("codigo", LEITOR.nextLine().toUpperCase());
    imprimeMensagemDeLeitura("o nome");
    campos.put("nome", LEITOR.nextLine().toUpperCase());
    imprimeMensagemDeLeitura("a carga horária (horas)");
    campos.put("cargaHoraria", LEITOR.nextLine());
    imprimeMensagemDeLeitura("o nível (BASICO, INTERMEDIARIO, AVANCADO)");
    campos.put("nivel", LEITOR.nextLine().toUpperCase());
  }
  
  private static void cadastrarTurma(Map<String, String> campos) {
    imprimeMensagemDeLeitura("o código");
    campos.put("codigo", LEITOR.nextLine().toUpperCase());
    imprimeMensagemDeLeitura("a data de início (dd/mm/yyyy)");
    campos.put("dtInicio", LEITOR.nextLine());
    imprimeMensagemDeLeitura("o data de término (dd/mm/yyyy)");
    campos.put("dtFinal", LEITOR.nextLine());
    imprimeMensagemDeLeitura("o período (MATUTINO, VESPERTINO, NOTURNO, SABADO)");
    campos.put("periodo", LEITOR.nextLine().toUpperCase());
    imprimeMensagemDeLeitura("a capacidade");
    campos.put("capacidade", LEITOR.nextLine());
    imprimeMensagemDeLeitura("o código do curso");
    campos.put("codigoCurso", LEITOR.nextLine().toUpperCase());
  }

  private static void cadastrarEstudante(Map<String, String> campos) {
    imprimeMensagemDeLeitura("o nome");
    campos.put("nome", LEITOR.nextLine().toUpperCase());
    imprimeMensagemDeLeitura("o cpf");
    campos.put("cpf", LEITOR.nextLine());
    imprimeMensagemDeLeitura("o email");
    campos.put("email", LEITOR.nextLine().toUpperCase());
    imprimeMensagemDeLeitura("a data de nascimento (dd/mm/yyyy)");
    campos.put("dtNascimento", LEITOR.nextLine());
    imprimeMensagemDeLeitura("o telefone");
    campos.put("telefone", LEITOR.nextLine());
    imprimeMensagemDeLeitura("o cep do endereço");
    campos.put("cep", LEITOR.nextLine());
    imprimeMensagemDeLeitura("o logradouro do endereço");
    campos.put("logradouro", LEITOR.nextLine().toUpperCase());
    imprimeMensagemDeLeitura("o numero do endereço");
    campos.put("numero", LEITOR.nextLine().toUpperCase());
    imprimeMensagemDeLeitura("o código da turma");
    campos.put("codigoTurma", LEITOR.nextLine().toUpperCase());
  }

  private static void listarCadastro(Cadastro cadastro) {
    var lista = cadastro.carregarLista();
    imprimeEstruturaDoMenu("---------- LISTAGEM -----------");
    imprimeEstruturaDoMenu(SEPARADOR);
    if (!lista.isEmpty()) {
      lista.forEach(App::imprimeMensagemDeSaida);
    } else {
      imprimeMensagemDeSaida("Nenhum cadastro encontrado.");
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
