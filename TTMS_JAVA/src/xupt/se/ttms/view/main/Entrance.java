package xupt.se.ttms.view.main;

import java.awt.Insets;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

public class Entrance {

	public static void main(String[] args) {
		
		try {
			// 设置窗口边框样式
			BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.translucencyAppleLike;
			BeautyEyeLNFHelper.launchBeautyEyeLNF();
			// 隐藏设置按钮
			UIManager.put("RootPane.setupButtonVisible", false);
			// 不活动时半透明效果, 设置此开关量为false即表示关闭之，BeautyEye LNF中默认是true
			// BeautyEyeLNFHelper.translucencyAtFrameInactive = false;
			
			// 设置属性即可：true表示使用ToolBar.background颜色实现纯色填充背景，BeautyEye中此属性默认是false
			UIManager.put("ToolBar.isPaintPlainBackground", Boolean.TRUE);
			
			//自定义JToolBar ui的border
			Border bd = new org.jb2011.lnf.beautyeye.ch8_toolbar.BEToolBarUI.ToolBarBorder(
			        UIManager.getColor("ToolBar.shadow")     //Floatable时触点的颜色
			        , UIManager.getColor("ToolBar.highlight")//Floatable时触点的阴影颜色
			        , new Insets(6, 0, 11, 0));              //border的默认insets
			UIManager.put("ToolBar.border",new BorderUIResource(bd));	
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 初始化登陆窗口
		new LoginFrame();
	}
}