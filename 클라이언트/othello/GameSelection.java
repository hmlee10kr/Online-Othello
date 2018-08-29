package othello;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// 초기 컴퓨터/온라인 게임 선택 화면
public class GameSelection extends JPanel
{	
	private JPanel game1 = new JPanel();// 좌측 컴퓨터와 플레이 모드 패널
	private JPanel game2 = new JPanel();// 우측 온라인 플레이 모드 패널

	// game menu mouseExited image
	private ImageIcon preImage1, preImage2;// 컴퓨터/온라인 마우스 올리기전 이미지를 나타낸다
	// game menu mouseEntered image
	private ImageIcon postImage1, postImage2;// 컴퓨터/온라인 마우스를 올린후 이미지를 나타낸다

	// game image label
	private JLabel img1, img2;	// game1, game2 패널에 들어갈 이미지

	public GameSelection()
	{
		setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
		setBackground(Color.black);
		setLayout(null);

		// 리소스에서 이미지 로딩
		loadImages();
	
		Mou lstnMouse = new Mou();// game1,game2패널 위에서 하는 행동들에 대응할수있도록 해주는 마우스 리스너
		
		// game1에 대한 패널 설정및 마우스 리스너 설정
		game1.setPreferredSize(new Dimension(AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT));
		game1.setBounds(0, 0, AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT);
		game1.addMouseListener(lstnMouse);
		img1 = new JLabel(preImage1);
		game1.add(img1);

		// game2에 대한 패널 설정및 마우스 리스너 설정
		game2.setPreferredSize(new Dimension(AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT));
		game2.setBounds(AppManager.PRVW_IMG_WIDTH + 10, 0, AppManager.PRVW_IMG_WIDTH, AppManager.PRVW_IMG_HEIGHT);
		game2.addMouseListener(lstnMouse);
		img2 = new JLabel(preImage2);
		game2.add(img2);
		
		add(game1);
		add(game2);
		
		// BGM 시작
		(new Music("ReversiMain.mp3", true)).start();
	}// GameSelection() 생성자
	
	// 리소스에서 이미지 로딩
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
	
	// 마우스 리스너 설정
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
			if (obj == game1) {	// 컴퓨터 모드
				img1.setIcon(preImage1);
				
				// 레벨 선택 화면으로 전환
				AppManager.getInstance().getMainFrame().switchPanel(GameSelection.this, AppManager.getInstance().getLevelPanel());
			}
			else if (obj == game2) { // 온라인 모드
				img2.setIcon(preImage2);
				
				// 로그인 화면으로 전환
				AppManager.getInstance().getMainFrame().switchPanel(GameSelection.this, AppManager.getInstance().getLoginPanel());
			}
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{	// 호버시 이펙트
			Object obj = e.getSource();
			
			if (obj == game1)
			{	// 컴퓨터 모드
				img1.setIcon(postImage1);
				img1.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			else if (obj == game2)
			{	// 온라인 모드
				img2.setIcon(postImage2);
				img2.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}

		@Override
		public void mouseExited(MouseEvent e)
		{	// 호버에서 벗어날 시 원래대로
			// stop Image
			Object obj = e.getSource();
			
			if (obj == game1)
			{	// 컴퓨터 모드
				img1.setIcon(preImage1);
				img1.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			else if (obj == game2)
			{	// 온라인 모드
				img2.setIcon(preImage2);
				img2.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}// MouseListener
} // GameSelection Class