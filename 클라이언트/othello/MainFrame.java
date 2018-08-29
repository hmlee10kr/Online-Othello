package othello;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// 화면 전환시, 메인 프레임에서 다른 패널들을 모두 삭제하고 해당 패널만 추가하여 처리한다.
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

	public void startUI()	{ setVisible(true); }	// 화면에 보이기
	// origin 패널에서 current 패널로 전환
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