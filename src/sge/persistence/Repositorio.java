package sge.persistence;

import java.util.List;

import sge.cadastro.Operacao;
import sge.domain.TipoCadastro;
import sge.exception.SgeException;

public interface Repositorio<T extends TipoCadastro> {
  
  List<T> carregar() throws SgeException;

  void salvar(TipoCadastro cadastro, Operacao operacao, int indiceCadastro) throws SgeException;

}
