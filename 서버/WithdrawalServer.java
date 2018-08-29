import java.io.*;
import java.net.*;

/*
	초기에 비밀 번호 변경 서버와 비슷한 의도로
	정수키를 확인하지 않았지만,
	
	내가 게임중에 다른 사람이 회원 탈퇴를 해버리면,
	전적 갱신을 할 수 없는 등의 문제가 있기 때문에,
	현재 로그인한 유저만 회원 탈퇴가 가능하도록 변경했다.
*/

// 회원 탈퇴 서버
// 아이디, 비밀 번호, 정수키를 받아 작업을 처리하고 결과를 전송한다.
// (정수키를 확인한다.)
public class WithdrawalServer extends ServerSocket implements Runnable
{
	private Thread thread;

	public WithdrawalServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);

		// 싱클톤 패턴의 서버 매니저에 추가
		ServerManager.getInstance().setWithdrawalServer(this);
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
				// 아이디, 정수키, 비밀 번호
				String id = Encryptor.decrypt(br.readLine());
				String key = Encryptor.decrypt(br.readLine());
				String password = Encryptor.decrypt(br.readLine());

				if (ServerManager.getInstance().getLoginServer().isValid(id, key) && ServerManager.getInstance().getMemberDAO().drop(id, password))
					// 현재 이용중인 유효한 계정이고, 아이디와 비밀 번호가 정확하면 성공
					pw.println(ServerConstants.WITHDRAWAL_SUCCESS);
				else
					// 회원 탈퇴 실패
					pw.println(ServerConstants.WITHDRAWAL_FAIL);

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
}	// WithdrawalServer Class