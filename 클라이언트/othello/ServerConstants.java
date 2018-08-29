package othello;

/*
	�� ���α׷��� ������ ���������� �̿뿡 ����Ͽ�
	���۵Ǿ��� ������,  MATCH_INVALID, UPDATE_INVALID ����
	���װ� �ƴ� �̻�, Ŭ���̾�Ʈ ���α׷��� ���� �̿�ô� �߻����� �ʴ´�.
*/

// �� ������Ʈ�� Ŭ���̾�Ʈ<->���� �� ������ ���������� ������ Ŭ����
public class ServerConstants
{
	// ������ ����
//	public static String SERVER_DOMAIN = "team9.gonetis.com";	// ���� �л��� ��ǻ�Ϳ��� �������� ����
	public static String SERVER_DOMAIN = "localhost";	// �� ��ǻ�Ϳ��� �������� ������ ������ ���
	
	public static final int REGISTER_SERV_PORT = 32433;	// ȸ�� ���� ���� ��Ʈ
	public static final int WITHDRAWAL_SERV_PORT = 32434;	// ȸ�� Ż�� ���� ��Ʈ
	public static final int CHANGE_SERV_PORT = 32435;	// ��й�ȣ ���� ���� ��Ʈ
	public static final int SEARCH_SERV_PORT = 32436;	// ���� �˻� ���� ��Ʈ
	public static final int LOGIN_SERV_PORT = 32437;	// �α��� ���� ��Ʈ
	public static final int MATCH_SERV_PORT = 32438;	// ��Ī ���� ��Ʈ
	public static final int UPDATE_SERV_PORT = 32439;	// ���� ���� ���� ��Ʈ
	public static final int POLL_SERV_PORT = 32440;	// ��Ī ������ ��⿭���� ������ ��û�ϴ� ���� ��Ʈ
	public static final int CHAT_SERV_PORT = 32441;	// ä�� ���� ��Ʈ
	
	public static final String REGISTER_SUCCESS = "RS";	// ȸ�� ���� ����
	public static final String REGISTER_EXIST = "RE";	// �ߺ��� ���̵� ����

	public static final String WITHDRAWAL_SUCCESS = "WS";	// ȸ�� Ż�� ����
	public static final String WITHDRAWAL_FAIL = "WF";	// ȸ�� Ż�� ����
	
	public static final String CHANGE_SUCCESS = "CS";	// ��й�ȣ ���� ����
	public static final String CHANGE_FAIL = "CF";	// ��й�ȣ ���� ����
	
	public static final String SEARCH_SUCCESS = "SS";	// ���� �˻� ����
	public static final String SEARCH_FAIL = "SF";	// ���� �˻� ����

	public static final String LOGIN_INUSE = "LI";	// �ش� ������ �̹� �����
	public static final String LOGIN_SUCCESS = "LS";	// �α��� ����
	public static final String LOGIN_FAIL = "LF";	// �α��� ����

	// ��Ī�� ���� �����带 �����Ͽ� ������ ���۵Ǹ�,
	// ���� ���� �����尡 �Է��� ���ʾ� �޾� ó���ϱ� ������
	// �� �Ͽ� ��밡 ������, ���� ���� �ΰ� ���ʰ� �ٲ��� ����� Ż�� ����� ���� �޴´�.
	
	// ���� ���Ͽ� ��밡 ������ ���ʰ� �ٲ��� ����ä�� ���� ������ ���, ������ �� ���ῡ�� ���ܰ� �߻��Ѵ�.
	// �� ��, ��뿡�� Ż�� ����� �˸��� MATCH_ALIVE �� �ǵ��� ���� ���� ��밡 ������ �ʾҴٴ� ���� �Ǻ��ϱ� ���� ���ȴ�.
	public static final String MATCH_ALIVE = "MA";	// MATCH_ALIVE
	public static final String MATCH_VALID = "MVLD";	// ������ ��ȿ�Ͽ� ��⿭�� ��ϵ�
	public static final String MATCH_INVALID = "MIVLD";	// ������ ��ȿ���� ����
	
	public static final String MATCH_BLACK = "B";	// ���� ������ ������
	public static final String MATCH_WHITE = "W";	// ���� ������ �Ͼ��
	/* �ٵ����� ���¸� ���ڵ� �� 8x8 �� 64 ������ ���ڿ��� �����ϴ� ���µ� */
	// char �� ������ ������ String.charAt(idx) ���� �񱳽� ���Ǽ��� ��� ����
	public static final char MATCH_CHAR_BLACK = 'B';
	public static final char MATCH_CHAR_WHITE = 'W';
	public static final char MATCH_CHAR_EMPTY = 'E';
	/* �ٵ����� ���¸� ���ڵ� �� 8x8 �� 64 ������ ���ڿ��� �����ϴ� ���µ� */
	
	public static final String MATCH_OK = "MO";	// �ش� ��ġ�� ���� �� �� �־� ó���ߴ�
	public static final String MATCH_YOUR_TURN = "MYT";	// ���� �����̴�
	public static final String MATCH_CANT_PUT = "MCNTP";	// �װ��� ���� �� �� ����
	public static final String MATCH_PASS = "MP";	// �ʰ� �н��ؾ� �Ѵ�
	public static final String MATCH_VS_PASS = "MVP";	// ��밡 �н��ؾ� �Ѵ�
	
	public static final String MATCH_WIN = "MWIN";	// �¸�
	public static final String MATCH_LOSE = "MLOSE";	// �й�
	public static final String MATCH_ESCAPE = "MESC";	// ��밡 Ż��
	public static final String MATCH_DRAW = "MDRW";	// ����
	
	public static final String UPDATE_INVALID = "UI";	// ������ ��ȿ���� ����
	public static final String UPDATE_SUCCESS = "US";	// ���� ���� ����
	public static final String UPDATE_FAIL = "UF";	// ���� ���� ����
	
	public static final String POLL_SUCCESS = "PS";	// ��Ī ������ ��⿭���� ���� �Ϸ�
	public static final String POLL_FAIL = "PF";	// �׷��� ������ ���� ��ü�� ��⿭�� �������� ����
}