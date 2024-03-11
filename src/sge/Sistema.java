package sge;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import sge.persistence.Extensao;

public class Sistema {

  public static final String TITULO = "Sistema de Gestão Escolar - SGE";

  public static final Extensao EXTENSAO_ARQUIVO = Extensao.JSON;
  public static final String CAMINHO_ARQUIVO_CURSO = "data/cursos.";
  public static final String CAMINHO_ARQUIVO_TURMA = "data/turmas.";
  public static final String CAMINHO_ARQUIVO_ESTUDANTE = "data/estudantes.";

  public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  public static final String DATA_VALIDA = "\\d{2}/\\d{2}/\\d{4}";
  public static final String NUMERO_VALIDO = "\\d+";

  public static final String CAMPO_CODIGO = "codigo";
  public static final String CAMPO_NOME = "nome";
  public static final String CAMPO_CARGA_HORARIA = "cargaHoraria";
  public static final String CAMPO_NIVEL = "nivel";
  public static final String CAMPO_DT_INICIO = "dtInicio";
  public static final String CAMPO_DT_FINAL = "dtFinal";
  public static final String CAMPO_PERIODO = "periodo";
  public static final String CAMPO_CAPACIDADE = "capacidade";
  public static final String CAMPO_CURSO = "curso";
  public static final String CAMPO_CPF = "cpf";
  public static final String CAMPO_EMAIL = "email";
  public static final String CAMPO_DT_NASCIMENTO = "dtNascimento";
  public static final String CAMPO_TELEFONE = "telefone";
  public static final String CAMPO_CEP_ENDERECO = "cep";
  public static final String CAMPO_LOGRADURO_ENDERECO = "logradouro";
  public static final String CAMPO_NUMERO_ENDERECO = "numero";
  public static final String CAMPO_TURMA = "turma";
  public static final String MASCARA_DATA = "##/##/####";
  public static final String MASCARA_NUMERO = "#*****";

  public static final Map<String,String> CAMPO_MASCARA = Map.of(
    CAMPO_CPF, "###.###.###-##",
    CAMPO_DT_NASCIMENTO, MASCARA_DATA,
    CAMPO_DT_INICIO, MASCARA_DATA,
    CAMPO_DT_FINAL, MASCARA_DATA,
    CAMPO_CARGA_HORARIA, MASCARA_NUMERO,
    CAMPO_CAPACIDADE, MASCARA_NUMERO,
    CAMPO_TELEFONE, "(##) 9####-####",
    CAMPO_CEP_ENDERECO, "########"
    );

  private Sistema() {}

}
