package othello;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

// 내 전적 보기 화면
public class MyProfile extends JPanel
{
   private JButton btnBack;	// 뒤로 가기 버튼
   private JPanel myProfilePanel;	// 메인 패널
   private JPanel imgPanel;	// 메달 이지미 패널
   private Font f1;	// 폰트
   private JLabel lbl1,lbl2,lbl3,lbl4,lbl5,lbl6,lbl7,lbl8,lbl9,lbl10;	// 전적 표시에 사용될 라벨들
   
   public MyProfile()
   {
      setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
      setBackground(new Color(57,131,66));
      //setBackground(new Color(0,0,0));
      
      setLayout(null);
   
      // 메인 패널
      myProfilePanel=new JPanel();
      myProfilePanel.setBackground(new Color(154, 205, 50));
      myProfilePanel.setBounds(450,100, 400, 500);
      myProfilePanel.setLayout(null);
         
      // 메달 이미지 패널
      imgPanel = new JPanel();
      imgPanel.setLayout(null);
      imgPanel.setBounds(10, 10, 380, 170);
      imgPanel.setBackground(new Color(154,205,50));
      myProfilePanel.add(imgPanel);
      imgPanel.setLayout(null);
 
      // 메달 이미지
      ImageIcon image = new ImageIcon(Main.class.getResource("../images/medal.png"));
       Image pre = image.getImage();
       Image scale = pre.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
           
      JLabel lblimage1 = new JLabel(new ImageIcon(scale));
      lblimage1.setBounds(0, 0, 380, 200);
      imgPanel.add(lblimage1);

      // 라벨들의 텍스트에 사용될 폰트
      f1 = new Font("맑은 고딕", Font.BOLD, 17);
      
      // 전적 표시에 사용될 라벨들
      lbl1 = new JLabel("총 플레이 횟수 :");	// 총 플레이 횟수
      lbl1.setBounds(50,230,150,25);
      myProfilePanel.add(lbl1);
      lbl1.setFont(f1);
      
      lbl2 = new JLabel("승리 횟수 :");	// 승리 횟수
      lbl2.setBounds(50,260,150,25);
      myProfilePanel.add(lbl2);
      lbl2.setFont(f1);
      
      lbl3 = new JLabel("패배 횟수 :");	// 패배 횟수
      lbl3.setBounds(50,290,150,25);
      myProfilePanel.add(lbl3);
      lbl3.setFont(f1);
      
      lbl4 = new JLabel("승률 :");	// 승률
      lbl4.setBounds(50,320,150,25);
      myProfilePanel.add(lbl4);
      lbl4.setFont(f1);
      
      lbl5 = new JLabel("탈주 횟수 :");	// 탈주 횟수
      lbl5.setBounds(50,350,150,25);
      myProfilePanel.add(lbl5);
      lbl5.setFont(f1);
      
      lbl6 = new JLabel("5회");	// 총 플레이 횟수
      lbl6.setBounds(210,230,50,25);
      myProfilePanel.add(lbl6);
      lbl6.setFont(f1);
      
      lbl7 = new JLabel("5회");	// 승리 횟수
      lbl7.setBounds(210,260,150,25);
      myProfilePanel.add(lbl7);
      lbl7.setFont(f1);
      
      lbl8 = new JLabel("5회");	// 패배 횟수
      lbl8.setBounds(210,290,150,25);
      myProfilePanel.add(lbl8);
      lbl8.setFont(f1);
      
      lbl9 = new JLabel("20%");	// 승률
      lbl9.setBounds(210,320,150,25);
      myProfilePanel.add(lbl9);
      lbl9.setFont(f1);
      
      lbl10 = new JLabel("3회");	// 탈주 횟수
      lbl10.setBounds(210,350,150,25);
      myProfilePanel.add(lbl10);
      lbl10.setFont(f1);
  
      /* 뒤로 가기 */
      ImageIcon image2 = new ImageIcon(Main.class.getResource("../images/delete.png"));
      Image pre2 = image2.getImage();
      Image scale2 = pre2.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
      
      btnBack = new JButton(new ImageIcon(scale2));
      btnBack.setBorderPainted(false);
      btnBack.setContentAreaFilled(false);
      btnBack.setFocusPainted(false);
      btnBack.setBounds(280,390,100,100);
       btnBack.addActionListener(new MyActionListener());
       myProfilePanel.add(btnBack);
       /* 뒤로 가기 */
       
       myProfilePanel.setBorder(BorderFactory.createLineBorder(new Color(173, 255, 47), 10, true));
       this.add(myProfilePanel);
   }

   // 내 전적 초기화
   public void init()
   {
		try
		{
			// 전적 검색 서버에 처리 요청
			Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.SEARCH_SERV_PORT);
			BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
			PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));	
			String result;
			
			// 내 아이디를 전송
			p.println(Encryptor.encrypt(AppManager.getInstance().getUserInfo().getId()));
			p.flush();
			
			result = b.readLine();
			if (result.equals(ServerConstants.SEARCH_SUCCESS))
			{	// 검색 성공
				lbl6.setText(b.readLine());	// 총 플레이 횟수
				lbl9.setText(String.format("%.4f", Double.parseDouble(b.readLine())) + "%");	// 승률
				lbl7.setText(b.readLine());	// 승리 횟수
				lbl8.setText(b.readLine());	// 패배 횟수
				lbl10.setText(b.readLine());	// 탈주 횟수
			}
			else if (result.equals(ServerConstants.SEARCH_FAIL))
			{	// 검색 실패
				lbl6.setText("");
				lbl7.setText("");
				lbl8.setText("");
				lbl9.setText("");
				lbl10.setText("");
			}
			
			b.close();
			p.close();
			s.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
   }

   public class MyActionListener implements ActionListener
   {
      @Override
      public void actionPerformed(ActionEvent e)
      {
         Object obj = e.getSource();
         
         if (obj == btnBack)
         {	// 뒤로 가기
	  		(new Music("back.mp3", false)).start();
	  		
	  		// 마이 페이지 화면으로 전환
            AppManager.getInstance().getMainFrame().switchPanel(MyProfile.this, AppManager.getInstance().getSettingPanel());
         }
      } // actionPerformed()
   }  // MyActionListener Inner Class
} // MyProfile Class