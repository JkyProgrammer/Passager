package displayer;

import main.Passage;

public class PassagePaintQueueItem {
	Passage passage;
	boolean wantsDebug = false;
	boolean wantsNormal = false;
	
	public PassagePaintQueueItem (Passage p, boolean d, boolean n) {
		passage = p;
		wantsDebug = d;
		wantsNormal = n;
	}
}
