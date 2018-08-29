import java.io.*;
import java.net.*;

// ȸ�� ���� ����
// ���̵�, ��� ��ȣ�� �޾� �۾��� ó���ϰ� ����� �����Ѵ�.
// (����Ű�� Ȯ������ �ʴ´�.)
public class RegisterServer extends ServerSocket implements Runnable
{
	private Thread thread;
	
	public RegisterServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);

		// ��Ŭ�� ������ ���� �Ŵ����� �߰�
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
		// accept �Լ����� ��ȯ�� ������ �̿��� ���� ����
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
				// ���̵�, �н� ����
				String id = Encryptor.decrypt(br.readLine());
				String password = Encryptor.decrypt(br.readLine());
				
				MemberDTO memberDTO = new MemberDTO();
				memberDTO.setId(id);
				memberDTO.setPassword(password);
				
				if (ServerManager.getInstance().getMemberDAO().insert(memberDTO))
					// ȸ�� ���� ����
					pw.println(ServerConstants.REGISTER_SUCCESS);
				else
					// �ߺ��� ���̵� ����
					pw.println(ServerConstants.REGISTER_EXIST);

				pw.flush();
				pw.close();	
				br.close();	
				clnt.close();
				
				interrupt();	// ������ ����
			}	// ChildThread �� run() �� try
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}	// ChildThread �� run()
	}	// ChildThread Inner Class
}	// RegisterServer Class