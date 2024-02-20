package sge.persistence;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import sge.domain.TipoCadastro;
import sge.exception.SgeException;

public abstract class Arquivo<T extends TipoCadastro> implements Repositorio<T> {

  protected String caminhoArquivo;
  protected Class<T> tipoCadastro;
  private static final String MENSAGEM_ERROR = "Erro ao carregar cadastrados. Arquivo";

  public List<String> carregarLinhas(String caminhoArquivo) throws SgeException {
    try {
      return Files.readAllLines(this.getArquivo(caminhoArquivo));
    } catch (Exception e) {
      throw new SgeException(STR."\{MENSAGEM_ERROR} \{caminhoArquivo}. \{e.getMessage()}");
    }
  }

  public byte[] carregarBytes(String caminhoArquivo) throws SgeException {
    try {
      return Files.readAllBytes(this.getArquivo(caminhoArquivo));
    } catch (Exception e) {
      throw new SgeException(STR."\{MENSAGEM_ERROR} \{caminhoArquivo}. \{e.getMessage()}");
    }
  }

  private Path getArquivo(String caminhoArquivo) throws Exception {
    var arquivo = Path.of(caminhoArquivo);
    if (!Files.exists(arquivo)) {
      Files.createDirectories(arquivo.getParent());
      Files.createFile(arquivo);
    }
    return arquivo;
  }

}
