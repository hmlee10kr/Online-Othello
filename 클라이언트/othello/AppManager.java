package othello;

// 싱클톤 패턴의 앱매니저
public class AppManager
{
	public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 720;
	public static final int PRVW_IMG_WIDTH = 635;
	public static final int PRVW_IMG_HEIGHT = 690;
	
	private static AppManager appManager = new AppManager();
	private MainFrame mainFrame;
	private GameSelection selectPanel;
	
	// 컴퓨터와 대결시 필요한 패널
	private LevelReversi levelPanel;
	private PlayReversi playPanel;
	
	// 온라인 플레이시 필요한 패널과 로그인 정보
	private Login loginPanel;
	private Join joinPanel;
	private Setting settingPanel;
	private MyProfile myProfilePanel;
	private OtherProfile otherProfilePanel;
	private PlayReversiOnline playOnlinePanel;
	private InUseInfo userInfo;

	// getter
	public static AppManager getInstance()	{ return appManager; }
	
	public MainFrame getMainFrame()					{ return mainFrame; }
	public GameSelection getSelectPanel()			{ return selectPanel; }
	public LevelReversi getLevelPanel()				{ return levelPanel; }
	public PlayReversi getPlayPanel()				{ return playPanel; }
	public Login getLoginPanel()					{ return loginPanel; }
	public Join getJoinPanel()						{ return joinPanel; }
	public Setting getSettingPanel()				{ return settingPanel; }
	public MyProfile getMyProfilePanel()			{ return myProfilePanel; }
	public OtherProfile getOtherProfilePanel()		{ return otherProfilePanel; }
	public PlayReversiOnline getPlayOnlinePanel()	{ return playOnlinePanel; }
	public InUseInfo getUserInfo()					{ return userInfo; }
	
	// setter
	public void setMainFrame(MainFrame mainFrame)						{ this.mainFrame = mainFrame; }
	public void setSelectPanel(GameSelection selectPanel)				{ this.selectPanel = selectPanel; }
	public void setLevelPanel(LevelReversi levelPanel)					{ this.levelPanel = levelPanel; }
	public void setPlayPanel(PlayReversi playPanel)						{ this.playPanel = playPanel; }
	public void setLoginPanel(Login loginPanel)							{ this.loginPanel = loginPanel; }
	public void setJoinPanel(Join joinPanel)							{ this.joinPanel = joinPanel; }
	public void setSettingPanel(Setting settingPanel)					{ this.settingPanel = settingPanel; }
	public void setMyProfilePanel(MyProfile myProfilePanel)				{ this.myProfilePanel = myProfilePanel; }
	public void setOtherProfilePanel(OtherProfile otherProfilePanel)	{ this.otherProfilePanel = otherProfilePanel; }
	public void setPlayOnlinePanel(PlayReversiOnline playOnlinePanel)	{ this.playOnlinePanel = playOnlinePanel; }
	public void setUserInfo(InUseInfo userInfo)							{ this.userInfo = userInfo; }
}