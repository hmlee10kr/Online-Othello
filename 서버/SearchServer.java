import java.io.*;
import java.net.*;

// ���� �˻� ����
// ���̵� �޾� �۾��� ó���ϰ� ����� �����Ѵ�.
// �˻� ������, ���� ������ �Բ� �����Ѵ�.
// (����Ű�� Ȯ������ �ʴ´�.)
public class SearchServer extends ServerSocket implements Runnable
{
	private Thread thread;
	
	public SearchServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);

		// ��Ŭ�� ������ ���� �Ŵ����� �߰�
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
				// ���̵�
				String id = Encryptor.decrypt(br.readLine());
				
				// �ش� ���̵� ���� ���� ���� �˻�
				MemberDTO memberDTO = ServerManager.getInstance().getMemberDAO().info(id);
				
				if (memberDTO != null) {
					// �˻� ����
					pw.println(ServerConstants.SEARCH_SUCCESS);
					
					// �ش� ���� ���� ����
					pw.println(Integer.toString(memberDTO.getTotal()));
					pw.println(Double.toString(memberDTO.getWrate()));
					pw.println(Integer.toString(memberDTO.getWin()));
					pw.println(Integer.toString(memberDTO.getLose()));
					pw.println(Integer.toString(memberDTO.getEscape()));
				}
				else {
					// �˻� ����
					pw.println(ServerConstants.SEARCH_FAIL);
				}
				
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
}	// SearchServer Class