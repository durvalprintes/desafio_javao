package sge.cadastro;

import java.util.List;
import java.util.Map;

import sge.exception.SgeException;

public interface Cadastro<T> {

  Map<String, String> getCampos();

  List<T> listar() throws SgeException;

  void validar() throws SgeException;

  void salvar() throws SgeException;

}
