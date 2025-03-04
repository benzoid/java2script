package swingjs.plaf;


import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import swingjs.api.js.DOMNode;

public class JSProgressBarUI extends JSLightweightUI {

	int frameZ = 10003;
	public JSProgressBarUI() {
		isContainer = true;
		setDoc();
	}
	
	@Override
	public DOMNode updateDOMNode() {
		if (domNode == null) {
			JRootPane root = jc.getRootPane();
			isContentPane = (root != null && jc == root.getContentPane());
			domNode = newDOMObject("div", id);
			if (root != null && root.getGlassPane() == c)
				DOMNode.setVisible(domNode,  false);
		}
		return updateDOMNodeCUI();
	}

	@Override
    protected Dimension getHTMLSizePreferred(DOMNode obj, boolean addCSS) {
		// SwingJS for now: just designated container width/height 
		return new Dimension(c.getWidth(), c.getHeight());
	}
	

	@Override
	public void installUI(JComponent jc) {
    LookAndFeel.installColorsAndFont(jc,
        "Panel.background",
        "Panel.foreground",
        "Panel.font");
	}

	@Override
	public void uninstallUI(JComponent jc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Dimension getPreferredSize(JComponent jc) {
  	return null;
  }


}
