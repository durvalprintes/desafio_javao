package sge.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import sge.cadastro.Operacao;
import sge.domain.TipoCadastro;
import sge.exception.SgeException;
import sge.persistence.util.JsonUtil;

public class Json<T extends TipoCadastro> extends Arquivo<T> {

  private static final ObjectMapper LEITOR = new ObjectMapper().findAndRegisterModules();
  
  public Json(String caminhoArquivo, Class<T> tipoCadastro) {
    this.caminhoArquivo = caminhoArquivo + Extensao.JSON;
    this.tipoCadastro = tipoCadastro;
  }

  public List<T> carregar() throws SgeException {
    try {
      var arquivo = this.carregarBytes(caminhoArquivo);
      if (arquivo.length == 0) {
        var cadastros = LEITOR.createArrayNode();
        LEITOR.writeValue(new File(caminhoArquivo), cadastros);
        return new ArrayList<>();
      }
      return JsonUtil.verificaLista(LEITOR.readValue(new String(arquivo),
        LEITOR
          .getTypeFactory()
          .constructCollectionType(List.class, tipoCadastro)), tipoCadastro);
    } catch (Exception e) {
      throw new SgeException(e.getMessage());
    }
  }

  public void salvar(TipoCadastro cadastro, Operacao operacao, int indiceCadastro) throws SgeException {
    try {
      var cadastros = this.carregar();
      switch (operacao) {
        case Operacao.ADICIONAR -> cadastros.add(tipoCadastro.cast(cadastro));
        case Operacao.EDITAR -> cadastros.set(indiceCadastro, tipoCadastro.cast(cadastro));
        case Operacao.EXCLUIR -> cadastros.remove(indiceCadastro);
      }
      LEITOR
        .writerWithDefaultPrettyPrinter()
        .writeValue(new File(caminhoArquivo), cadastros);
    } catch (Exception e) {
      throw new SgeException(e.getMessage());
    }
  }

}
