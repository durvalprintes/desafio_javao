package sge.cadastro;

import java.util.List;
import java.util.Map;

import sge.exception.SgeException;

public interface Cadastro<T> {

  Map<String, String> getCampos();
  
  void setIndice(int indiceCadastro);
  
  void setOperacao(Operacao operacao);

  List<T> listar() throws SgeException;

  T procurarIndice() throws SgeException;

  void validar() throws SgeException;
  
  void salvar() throws SgeException;

}
