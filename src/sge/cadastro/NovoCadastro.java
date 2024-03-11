package sge.cadastro;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import sge.domain.TipoCadastro;
import sge.exception.SgeException;
import sge.persistence.Arquivo;

public abstract class NovoCadastro<T extends TipoCadastro> implements Cadastro<T> {

  protected Arquivo<T> arquivo;
  protected TipoCadastro cadastro;
  protected int indiceCadastro = 0;
  protected Operacao operacao = Operacao.ADICIONAR;
  protected Map<String, String> campos = new LinkedHashMap<>();

  @Override
  public Map<String, String> getCampos() {
    this.inicializarCampos(campos);
    return campos;
  }

  protected abstract void inicializarCampos(Map<String, String> campos);

  @Override
  public void setIndice(int indiceCadastro) {
    this.indiceCadastro = indiceCadastro;
  }

  @Override
  public void setOperacao(Operacao operacao) {
    this.operacao = operacao;
  }

  @Override
  public List<T> listar() throws SgeException {
    return arquivo.carregar();
  }

  protected List<T> verificarLista() throws SgeException {
    var lista = arquivo.carregar();
    if (operacao.equals(Operacao.EDITAR)) {
      lista.remove(indiceCadastro);
    }
    return lista;
  }

  @Override
  public T procurarIndice() throws SgeException {
    return this.listar().get(indiceCadastro);
  }

  @Override
  public void salvar() throws SgeException {
    arquivo.salvar(cadastro, operacao, indiceCadastro);
  }

  public abstract Map<String, String> setCampos(TipoCadastro cadastro);

}
