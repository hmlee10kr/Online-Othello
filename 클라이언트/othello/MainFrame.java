package othello;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// ȭ�� ��ȯ��, ���� �����ӿ��� �ٸ� �гε��� ��� �����ϰ� �ش� �гθ� �߰��Ͽ� ó���Ѵ�.
public class MainFrame extends JFrame
{	
	public MainFrame()
	{
		setTitle("Othello");
		setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void startUI()	{ setVisible(true); }	// ȭ�鿡 ���̱�
	// origin �гο��� current �гη� ��ȯ
	public void switchPanel(JPanel origin, JPanel current)
	{
		if (origin != null)
			origin.setVisible(false);
		current.setVisible(true);
		
		getContentPane().removeAll();
		getContentPane().add(current);
		pack();
	}
}