package othello;

/*
	���� ���ڿ��� �� ���ڿ� ���� ������
	���� 40�� ���� ���ڿ��� �����Ͽ�,
	��ȣȭ/��ȣȭ �� �����Ѵ�.
*/

// ���� 20�� ���� �����ϴ� ��ȣȭ/��ȣȭ Ŭ����
public class Encryptor
{
	// ���� ������ ���ε� �ε���
	private static final int ordinal_10 = 7;
	private static final int ordinal_1	= 33;
	// �� ���ڰ� ���ε� �ε���
	private static final int ordinal[] = {	34, 4, 25, 12, 9,
											31, 17, 22, 5, 14,
											3, 30, 15, 11, 29,
											20, 8, 38, 13, 27	};
	
	// ��ȣȭ
	public static String encrypt(String str)
	{
		if (str == null)
			return null;
		
		String encrypted = null;
		char temp[] = new char[40];
		
		for (int i=0; i<40; i++)
			// �ƽ�Ű �ڵ�� 'A' ~ 'z' ����
			temp[i] = (char)(Math.random()*((int)'z' - (int)'A' + 1) + (int)'A');

		int strlen = str.length();
		
		// ���� ���� ����
		temp[ordinal_10] = (char)((int)'0' + (int)(strlen/10));
		temp[ordinal_1] = (char)((int)'0' + (int)(strlen%10));
		// ���� ���ڿ��� �� ���� ����
		for (int i=0; i<strlen; i++)
			temp[ordinal[i]] = str.charAt(i);
		
		encrypted = new String(temp);
		return encrypted;
	}
	
	// ��ȣȭ
	public static String decrypt(String str)
	{
		if (str == null)
			return null;
		
		// ���� ���� �ҷ�����
		int strlen = ((int)str.charAt(ordinal_10) - (int)'0') * 10 + ((int)str.charAt(ordinal_1) - (int)'0');
		
		String decrypted = "";
		// ���� ���ڿ� ����
		for (int i=0; i<strlen; i++)
			decrypted += Character.toString(str.charAt(ordinal[i]));
		
		return decrypted;		
	}
}