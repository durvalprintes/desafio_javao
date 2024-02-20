package sge.persistence.util;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Map;
import java.util.function.Function;

import sge.Sistema;
import sge.cadastro.CadastroEstudante;
import sge.domain.Curso;
import sge.domain.Endereco;
import sge.domain.Estudante;
import sge.domain.Nivel;
import sge.domain.Periodo;
import sge.domain.TipoCadastro;
import sge.domain.Turma;
import sge.exception.SgeException;

public class CsvUtil {

  private static final String TIPO_CADASTRO_NAO_ENCONTRADO = "Tipo de cadastro nao encontrado.";

  public static final Map<Class<? extends TipoCadastro>, Function<String[], TipoCadastro>> processador =
    Map.of(
      Curso.class, CsvUtil::criarCurso,
      Turma.class, CsvUtil::criarTurma,
      Estudante.class, CsvUtil::criarEstudante
    );

  private CsvUtil() {}

  public static TipoCadastro fromCsv(Class<? extends TipoCadastro> tipoCadastro, String[] linha) {
    var cadastros = processador.get(tipoCadastro);
    return Objects.requireNonNull(cadastros, TIPO_CADASTRO_NAO_ENCONTRADO).apply(linha);
  }

  private static TipoCadastro criarCurso(String[] linha) {
    return new Curso(linha[0], linha[1], Integer.parseInt(linha[2]), Nivel.valueOf(linha[3]));
  }

  private static TipoCadastro criarTurma(String[] linha) {
    return new Turma(linha[0], LocalDate.parse(linha[1], Sistema.FORMATO_DATA),
        LocalDate.parse(linha[2], Sistema.FORMATO_DATA), Periodo.valueOf(linha[3]), Integer.parseInt(linha[4]), linha[5],
        new CadastroEstudante().calcularVagaDisponivel(linha[0], Integer.parseInt(linha[4])));
  }

  private static TipoCadastro criarEstudante(String[] linha) {
    return new Estudante(linha[0], linha[1], linha[2], LocalDate.parse(linha[3], Sistema.FORMATO_DATA), linha[4],
        new Endereco(linha[5], linha[6], linha[7]), linha[8]);
  }

  public static String toCsv(TipoCadastro cadastro) throws SgeException {
    return switch (cadastro) {
      case Curso(String codigo, String nome, int cargaHoraria, Nivel nivel) -> STR."""
        \{codigo},\{nome},\{cargaHoraria},\{nivel}
        """;
      case Turma(String codigo, LocalDate dtInicio, LocalDate dtFinal,
      Periodo periodo, int capacidade, String curso, _) -> STR."""
        \{codigo},\{Sistema.FORMATO_DATA.format(dtInicio)},\{Sistema.FORMATO_DATA.format(dtFinal)},\{periodo},\
        \{capacidade},\{curso}
        """;
      case Estudante(String nome, String cpf, String email, LocalDate dtNascimento,
      String telefone, Endereco(String cep, String logradouro, String numero), String turma) ->  STR."""
        \{nome},\{cpf},\{email},\{Sistema.FORMATO_DATA.format(dtNascimento)},\{telefone},\{cep},\{logradouro},\
        \{numero},\{turma}
        """;
      default -> throw new SgeException(TIPO_CADASTRO_NAO_ENCONTRADO);
    };
  }

}
