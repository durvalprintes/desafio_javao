package sge.persistence.util;

import sge.Sistema;
import sge.domain.TipoCadastro;
import sge.persistence.Arquivo;
import sge.persistence.Csv;
import sge.persistence.Json;

public class ArquivoUtil {

  private ArquivoUtil() {}

  public static <T extends TipoCadastro> Arquivo<T> getTipoArquivo(String caminhoArquivo, Class<T> tipoCadastro) {
    return switch (Sistema.EXTENSAO_ARQUIVO) {
      case CSV -> new Csv<>(caminhoArquivo, tipoCadastro);
      case JSON -> new Json<>(caminhoArquivo, tipoCadastro);
    };
  }

}
