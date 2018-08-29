package othello;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// �ʱ� ��ǻ��/�¶��� ���� ���� ȭ��
public class GameSelection extends JPanel
{	
	private JPanel game1 = new JPanel();// ���� ��ǻ�Ϳ� �÷��� ��� �г�
	private JPanel game2 = new JPanel();// ���� �¶��� �÷��� ��� �г�

	// game menu mouseExited image
	private ImageIcon preImage1, preImage2;// ��ǻ��/�¶��� ���콺 �ø����� �̹����� ��Ÿ����
	// game menu mouseEntered image
	private ImageIcon postImage1, postImage2;// ��ǻ��/�¶��� ���콺�� �ø��� �̹����� ��Ÿ����

	// game image label
	private JLabel img1, img2;	// game1, game2 �гο� �� �̹���

	public GameSelection()
	{
		setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
		setBackground(Color.black);
		setLayout(null);

		// ���ҽ����� �̹��� �ε�
		loadImages();
	
		Mou lstnMouse = new Mou();// game1,game2�г� ������ �ϴ� �ൿ�鿡 �����Ҽ��ֵ��� ���ִ� ���콺 ������
		
		// game1�� ���� �г� ������ ���콺 ������ ����
		game1.setPreferredSize(new Dimension(AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT));
		game1.setBounds(0, 0, AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT);
		game1.addMouseListener(lstnMouse);
		img1 = new JLabel(preImage1);
		game1.add(img1);

		// game2�� ���� �г� ������ ���콺 ������ ����
		game2.setPreferredSize(new Dimension(AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT));
		game2.setBounds(AppManager.PRVW_IMG_WIDTH + 10, 0, AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT);
		game2.addMouseListener(lstnMouse);
		img2 = new JLabel(preImage2);
		game2.add(img2);
		
		add(game1);
		add(game2);
		
		// BGM ����
		(new Music("ReversiMain.mp3", true)).start();
	}// GameSelection() ������
	
	// ���ҽ����� �̹��� �ε�
	private void loadImages()
	{
		ImageIcon t = new ImageIcon(Main.class.getResource("../images/ONEPLAYERNEW.png"));
		preImage1 = new ImageIcon(t.getImage().getScaledInstance(AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT,
																java.awt.Image.SCALE_SMOOTH));

		t = new ImageIcon(Main.class.getResource("../images/TWOPLAYER.jpg"));
		preImage2 = new ImageIcon(t.getImage().getScaledInstance(AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT,
																java.awt.Image.SCALE_SMOOTH));

		t = new ImageIcon(Main.class.getResource("../images/ONEPLAYERNEW_HL.png"));
		postImage1 = new ImageIcon(t.getImage().getScaledInstance(AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT,
																java.awt.Image.SCALE_SMOOTH));

		t = new ImageIcon(Main.class.getResource("../images/TWOPLAYER_HL.jpg"));
		postImage2 = new ImageIcon(t.getImage().getScaledInstance(AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT,
																java.awt.Image.SCALE_SMOOTH));
	}// loadImages()
	
	// ���콺 ������ ����
	private class Mou implements MouseListener
	{
		@Override
		public void mousePressed(MouseEvent e) { }
		@Override
		public void mouseReleased(MouseEvent e) { }		
		
		@Override
		public void mouseClicked(MouseEvent e)
		{
			Object obj = e.getSource();
			
			(new Music("select.mp3", false)).start();
			if (obj == game1) {	// ��ǻ�� ���
				img1.setIcon(preImage1);
				
				// ���� ���� ȭ������ ��ȯ
				AppManager.getInstance().getMainFrame().switchPanel(GameSelection.this, AppManager.getInstance().getLevelPanel());
			}
			else if (obj == game2) { // �¶��� ���
				img2.setIcon(preImage2);
				
				// �α��� ȭ������ ��ȯ
				AppManager.getInstance().getMainFrame().switchPanel(GameSelection.this, AppManager.getInstance().getLoginPanel());
			}
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{	// ȣ���� ����Ʈ
			Object obj = e.getSource();
			
			if (obj == game1)
			{	// ��ǻ�� ���
				img1.setIcon(postImage1);
				img1.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			else if (obj == game2)
			{	// �¶��� ���
				img2.setIcon(postImage2);
				img2.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}

		@Override
		public void mouseExited(MouseEvent e)
		{	// ȣ������ ��� �� �������
			// stop Image
			Object obj = e.getSource();
			
			if (obj == game1)
			{	// ��ǻ�� ���
				img1.setIcon(preImage1);
				img1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			else if (obj == game2)
			{	// �¶��� ���
				img2.setIcon(preImage2);
				img2.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}// MouseListener
} // GameSelection Class