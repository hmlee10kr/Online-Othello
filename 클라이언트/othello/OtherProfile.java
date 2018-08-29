package othello;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import javax.swing.*;

// 다른 사람 전적 보기 화면
public class OtherProfile extends JPanel
{
   private JButton btnSearch;      // 아이디로 다른 사람 전적 검색 버튼
   private MyActionListener listener;   // 버튼 클릭 리스너
   private JButton btnBack;	// 뒤로 가기 버튼
   private JPanel otherProfilePanel;	// 메인 패널
   private JPanel imgPanel;	// 세 사람 이미지 패널
   private Font f1;	// 폰트
   private JLabel lbl0,lbl1,lbl2,lbl3,lbl4,lbl5,lbl6,lbl7,lbl8,lbl9,lbl10;	// 전적 표시에 사용될 라벨들
   
   public OtherProfile()
   {
      setPreferredSize(new Dimension(AppManager.SCREEN_WIDTH, AppManager.SCREEN_HEIGHT));
         setBackground(new Color(57,131,66));
         //setBackground(new Color(0,0,0));
         
         setLayout(null);
         
         // 메인 패널
         otherProfilePanel=new JPanel();
         otherProfilePanel.setBackground(new Color(154, 205, 50));
         otherProfilePanel.setBounds(450,100, 400, 500);
         otherProfilePanel.setLayout(null);

         // 세 사람 이미지 패널
         imgPanel = new JPanel();
         imgPanel.setLayout(null);
         imgPanel.setBounds(10, 10, 380, 170);
         imgPanel.setBackground(new Color(154,205,50));
         otherProfilePanel.add(imgPanel);
         imgPanel.setLayout(null);
         
         // 세 사람 이미지
         ImageIcon image = new ImageIcon(Main.class.getResource("../images/team.png"));
          Image pre = image.getImage();
          Image scale = pre.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);         
         
         JLabel lblimage1 = new JLabel(new ImageIcon(scale));
         lblimage1.setBounds(0, 0, 380, 200);
         imgPanel.add(lblimage1);
         
         // 라벨들의 텍스트에 사용될 폰트
         f1 = new Font("맑은 고딕", Font.BOLD, 17);

         // 아이디로 전적 검색, 뒤로 가기 버튼 액션 리스너
         listener = new MyActionListener();
         
         lbl0 = new JLabel("[       ]님의 전적");	// 아이디
         lbl0.setBounds(50,195,150,25);
         otherProfilePanel.add(lbl0);
         lbl0.setFont(f1);
         
         lbl1 = new JLabel("총 플레이 횟수 :");	// 총 플레이 횟수
         lbl1.setBounds(50,230,150,25);
         otherProfilePanel.add(lbl1);
         lbl1.setFont(f1);
         
         lbl2 = new JLabel("승리 횟수 :");	// 승리 횟수
         lbl2.setBounds(50,260,150,25);
         otherProfilePanel.add(lbl2);
         lbl2.setFont(f1);
         
         lbl3 = new JLabel("패배 횟수 :");	// 패배 횟수
         lbl3.setBounds(50,290,150,25);
         otherProfilePanel.add(lbl3);
         lbl3.setFont(f1);
         
         lbl4 = new JLabel("승률 :");	// 승률
         lbl4.setBounds(50,320,150,25);
         otherProfilePanel.add(lbl4);
         lbl4.setFont(f1);
         
         lbl5 = new JLabel("탈주 횟수 :");	// 탈주 횟수
         lbl5.setBounds(50,350,150,25);
         otherProfilePanel.add(lbl5);
         lbl5.setFont(f1);
         
         lbl6 = new JLabel("5회");	// 총 플레이 횟수
         lbl6.setBounds(210,230,50,25);
         otherProfilePanel.add(lbl6);
         lbl6.setFont(f1);
         
         lbl7 = new JLabel("5회");	// 승리 횟수
         lbl7.setBounds(210,260,150,25);
         otherProfilePanel.add(lbl7);
         lbl7.setFont(f1);
         
         lbl8 = new JLabel("5회");	// 패배 횟수
         lbl8.setBounds(210,290,150,25);
         otherProfilePanel.add(lbl8);
         lbl8.setFont(f1);
         
         lbl9 = new JLabel("20%");	// 승률
         lbl9.setBounds(210,320,150,25);
         otherProfilePanel.add(lbl9);
         lbl9.setFont(f1);
         
         lbl10 = new JLabel("3회");	// 탈주 횟수
         lbl10.setBounds(210,350,150,25);
         otherProfilePanel.add(lbl10);
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
          btnBack.addActionListener(listener);
          otherProfilePanel.add(btnBack);
          /* 뒤로 가기 */
          
          /* 아이디로 다른 사람 전적 검색 */
          ImageIcon image3 = new ImageIcon(Main.class.getResource("../images/magnifier.png"));
         Image pre3 = image3.getImage();
         Image scale3 = pre3.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
         
         btnSearch = new JButton(new ImageIcon(scale3));
         btnSearch.setBorderPainted(false);
         btnSearch.setContentAreaFilled(false);
         btnSearch.setFocusPainted(false);
          btnSearch.setBounds(20,390,100,100);
          btnSearch.addActionListener(listener);
         otherProfilePanel.add(btnSearch);
         /* 아이디로 다른 사람 전적 검색 */
         
          otherProfilePanel.setBorder(BorderFactory.createLineBorder(new Color(173, 255, 47), 10, true));
          this.add(otherProfilePanel);
      }

   // ID 에 해당하는 전적으로 초기화
   	public void init(String id)
   	{
		try
		{
			// 전적 검색 서버에 처리 요청
			Socket s = new Socket(ServerConstants.SERVER_DOMAIN, ServerConstants.SEARCH_SERV_PORT);
			BufferedReader b = new BufferedReader(new InputStreamReader(s.getInputStream(), "UTF8"));
			PrintWriter p = new PrintWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));	
			String result;

			// 인자로 받은 아이디를 전송
			p.println(Encryptor.encrypt(id));
			p.flush();
			
			result = b.readLine();
			if (result.equals(ServerConstants.SEARCH_SUCCESS))
			{	// 검색 성공
				lbl0.setText("[ " + id + " ] 님의 전적");	// 총 플레이 횟수
				lbl6.setText(b.readLine() + "회");
				lbl9.setText(String.format("%.4f", Double.parseDouble(b.readLine())) + "%");	// 승률
				lbl7.setText(b.readLine() + "회");	// 승리 횟수
				lbl8.setText(b.readLine() + "회");	// 패배 횟수
				lbl10.setText(b.readLine() + "회");	// 탈주 횟수
			}
			else if (result.equals(ServerConstants.SEARCH_FAIL))
			{	// 검색 실패
				lbl0.setText("[  ] 님의 전적");
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
            AppManager.getInstance().getMainFrame().switchPanel(OtherProfile.this, AppManager.getInstance().getSettingPanel());
         }
         else if(obj == btnSearch)
         {	// 아이디로 전적 검색
        	 
        	 // 입력한 아이디가 20자를 초과하면 재입력
        	String resultStr = null;
        	do {
	  			(new Music("select.mp3", false)).start();
        		resultStr = JOptionPane.showInputDialog(OtherProfile.this, "검색할 아이디를 입력하세요." + System.lineSeparator() + "(20자 미만)");
        		if (resultStr == null) resultStr = "";
        	} while (20 < resultStr.length());

            // 입력 상자를 닫거나, 빈칸이면 실행하지 않음
    		if (resultStr != null && !resultStr.equals("")) init(resultStr);
         }
      } // actionPerformed()
   }  // MyActionListener Inner Class
} // OtherProfile Class