package sge.gui.window;

import java.awt.*;
import java.util.Arrays;

import javax.swing.*;

import sge.Sistema;
import sge.cadastro.CadastroCurso;
import sge.cadastro.CadastroEstudante;
import sge.cadastro.CadastroTurma;
import sge.cadastro.NovoCadastro;
import sge.domain.TipoCadastro;

public class Principal extends Padrao {

	public Principal() {
		super();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(STR."\{Sistema.TITULO} v1.0");

		JLabel mensagemInicial = new JLabel(STR."Bem-vindo ao \{Sistema.TITULO}");
		mensagemInicial.setFont(new Font("Fira Code", Font.BOLD, 25));
		mensagemInicial.setAlignmentX(CENTER_ALIGNMENT);

		var menu = criarMenu();
		var painel = criarPainel(mensagemInicial);

		getContentPane().add(painel);
		setJMenuBar(menu);
		setVisible(true);
	}

	private JMenuBar criarMenu() {
		var menu = new JMenuBar();

		var menuCadastro = new JMenu("Cadastro");
		var menuCadastroDeCursos = criarItemCadastro("Cursos", () -> exibirCadastro(new CadastroCurso()));
		var menusCadastroDeTurmas = criarItemCadastro("Turmas", () -> exibirCadastro(new CadastroTurma()));
		var menuCadastroDeEstudantes = criarItemCadastro("Estudantes", () -> exibirCadastro(new CadastroEstudante()));

		menuCadastro.add(menuCadastroDeCursos);
		menuCadastro.add(menusCadastroDeTurmas);
		menuCadastro.add(menuCadastroDeEstudantes);

		var menuAjuda = new JMenu("Ajuda");
		var menuSobre = new JMenu("Sobre");

		menu.add(menuCadastro);
		menu.add(menuAjuda);
		menu.add(menuSobre);

		return menu;
	}

	private JMenuItem criarItemCadastro(String titulo, Runnable exibir) {
		var itemCadastro = new JMenuItem(titulo);
		itemCadastro.addActionListener(_ -> exibir.run());
		return itemCadastro;
	}

	private void exibirCadastro(NovoCadastro<? extends TipoCadastro> cadastro) {
		new Cadastro(STR."SGE - Cadastro de \{cadastro.getClass().getSimpleName().substring(8)}", cadastro);
	}

	private JPanel criarPainel(Component... componentes) {
		var painel = new JPanel();
		painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
		painel.add(Box.createVerticalGlue());
		Arrays.stream(componentes).forEach(painel::add);
		painel.add(Box.createVerticalGlue());
		return painel;
	}

}
