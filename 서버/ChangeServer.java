import java.io.*;
import java.net.*;

/*
	내가 이용중에 다른 사람이 비밀 번호를 변경하더라도,
	서버에서 (아이디, 정수키) 로 로그인한 유저를 관리하기 때문에
	게임 플레이, 전적 갱신 등은 문제 없이 이용 가능하다.
*/

// 비밀 번호 변경 서버
// 아이디, 현재 비밀 번호, 변경할 비밀 번호를 받아 작업을 처리하고 결과를 전송한다.
// (정수키를 확인하지 않는다.)
public class ChangeServer extends ServerSocket implements Runnable
{
	private Thread thread;

	public ChangeServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);
		
		// 싱클톤 패턴의 서버 매니저에 추가
		ServerManager.getInstance().setChangeServer(this);
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
				// 아이디, 현재 비밀 번호, 변경할 비밀 번호
				String id = Encryptor.decrypt(br.readLine());
				String password = Encryptor.decrypt(br.readLine());
				String change = Encryptor.decrypt(br.readLine());
				
				// MemberDAO 의 login 은 DB 에 해당 아이디와 패스워드를 갖는 계정이 있는지 검색하는 메서드이다.
				if (ServerManager.getInstance().getMemberDAO().login(id, password))
				{	// 해당 정보를 갖는 계정이 존재한다.
					
					// 객체에 계정 정보들을 저장한 후, 비밀 번호를 갱신
					MemberDTO memberDTO = ServerManager.getInstance().getMemberDAO().info(id);
					memberDTO.setPassword(change);
					
					// 해당 객체를 인자로 전달하여, 계정 정보 갱신
					ServerManager.getInstance().getMemberDAO().update(memberDTO);
					
					// 비밀 번호 변경에 성공했다.
					pw.println(ServerConstants.CHANGE_SUCCESS);
				}
				else
				{	// 해당 정보를 갖는 계정이 존재하지 않는다.
					pw.println(ServerConstants.CHANGE_FAIL);
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
}	// ChangeServer Class