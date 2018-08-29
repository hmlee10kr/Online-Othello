package othello;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

// 마이 페이지 패널
public class Setting extends JPanel
{
   private JButton btnBack;      // 뒤로가기 버튼
   private JPanel settingPanel;	// 메인 패널
   private JPanel imgPanel;	// 설정 이미지 패널
   private JButton imgButton1;	// '내 전적 보기' 버튼
   private JButton imgButton2;	// '아이디로 검색' 버튼
   private JButton imgButton3;	// '비밀번호 변경' 버튼
   private JButton imgButton4;	// '회원 탈퇴' 버튼
   private JButton imgButton5;	// '게임 시작' 버튼
   private Font f1;	// 폰트
   
   public Setting()
   {
      setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
      setBackground(new Color(57,131,66));
      //setBackground(new Color(0,0,0));
      
      setLayout(null);
      
      // 메인 패널
      settingPanel=new JPanel();
      settingPanel.setBackground(new Color(154, 205, 50));
      settingPanel.setBounds(450,100, 400, 500);
      settingPanel.setLayout(null);
      
      // 설정 이미지 패널
      imgPanel = new JPanel();
      imgPanel.setLayout(null);
      imgPanel.setBounds(10, 10, 380, 200);
      imgPanel.setBackground(new Color(154,205,50));
      settingPanel.add(imgPanel);
      imgPanel.setLayout(null);
  
      // 설정 이미지
		ImageIcon image = new ImageIcon(Main.class.getResource("../images/settings.png"));
		Image pre = image.getImage();
		Image scale = pre.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
      
      JLabel lblimage1 = new JLabel(new ImageIcon(scale));
      lblimage1.setBounds(0, 0, 380, 200);
      imgPanel.add(lblimage1);
      
      // 버튼들의 텍스트에 사용될 폰트
      f1 = new Font("맑은 고딕", Font.BOLD, 17);

      // 버튼들 설정
      imgButton1 = new JButton("내 전적 보기",new ImageIcon(Main.class.getResource("../images/button.png")));
      imgButton1.setHorizontalTextPosition(JButton.CENTER);
      imgButton1.setVerticalTextPosition(JButton.CENTER);
      imgButton1.setFont(f1);
      imgButton1.setBorderPainted(false);
      imgButton1.setBounds(50, 200, 300, 40);
      settingPanel.add(imgButton1);
      
      imgButton2 = new JButton("아이디로 검색", new ImageIcon(Main.class.getResource("../images/button.png")));
      imgButton2.setBounds(50, 260, 300, 40);
      imgButton2.setHorizontalTextPosition(JButton.CENTER);
      imgButton2.setVerticalTextPosition(JButton.CENTER);
      imgButton2.setFont(f1);
      imgButton2.setBorderPainted(false);
      settingPanel.add(imgButton2);
      
      imgButton3 = new JButton("비밀번호 변경", new ImageIcon(Main.class.getResource("../images/button.png")));
      imgButton3.setBounds(50, 320, 300, 40);
      imgButton3.setHorizontalTextPosition(JButton.CENTER);
      imgButton3.setVerticalTextPosition(JButton.CENTER);
      imgButton3.setFont(f1);
      imgButton3.setBorderPainted(false);
      settingPanel.add(imgButton3);
      
      imgButton4 = new JButton("회원 탈퇴", new ImageIcon(Main.class.getResource("../images/button.png")));
      imgButton4.setBounds(50, 380, 300, 40);
      imgButton4.setHorizontalTextPosition(JButton.CENTER);
      imgButton4.setVerticalTextPosition(JButton.CENTER);
      imgButton4.setFont(f1);
      imgButton4.setBorderPainted(false);
      settingPanel.add(imgButton4);
      
      imgButton5 = new JButton("게임 시작", new ImageIcon(Main.class.getResource("../images/button.png")));
      imgButton5.setBounds(50, 440, 300, 40);
      imgButton5.setHorizontalTextPosition(JButton.CENTER);
      imgButton5.setVerticalTextPosition(JButton.CENTER);
      imgButton5.setFont(f1);
      imgButton5.setBorderPainted(false);
      settingPanel.add(imgButton5);
      
      // 버튼들에 액션 리스너 등록
      MyActionListener listener = new MyActionListener();
      imgButton1.addActionListener(listener);
      imgButton2.addActionListener(listener);
      imgButton3.addActionListener(listener);
      imgButton4.addActionListener(listener);
      imgButton5.addActionListener(listener);
      
      /* 뒤로가기 버튼(로그아웃) */
		ImageIcon image1 = new ImageIcon(Main.class.getResource("../images/back.png"));
		Image pre1 = image1.getImage();
		Image scale1 = pre1.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
		
		btnBack = new JButton(new ImageIcon(scale1));
		// 버튼 경계 제거
		btnBack.setBorderPainted(false);
		// 버튼 내용제거
		btnBack.setContentAreaFilled(false);
		// 포커스시 경계 제거
		btnBack.setFocusPainted(false);
		
		btnBack.addActionListener(listener);
		btnBack.setBounds(1050, 630, 200, 60);
		add(btnBack);
    /* 뒤로가기 버튼(로그아웃) */
      
       settingPanel.setBorder(BorderFactory.createLineBorder(new Color(173, 255, 47), 10, true));
       this.add(settingPanel);

   }
  
   // 5개, 뒤로 가기(로그 아웃) 버튼들의 액션 리스너 클래스
   public class MyActionListener implements ActionListener
   {
      @Override
      public void actionPerformed(ActionEvent e)
      {
         Object obj = e.getSource();
         
         if (obj == imgButton1)
         {	// 내 전적 보기 버튼
        	(new Music("select.mp3", false)).start();
        	
        	// 내 전적 보기 화면 초기화 후 전환
	  		AppManager.getInstance().getMyProfilePanel().init();
	  		AppManager.getInstance().getMainFrame().switchPanel(Setting.this, AppManager.getInstance().getMyProfilePanel());
         }
         else if (obj == imgButton2)
         {	// 다른 사람 전적 검색 버튼
        	 
        	 // 입력한 아이디가 20자를 초과할 경우 재입력
            String resultStr = null;
         	do {
	  			(new Music("select.mp3", false)).start();
        		resultStr = JOptionPane.showInputDialog(Setting.this, "검색할 아이디를 입력하세요." + System.lineSeparator() + "(20자 미만)");
        		if (resultStr == null) resultStr = "";
        	} while (20 < resultStr.length()); 

         	// 입력 상자를 닫거나, 빈칸이면 실행하지 않음
            if (resultStr != null && !resultStr.equals("")) {
	  			(new Music("select.mp3", false)).start();
	  			// 다른 사람 전적 보기 화면 초기화 후 전환
            	AppManager.getInstance().getOtherProfilePanel().init(resultStr);
            	AppManager.getInstance().getMainFrame().switchPanel(Setting.this, AppManager.getInstance().getOtherProfilePanel());
            }
         }
         else if (obj == imgButton3)
         { 	// 비밀 번호 변경 버튼
        	 
        	 // 입력한 현재 비밀번호가 20자를 초과할 경우 재입력
            String resultStr1 = null;
         	do {
	  			(new Music("select.mp3", false)).start();
        		resultStr1 = JOptionPane.showInputDialog(Setting.this, "현재 비밀번호를 입력하세요." + System.lineSeparator() + "(20자 미만)");
        		if (resultStr1 == null) resultStr1 = "";
        	} while (20 < resultStr1.length());
            
         	// 입력 상자를 닫거나, 빈칸이면 실행하지 않음
            if (resultStr1 == null || resultStr1.equals(""))
            	return;
            
            // 입력한 변경할 비밀번호가 20자를 초과할 경우 재입력
            String resultStr2 = null;
         	do {
	  			(new Music("select.mp3", false)).start();
        		resultStr2 = JOptionPane.showInputDialog(Setting.this, "변경할 비밀번호를 입력하세요." + System.lineSeparator() + "(20자 미만)");
        		if (resultStr2 == null) resultStr2 = "";
        	} while (20 < resultStr2.length());
            
            if (resultStr2 != null && !resultStr2.equals(""))
            {	// 입력 상자를 닫거나, 빈칸이면 실행하지 않음
				try
				{
					// 비밀 번호 변경 서버에 처리 요청
					Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.CHANGE_SERV_PORT);
					BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
					PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));	
					
					// 아이디, 현재 비밀번호, 변경할 비밀번호를 전송
					p.println(Encryptor.encrypt(AppManager.getInstance().getUserInfo().getId()));
					p.println(Encryptor.encrypt(resultStr1));
					p.println(Encryptor.encrypt(resultStr2));
					p.flush();
					String result = b.readLine();
				
					if (result.equals(ServerConstants.CHANGE_SUCCESS)) {
						// 비밀번호 변경 성공
			  			(new Music("select.mp3", false)).start();
						JOptionPane.showMessageDialog(Setting.this, "완료되었습니다.");
					}
					else if (result.equals(ServerConstants.CHANGE_FAIL)) {
						// 비밀번호 변경 실패
			  			(new Music("error.mp3", false)).start();
						JOptionPane.showMessageDialog(Setting.this, "계정 정보가 존재하지 않습니다.");
					}
					
					b.close();
					p.close();
					s.close();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
            }
         }
         else if (obj == imgButton4)
         {	// 회원 탈퇴 버튼
            int result = 0;
  			(new Music("select.mp3", false)).start();
            result = JOptionPane.showConfirmDialog(Setting.this, "회원 탈퇴를 하시겠습니까?");
            
            if (result != JOptionPane.YES_OPTION)
            	return;
       
            // 입력한 비밀번호가 20자를 초과할 경우 재입력
            String resultStr = null;
         	do {
	  			(new Music("select.mp3", false)).start();
        		resultStr = JOptionPane.showInputDialog(Setting.this, "비밀번호를 입력하세요." + System.lineSeparator() + "(20자 미만)");
        		if (resultStr == null) resultStr = "";
        	} while (20 < resultStr.length());
            
            if (resultStr != null && !resultStr.equals(""))
            {	// 입력 상자를 닫거나, 빈칸이면 실행하지 않음
				try
				{
					// 회원 탈퇴 서버에 처리 요청
					Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.WITHDRAWAL_SERV_PORT);
					BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
					PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));	
					
					// ID, 정수키, 입력한 비밀번호 전송
					p.println(Encryptor.encrypt(AppManager.getInstance().getUserInfo().getId()));
					p.println(Encryptor.encrypt(AppManager.getInstance().getUserInfo().getKey()));
					p.println(Encryptor.encrypt(resultStr));
					p.flush();
					String rlt = b.readLine();
				
					if (rlt.equals(ServerConstants.WITHDRAWAL_SUCCESS))
					{	// 탈퇴 성공
			  			(new Music("select.mp3", false)).start();
						JOptionPane.showMessageDialog(Setting.this, "회원 탈퇴가 완료되었습니다.");
						
						// 로그 아웃
						AppManager.getInstance().getUserInfo().getBr().close();
						AppManager.getInstance().getUserInfo().getPw().close();
						AppManager.getInstance().getUserInfo().getSocket().close();
						
						// 로그인 화면으로 전환
						AppManager.getInstance().getMainFrame().switchPanel(Setting.this, AppManager.getInstance().getLoginPanel());
					}
					else if (rlt.equals(ServerConstants.WITHDRAWAL_FAIL))
					{	// 탈퇴 실패
			  			(new Music("error.mp3", false)).start();
						JOptionPane.showMessageDialog(Setting.this, "계정 정보가 존재하지 않습니다.");
					}
					
					b.close();
					p.close();
					s.close();
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}       	
            }
         }
         else if (obj == imgButton5)
         {	// 게임 시작 버튼
	  		(new Music("select.mp3", false)).start();
	  		// 게임 시작후 게임 화면으로 전환
        	 AppManager.getInstance().getPlayOnlinePanel().start();
            AppManager.getInstance().getMainFrame().switchPanel(Setting.this, AppManager.getInstance().getPlayOnlinePanel());
         }
         else if (obj == btnBack)
         {	// 뒤로 가기(로그 아웃)
	  		(new Music("select.mp3", false)).start();
        	 if (JOptionPane.showConfirmDialog(Setting.this, "로그 아웃 하시겠습니까?") == JOptionPane.YES_OPTION)
        	 {
 	  			(new Music("back.mp3", false)).start();
 	  			
        		 try
        		 {	// 로그인시의 연결을 통해 생성된 로그인 서버의 쓰레드와의 연결 종료
        			 // 입력 대기 상태의 해당 쓰레드에서 예외 발생 -> 로그 아웃/종료 했음을 판별 
	        		 AppManager.getInstance().getUserInfo().getBr().close();
	        		 AppManager.getInstance().getUserInfo().getPw().close();
	        		 AppManager.getInstance().getUserInfo().getSocket().close();
        		 }
        		 catch (Exception ex)
        		 {
        			 ex.printStackTrace();
        		 }
        		 
        		 // 로그인 페이지로 전환
                 AppManager.getInstance().getMainFrame().switchPanel(Setting.this, AppManager.getInstance().getLoginPanel());
        	 }
         }
      } // actionPerformed()
   }  // MyActionListener Inner Class
} // Setting Class