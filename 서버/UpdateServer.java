import java.io.*;
import java.net.*;

// ���� ���� ����
// ������ ���ڰ� ������(�Ѵ� Ż���� ���� ��Ī ������ ������)
// ���� ���̵�, ���� ����Ű, ���� ���̵�, ���� ����� �޾� �۾��� ó���ϰ� ����� �����Ѵ�.
// (������ ����Ű�� Ȯ���Ѵ�.)
// (�Ѵ� Ż���Ͽ� ��Ī ������ ��û�� ���� ServerConstants.UPDATE_MATCH_SERVER_REQUEST ��� Ű�� ���۵ȴ�.)
public class UpdateServer extends ServerSocket implements Runnable
{
	private Thread thread;
	
	public UpdateServer(int port) throws Exception
	{
		super(port);
		thread = new Thread(this);

		// ��Ŭ�� ������ ���� �Ŵ����� �߰�
		ServerManager.getInstance().setUpdateServer(this);
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
				// ���� ���̵�, ���� ����Ű, ���� ���̵�, ���� ���
				String id = Encryptor.decrypt(br.readLine());
				String key = Encryptor.decrypt(br.readLine());
				String loser = Encryptor.decrypt(br.readLine());
				String updateConstant = br.readLine();
				
				// ���ڿ� ������ ���� ������ ���� ��ü
				MemberDTO memberDTO = ServerManager.getInstance().getMemberDAO().info(id);
				MemberDTO memberDTOLoser = ServerManager.getInstance().getMemberDAO().info(loser);
				String result = "";
				
				if (memberDTO != null && memberDTOLoser != null)
				{
					if (ServerManager.getInstance().getLoginServer().isValid(id, key) ||
							key.equals(ServerConstants.UPDATE_MATCH_SERVER_REQUEST))
					{	// ��ȿ�� ��û�̰ų� ��Ī ������ ��û�� ��� �� ������ ���� ����
						memberDTO.setTotal(memberDTO.getTotal() + 1);
						memberDTOLoser.setTotal(memberDTOLoser.getTotal() + 1);

						// ����� ��� �� �÷��� Ƚ���� �����ϰ� �ȴ�.
						if (!updateConstant.equals(ServerConstants.MATCH_DRAW))
						{
							memberDTO.setWin(memberDTO.getWin() + 1);
							
							switch (updateConstant)
							{
							case ServerConstants.MATCH_WIN : memberDTOLoser.setLose(memberDTOLoser.getLose() + 1); break;
							case ServerConstants.MATCH_ESCAPE : memberDTOLoser.setEscape(memberDTOLoser.getEscape() + 1); break;
							}
						}
						
						memberDTO.setWrate((double)memberDTO.getWin() / (double)memberDTO.getTotal());
						memberDTOLoser.setWrate((double)memberDTOLoser.getWin() / (double)memberDTOLoser.getTotal());
						
						// DB �� ���� ����
						ServerManager.getInstance().getMemberDAO().update(memberDTO);
						ServerManager.getInstance().getMemberDAO().update(memberDTOLoser);
						
						// ���� ���� ����
						result = ServerConstants.UPDATE_SUCCESS;
					}
					else
					{	// ��ȿ���� ���� ��û�� ���
						result = ServerConstants.UPDATE_INVALID;
					}
				}	// if (memberDTO != null && memberDTOLoser != null)
				else
				{	// ���� ���� ����
					result = ServerConstants.UPDATE_FAIL;
				}
				
				pw.println(result);
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
}	// UpdateServer Class