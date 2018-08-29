import java.io.*;
import java.net.*;

// 전적 검색 서버
// 아이디를 받아 작업을 처리하고 결과를 전송한다.
// 검색 성공시, 전적 정보를 함께 전송한다.
// (정수키를 확인하지 않는다.)
public class SearchServer extends ServerSocket implements Runnable
{
	private Thread thread;
	
	public SearchServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);

		// 싱클톤 패턴의 서버 매니저에 추가
		ServerManager.getInstance().setSearchServer(this);
	}

	public void start()
	{
		thread.start();
	}
	
	@Override
	public void run()
	{
		while (true)
			try { (new ChildThread(accept())).start(); }
			catch (Exception ex) { ex.printStackTrace(); }
	}

	private class ChildThread extends Thread
	{
		// accept 함수에서 반환된 소켓을 이용한 연결 정보
		private Socket clnt;
		private BufferedReader br;
		private PrintWriter pw;

		public ChildThread(Socket clnt)
		{
			this.clnt = clnt;
			
			try
			{
				br = new BufferedReader(new InputStreamReader(clnt.getInputStream(), "UTF8"));
				pw = new PrintWriter(new OutputStreamWriter(clnt.getOutputStream(), "UTF8"));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}	// ChildThread Inner Class constructor
		
		@Override
		public void run()
		{
			try
			{
				// 아이디
				String id = Encryptor.decrypt(br.readLine());
				
				// 해당 아이디를 갖는 계정 정보 검색
				MemberDTO memberDTO = ServerManager.getInstance().getMemberDAO().info(id);
				
				if (memberDTO != null) {
					// 검색 성공
					pw.println(ServerConstants.SEARCH_SUCCESS);
					
					// 해당 전적 정보 전송
					pw.println(Integer.toString(memberDTO.getTotal()));
					pw.println(Double.toString(memberDTO.getWrate()));
					pw.println(Integer.toString(memberDTO.getWin()));
					pw.println(Integer.toString(memberDTO.getLose()));
					pw.println(Integer.toString(memberDTO.getEscape()));
				}
				else {
					// 검색 실패
					pw.println(ServerConstants.SEARCH_FAIL);
				}
				
				pw.flush();
				pw.close();	
				br.close();	
				clnt.close();
				
				interrupt();	// 쓰레드 종료
			}	// ChildThread 의 run() 의 try
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}	// ChildThread 의 run()
	}	// ChildThread Inner Class
}	// SearchServer Class