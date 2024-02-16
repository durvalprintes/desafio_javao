package sge.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sge.exception.SgeException;

public class Arquivo {

  public static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  private Arquivo() {}

  public static List<String[]> carregarArquivo(String path) throws SgeException {
    var campos = new ArrayList<String[]>();
    try {
      var arquivo = Path.of(path);
      if (!Files.exists(arquivo)) {
        Files.createDirectories(arquivo.getParent());
        Files.createFile(arquivo);
      } else {
        var linhas = Files.readAllLines(arquivo);
        linhas.forEach(linha -> campos.add(linha.split(",")));
      }
    } catch (Exception e) {
      throw new SgeException("Erro ao carregar dados cadastrados. Arquivo " + path + e.getMessage());
    }
    return campos;
  }

  public static void salvarLinha(String path, String linha) throws SgeException {
    try {
      Files.writeString(Path.of(path), linha, StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new SgeException("Erro ao salvar linha no arquivo " + path + e.getMessage());
    }
  }

}
