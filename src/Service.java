package yg.and.wallpaper.space;

import rajawali.wallpaper.Wallpaper;
import android.content.Context;
import android.content.res.Configuration;
import android.view.Display;
import android.view.MotionEvent;

public class Service extends Wallpaper {
	private Renderer mRenderer;

	public Engine onCreateEngine() {
		mRenderer = new Renderer(this);
		return new WallpaperEngine(this.getSharedPreferences(SHARED_PREFS_NAME,
				Context.MODE_PRIVATE), getBaseContext(), mRenderer, false);
		
		
		//Display display = getWindowManager().getDefaultDisplay();
		//Point size = new Point();
		//display.getSize(size);
		
		
		
		//public void onConfigurationChanged (Configuration newConfig)
		
		
	}
	
	//public void onConfigurationChanged (Configuration newConfig) {
		//mRenderer.configUpdate();		
	//}
	
	
}
