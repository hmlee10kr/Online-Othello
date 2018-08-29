package othello;

import javax.swing.text.*;

// 텍스트 필드의 글자수 입력에 제한을 두기 위한 클래스
// limit 만큼 텍스트/패스워드 필드 글자수 제한
public class JTextFieldLimit extends PlainDocument
{
	private int limit;
	
	JTextFieldLimit(int limit)
	{
		super();
		this.limit = limit;
	}
	
	JTextFieldLimit(int limit, boolean upper)
	{
		super();
		this.limit = limit;
	}
	
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
	{
		if (str == null)
			return;
		
		if ((getLength() + str.length()) <= limit)
			super.insertString(offset, str, attr);
	}
}