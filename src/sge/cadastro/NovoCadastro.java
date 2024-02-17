package sge.cadastro;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sge.domain.TipoCadastro;
import sge.exception.SgeException;
import sge.persistence.Arquivo;

public abstract class NovoCadastro<T extends TipoCadastro> implements Cadastro<T> {

  protected String arquivo;
  protected TipoCadastro cadastro;
  protected Map<String, String> campos = new LinkedHashMap<>();

  @Override
  public Map<String, String> getCampos() {
    this.inicializarCampos(campos);
    return campos;
  }

  protected abstract void inicializarCampos(Map<String, String> campos);

  @Override
  public List<T> listar() throws SgeException {
    return Arquivo.carregar(arquivo)
      .stream()
      .map(this::criarTipoCadastro)
      .toList();
  }

  protected abstract T criarTipoCadastro(String[] campo);

  @Override
  public void salvar() throws SgeException {
    Arquivo.salvar(arquivo, cadastro.salvar());
  }

}
