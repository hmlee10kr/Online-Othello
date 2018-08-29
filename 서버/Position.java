// 바둑판에서의 위치를 나타내기 위한 클래스
public class Position
{
	protected int row;	// 행
	protected int col;	// 열

	public Position()
	{
		this(0,0);
	}
	public Position(int row, int col)
	{
		this.row = row;
		this.col = col;
	}
	
	// Getter/Setter
	public int getRow()	{ return row; }
	public int getCol()	{ return col; }
	public void setRow(int row)	{ this.row = row; }
	public void setCol(int col)	{ this.col = col; }
	
	public void setPosition(int row, int col)
	{
		this.row = row;
		this.col = col;
	}

	public boolean equals(Position rPosition)
	{
		return (row == rPosition.row && col == rPosition.col);
	}
} // Position Class