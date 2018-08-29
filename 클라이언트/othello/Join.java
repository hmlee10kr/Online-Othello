package othello;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

// 회원 가입 화면
public class Join extends JPanel
{
   private JButton btnBack;      // 뒤로가기 버튼
   private MyActionListener listener;   // 뒤로가기, 회원 가입 버튼 클릭 리스너
   private JButton btnJoin;// 회원 가입 버튼
   private JPasswordField password;	// 패스워드 입력창
   private JTextField userID;	// 아이디 입력창
   private JLabel lblUserID;	// ID 라벨
   private JLabel lblUserPW;	// PW 라벨
   private JPanel joinPanel;	// 메인 패널
   private JPanel imgPanel;	// 링크 이미지 패널
   
   public Join()
   {
      setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
      setBackground(new Color(57,131,66));
      //setBackground(new Color(0,0,0));
      
      setLayout(null);
      
      // 메인 패널
      joinPanel=new JPanel();
      joinPanel.setBackground(new Color(154, 205, 50));
      joinPanel.setBounds(450,100, 400, 500);
      joinPanel.setLayout(null);
      
      // 링크 이미지 패널
      imgPanel = new JPanel();
      imgPanel.setLayout(null);
      imgPanel.setBounds(10, 10, 380, 200);
      imgPanel.setBackground(new Color(154,205,50));
      joinPanel.add(imgPanel);
      imgPanel.setLayout(null);
      
      // 링크 이미지
      ImageIcon image = new ImageIcon(Main.class.getResource("../images/link3.png"));
       Image pre = image.getImage();
       Image scale = pre.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
      
      JLabel lblimage1 = new JLabel(new ImageIcon(scale));
      lblimage1.setBounds(0, 0, 380, 200);
      imgPanel.add(lblimage1);

      listener = new MyActionListener();
      
  		// ID 라벨
      lblUserID = new JLabel("ID");
      lblUserID.setBounds(50,230,50,25);
      lblUserID.setFont(new Font("SansSerif", Font.BOLD, 30));
      joinPanel.add(lblUserID);
      
      // PW 라벨
      lblUserPW = new JLabel("PW");
       lblUserPW.setBounds(50, 300, 50, 25);
       lblUserPW.setFont(new Font("SansSerif", Font.BOLD, 30));
       joinPanel.add(lblUserPW);
       
       // 길이 20자 제한의 아이디 입력 필드
      userID = new JTextField(20);
      userID.setDocument(new JTextFieldLimit(20));
       userID.setBounds(130, 230, 160, 30);
       joinPanel.add(userID);

       // 길이 20자 제한의 패스워드 입력 필드
       password = new JPasswordField(20);
       password.setDocument(new JTextFieldLimit(20));
       password.setBounds(130,300, 160, 30);
       joinPanel.add(password);
       
       /* 회원 가입 */
      ImageIcon image2 = new ImageIcon(Main.class.getResource("../images/check.png"));
      Image pre2 = image2.getImage();
      Image scale2 = pre2.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
      
       btnJoin = new JButton(new ImageIcon(scale2));
       btnJoin.setBorderPainted(false);
       btnJoin.setContentAreaFilled(false);
       btnJoin.setFocusPainted(false);
       btnJoin.setBounds(280,390,100,100);
       btnJoin.addActionListener(listener);
       joinPanel.add(btnJoin);
       /* 회원 가입 */
       
       /* 뒤로 가기 */
       ImageIcon image3 = new ImageIcon(Main.class.getResource("../images/delete.png"));
      Image pre3 = image3.getImage();
      Image scale3 = pre3.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
      
      btnBack = new JButton(new ImageIcon(scale3));
      btnBack.setBorderPainted(false);
      btnBack.setContentAreaFilled(false);
      btnBack.setFocusPainted(false);
       btnBack.setBounds(20,390,100,100);
       btnBack.addActionListener(listener);
      joinPanel.add(btnBack);
      /* 뒤로 가기 */
      
      joinPanel.setBorder(BorderFactory.createLineBorder(new Color(173, 255, 47), 10, true));
      this.add(joinPanel);    
   }

   public class MyActionListener implements ActionListener
   {
      @Override
      public void actionPerformed(ActionEvent e)
      {
    	  Object obj = e.getSource();
    	  
    	  if (obj == btnBack)
    	  {		// 뒤로 가기
	  			(new Music("back.mp3", false)).start();
	  			userID.setText("");
	  			password.setText("");
	  			
	  			// 로그인 화면으로 전환
		        AppManager.getInstance().getMainFrame().switchPanel(Join.this, AppManager.getInstance().getLoginPanel());
    	  }
    	  else if (obj == btnJoin)
    	  {		// 회원 가입
    		  
    		  // 빈 칸이 존재할시 대화 상자 출력
    		  if (userID.getText().equals("") && password.getText().equals("")) {
  	  			(new Music("error.mp3", false)).start();
    			  JOptionPane.showMessageDialog(Join.this, "계정 정보를 입력해주세요.");
    			  return;
    		  }
    		  else if (userID.getText().equals("")) {
  	  			(new Music("error.mp3", false)).start();
    			  JOptionPane.showMessageDialog(Join.this, "아이디를 입력해주세요.");
    			  return;
    		  }
    		  else if (password.getText().equals("")) {
  	  			(new Music("error.mp3", false)).start();
    			  JOptionPane.showMessageDialog(Join.this, "패스워드를 입력해주세요.");
    			  return;
    		  }
    		  
				try
				{
					// 회원 가입 서버에 처리 요청
					Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.REGISTER_SERV_PORT);
					BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
					PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));	
					String result;
					
					// 아이디, 패스워드 전송
					p.println(Encryptor.encrypt(userID.getText()));
					p.println(Encryptor.encrypt(password.getText()));
					p.flush();
					
					result = b.readLine();
					if (result.equals(ServerConstants.REGISTER_SUCCESS))
					{	// 회원 가입 성공
	    	  			(new Music("select.mp3", false)).start();
		    			JOptionPane.showMessageDialog(Join.this, "회원가입 성공.");	
		    			
		    			// 로그인 서버에 회원 가입한 정보로 로그인 요청
						Socket sock = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.LOGIN_SERV_PORT);
						BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF8"));
						PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF8"));	
						
						// 방금 입력한 아이디, 패스워드 전송
						pw.println(Encryptor.encrypt(userID.getText()));
						pw.println(Encryptor.encrypt(password.getText()));
						pw.flush();
						String rlt = br.readLine();
					
						if (rlt.equals(ServerConstants.LOGIN_SUCCESS))
						{	// 로그인 성공
							
		    	  			// 로그인 서버가 발행한 정수키 저장
							String key = Encryptor.decrypt(br.readLine());
			    			
							AppManager.getInstance().getUserInfo().setId(userID.getText());
							AppManager.getInstance().getUserInfo().setKey(key);
							
							// 로그인 서버에서는 이 연결을 이용해 입력 대기 상태의 쓰레드를 만들고
							// 사용자가 로그 아웃/종료 하여 이 연결이 닫히면, 예외 처리를 이용해 로그 아웃으로 판별하게 된다.							
							AppManager.getInstance().getUserInfo().setSocket(sock);
							AppManager.getInstance().getUserInfo().setBr(br);
							AppManager.getInstance().getUserInfo().setPw(pw);

							// 마이 페이지 화면으로 전환
							AppManager.getInstance().getMainFrame().switchPanel(Join.this, AppManager.getInstance().getSettingPanel());
							
			    			userID.setText("");
			    			password.setText("");
						}
					}
					else if (result.equals(ServerConstants.REGISTER_EXIST))
					{	// 중복된 아이디가 존재
	    	  			(new Music("error.mp3", false)).start();
		    			JOptionPane.showMessageDialog(Join.this, "중복된 아이디가 존재합니다.");
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
      } // actionPerformed()
   }  // MyActionListener Inner Class
} // Join Class