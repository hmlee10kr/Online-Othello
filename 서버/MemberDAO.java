import java.sql.*;
import java.util.*;

public class MemberDAO {
   String jdbcDriver = "com.mysql.jdbc.Driver";
   String jdbcUrl = "jdbc:mysql://" + ServerConstants.DATABASE_IP + ":" + ServerConstants.DATABASE_PORT + "/othello";
   Connection conn;
   //�ڹٿ� JDBC�� �����ϱ� ����
   
   PreparedStatement pstmt;
   ResultSet rs;
   //��ɿ� ���� ��ȯ���� ����� �����ϱ� ����

   String sql;
   //��ɾ �����ϱ� ���� String ����
   
   public MemberDAO()
   {   
	   try{
       Class.forName(jdbcDriver);
	   ServerManager.getInstance().setMemberDAO(this);
	   
      }catch(Exception e){
          e.printStackTrace();
       }
   }
   
   //���� �޼ҵ�
   public void connectDB(){
      try{
         conn = DriverManager.getConnection(jdbcUrl,ServerConstants.DATABASE_ID,ServerConstants.DATABASE_PW);
         System.out.println("DB ���� ����!!");
      }catch(Exception e){
         e.printStackTrace();
      }
   }
   public void closeDB(){
      try{
         if (pstmt != null) pstmt.close();
         if (rs != null) rs.close();
         if (conn != null) conn.close();
      }catch (Exception e) {
         // TODO: handle exception
         e.printStackTrace();
      }
   }
   //DB�� ������ ���� ����
      
   //1. info���̺� �߰� �޼ҵ�
   public synchronized boolean insert(MemberDTO mdto){
      connectDB();
      sql = "insert into info values(?,?,?,?,?,?,?)";
      
      int result = 0;
      
      try{
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, mdto.getId());
         pstmt.setString(2, mdto.getPassword());
         pstmt.setInt(3, mdto.getTotal());
         pstmt.setDouble(4, mdto.getWrate());
         pstmt.setInt(5, mdto.getWin());
         pstmt.setInt(6, mdto.getLose());
         pstmt.setInt(7, mdto.getEscape());
         result = pstmt.executeUpdate();
      }catch(SQLException e){
         e.printStackTrace();
      }
      closeDB();
      if(result>0)
         return true;
      else
         return false;
   }
   //���ο� �����͸� �����ϱ� ���� �Լ�
      
   //2. �α��� ���� ���� �޼ҵ�
   public synchronized boolean login(String id, String password){
      connectDB();
      sql = "select * from info where id=? and password=?";
      
      ResultSet rs = null;
      
      try{
         pstmt = conn.prepareStatement(sql);
         pstmt.setString(1, id);
         pstmt.setString(2, password);
         
         rs = pstmt.executeQuery();
      }catch(SQLException e){
         e.printStackTrace();
      }
      
      try {
	      if(rs.next())
	      {
	          closeDB();
	         return true;
	      }
	      else
	      {
	          closeDB();
	         return false;
	      }
      }catch(SQLException e){
          e.printStackTrace();
          return false;
      }
   }   
   //3. ����� ������ ������Ʈ �ϱ� ���� �Լ�
   public synchronized boolean update(MemberDTO mdto){
      connectDB();
      sql="update info set id=?, password=?, total=?, wrate=?, win=?, lose=?, escape=? where id=?";
      int result=0;
      
      try{
         pstmt=conn.prepareStatement(sql);
         pstmt.setString(1, mdto.getId());
         pstmt.setString(2, mdto.getPassword());
         pstmt.setInt(3, mdto.getTotal());
         pstmt.setDouble(4, mdto.getWrate());
         pstmt.setInt(5, mdto.getWin());
         pstmt.setInt(6, mdto.getLose());
         pstmt.setInt(7, mdto.getEscape());
         pstmt.setString(8, mdto.getId());
         result=pstmt.executeUpdate();
      }catch(SQLException e){
         e.printStackTrace();
      }
      closeDB();
      if(result>0)
         return true;
      else
         return false;
   }
   //�̹� ����� �����͸� �����ϱ� ���� �Լ�
   
   //4. ȸ��Ż�� ���� �Լ�
   public synchronized boolean drop(String id, String password){
      connectDB();
      sql = "delete from info where id=? and password=?";
      int result=0;
      
      try{
         pstmt=conn.prepareStatement(sql);
         pstmt.setString(1, id);
         pstmt.setString(2, password);
         result = pstmt.executeUpdate();
      }catch(SQLException e){
         e.printStackTrace();
      }
      
      closeDB();
      if(result>0)
         return true;
      else
         return false;
   }   
   
   //4.���̵��� �̿��Ͽ� ȸ�� 1���� ������ ���ϴ� �޼ҵ�
   public synchronized MemberDTO info(String id){
	  connectDB();
      sql = "select * from info where id=?";
      
      try{
         pstmt=conn.prepareStatement(sql);
         pstmt.setString(1,id);
      }catch(SQLException e){
         e.printStackTrace();
      }

      MemberDTO mdto=null;   
      try{
	      ResultSet rs=pstmt.executeQuery();
	      //rs�� �����͸� MemberDTO��ü�� ����(11��)
	         
	      if(rs.next()){
	    	 mdto = new MemberDTO();
	         mdto.setId(rs.getString("id"));
	         mdto.setPassword(rs.getString("password"));
	         mdto.setTotal(rs.getInt("total"));
	         mdto.setWrate(rs.getDouble("wrate"));
	         mdto.setWin(rs.getInt("win"));
	         mdto.setLose(rs.getInt("lose"));
	         mdto.setEscape(rs.getInt("escape"));
	      }
      }catch(SQLException e){
          e.printStackTrace();
      }
      //�����Ͱ� ���������� ����
         
         
      closeDB();
         
      return mdto;//MemberDTO�� ��ü
         
   }
      
   //5.��ü ȸ�� ��� ��ȸ �޼ҵ�
   public synchronized ArrayList<MemberDTO> search(String id){
      connectDB();
      sql = "select * from info";
      ArrayList<MemberDTO> list=new ArrayList<MemberDTO>();

      try{
         pstmt=conn.prepareStatement(sql);
      }catch(SQLException e){
         e.printStackTrace();
      }
      
      try{
	      ResultSet rs=pstmt.executeQuery();
	      //rs=>ArrayList<MemberDTO>��ȯ
	         
	      while(rs.next()){
	         MemberDTO mdto=new MemberDTO();
	            
	         mdto.setId(rs.getString("id"));
	         mdto.setPassword(rs.getString("password"));
	         mdto.setTotal(rs.getInt("total"));
	         mdto.setWrate(rs.getDouble("wrate"));
	         mdto.setWin(rs.getInt("win"));
	         mdto.setLose(rs.getInt("lose"));
	         mdto.setEscape(rs.getInt("escape"));
	         
	         list.add(mdto);
	      }
      }catch(SQLException e){
          e.printStackTrace();
      }
         
      closeDB();
         
      return list;
   }   

	// 6. ä�� �α� ����
	public synchronized boolean chatLogging(String origin, String dest, String message)
	{
		int result = 0;
		sql = "INSERT INTO chatlog VALUES(?, ?, ?, ?)";
		
		connectDB();

		try
		{
			long timeNow = Calendar.getInstance().getTimeInMillis();
			Timestamp ts = new Timestamp(timeNow);

			// ä�� ����� Ÿ�ӽ������� �Բ� ����
			pstmt = conn.prepareStatement(sql);
			pstmt.setTimestamp(1, ts);
			pstmt.setString(2, origin);
			pstmt.setString(3, dest);
			pstmt.setString(4, message);
			result = pstmt.executeUpdate();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		closeDB();
		
		return (result > 0);
	}
}
