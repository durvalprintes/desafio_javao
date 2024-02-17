package sge;

import java.time.format.DateTimeFormatter;

public class Sistema {

  public static final String CAMINHO_ARQUIVO_CURSO = "data/cursos.csv";
  public static final String CAMINHO_ARQUIVO_TURMA = "data/turmas.csv";
  public static final String CAMINHO_ARQUIVO_ESTUDANTE = "data/estudantes.csv";

  public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  public static final String DATA_VALIDA = "\\d{2}/\\d{2}/\\d{4}";
  public static final String NUMERO_VALIDO = "\\d+";

  public static final String CAMPO_CODIGO = "codigo";
  public static final String CAMPO_CARGA_HORARIA = "cargaHoraria";
  public static final String CAMPO_CPF = "cpf";
  public static final String CAMPO_EMAIL = "email";
  public static final String CAMPO_DT_NASCIMENTO = "dtNascimento";
  public static final String CAMPO_CODIGO_TURMA = "codigoTurma";
  public static final String CAMPO_DT_INICIO = "dtInicio";
  public static final String CAMPO_DT_FINAL = "dtFinal";
  public static final String CAMPO_CODIGO_CURSO = "codigoCurso";

  private Sistema() {}

}
