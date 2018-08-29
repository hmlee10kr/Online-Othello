import java.io.*;
import java.net.*;

// 회원 가입 서버
// 아이디, 비밀 번호를 받아 작업을 처리하고 결과를 전송한다.
// (정수키를 확인하지 않는다.)
public class RegisterServer extends ServerSocket implements Runnable
{
	private Thread thread;
	
	public RegisterServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);

		// 싱클톤 패턴의 서버 매니저에 추가
		ServerManager.getInstance().setRegisterServer(this);
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
				// 아이디, 패스 워드
				String id = Encryptor.decrypt(br.readLine());
				String password = Encryptor.decrypt(br.readLine());
				
				MemberDTO memberDTO = new MemberDTO();
				memberDTO.setId(id);
				memberDTO.setPassword(password);
				
				if (ServerManager.getInstance().getMemberDAO().insert(memberDTO))
					// 회원 가입 성공
					pw.println(ServerConstants.REGISTER_SUCCESS);
				else
					// 중복된 아이디가 존재
					pw.println(ServerConstants.REGISTER_EXIST);

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
}	// RegisterServer Class