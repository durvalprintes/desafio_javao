package sge.persistence.util;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import sge.domain.Periodo;
import sge.domain.TipoCadastro;
import sge.domain.Turma;

public class JsonUtil {

  private JsonUtil() {
  }

  public static <T extends TipoCadastro> List<T> verificaLista(List<T> lista, Class<T> tipoCadastro) {
    if (lista.isEmpty() || lista.stream().noneMatch(Turma.class::isInstance))
      return lista;
    return lista
        .stream()
        .map(cadastro -> {
          if (cadastro instanceof Turma(String codigo, LocalDate dtInicio, LocalDate dtFinal, Periodo periodo, int capacidade, String curso)) {
            return tipoCadastro.cast(new Turma(codigo, dtInicio, dtFinal, periodo, capacidade, curso));
          }
          return cadastro;
        })
        .collect(Collectors.toList());
  }

}
