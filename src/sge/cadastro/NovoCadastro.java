package sge.cadastro;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sge.domain.TipoCadastro;
import sge.exception.SgeException;
import sge.persistence.Arquivo;

public abstract class NovoCadastro<T extends TipoCadastro> implements Cadastro<T> {

  protected TipoCadastro cadastro;
  protected Arquivo<T> arquivo;
  protected Map<String, String> campos = new LinkedHashMap<>();

  @Override
  public Map<String, String> getCampos() {
    this.inicializarCampos(campos);
    return campos;
  }

  protected abstract void inicializarCampos(Map<String, String> campos);

  @Override
  public List<T> listar() throws SgeException {
    return arquivo.carregar();
  }

  @Override
  public void salvar() throws SgeException {
    arquivo.salvar(cadastro);
  }

}
