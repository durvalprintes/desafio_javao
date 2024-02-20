package sge.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import sge.domain.TipoCadastro;
import sge.exception.SgeException;
import sge.persistence.util.CsvUtil;

public class Csv<T extends TipoCadastro> extends Arquivo<T> {

  public Csv(String caminhoArquivo, Class<T> tipoCadastro) {
    this.caminhoArquivo = caminhoArquivo + Extensao.CSV;
    this.tipoCadastro = tipoCadastro;
  }

  public List<T> carregar() throws SgeException {
    try {
        return this.carregarLinhas(caminhoArquivo)
          .stream()
          .map(linha -> tipoCadastro.cast(CsvUtil.fromCsv(tipoCadastro, linha.split(","))))
          .toList();
    } catch (Exception e) {
      throw new SgeException(STR."Erro ao carregar cadastrados. Arquivo \{caminhoArquivo}. \{e.getMessage()}");
    }
  }

  public void salvar(TipoCadastro cadastro) throws SgeException {
    try {
      Files.writeString(Path.of(caminhoArquivo), CsvUtil.toCsv(cadastro), StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new SgeException(STR."Erro ao salvar novo cadastro no arquivo \{caminhoArquivo}. \{e.getMessage()}");
    }
  }

}
