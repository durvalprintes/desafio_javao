package sge.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import sge.exception.SgeException;

public class Arquivo {

  private Arquivo() {}

  public static List<String[]> carregar(String caminhoArquivo) throws SgeException {
    var campos = new ArrayList<String[]>();
    try {
      var arquivo = Path.of(caminhoArquivo);
      if (!Files.exists(arquivo)) {
        Files.createDirectories(arquivo.getParent());
        Files.createFile(arquivo);
      } else {
        var cadastros = Files.readAllLines(arquivo);
        cadastros.forEach(cadastro -> campos.add(cadastro.split(",")));
      }
    } catch (Exception e) {
      throw new SgeException(STR."Erro ao carregar cadastrados. Arquivo \{caminhoArquivo}. \{e.getMessage()}");
    }
    return campos;
  }

  public static void salvar(String caminhoArquivo, String cadastro) throws SgeException {
    try {
      Files.writeString(Path.of(caminhoArquivo), cadastro, StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new SgeException(STR."Erro ao salvar novo cadastro no arquivo \{caminhoArquivo}. \{e.getMessage()}");
    }
  }

}
