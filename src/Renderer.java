package yg.and.wallpaper.space;



import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.animation.Animation3D;
import rajawali.animation.RotateAnimation3D;
import rajawali.animation.TranslateAnimation3D;
import rajawali.lights.ALight;
import rajawali.lights.DirectionalLight;
import rajawali.materials.DiffuseMaterial;
import rajawali.materials.SimpleMaterial;
import rajawali.math.Number3D;
import rajawali.primitives.Cube;
import rajawali.primitives.Plane;
import rajawali.renderer.RajawaliRenderer;
import yg.and.wallpaper.space.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;


public class Renderer extends RajawaliRenderer {
	
	private Animation3D mAnim;
	
	private Animation3D mCamAnim;
	
	private Plane mothership, starsFrontA, starsFrontB, starsBackA, starsBackB, starsBackC, fire;
	
	private float starsScrollFrontA = 0;
	private float starsScrollFrontB = 0;
	private float starsWidthFront = 15f;
	private float starsScrollBackA = 0;
	private float starsScrollBackB = 0;
	private float starsScrollBackC = 0;
	private float starsWidthBack = 16f;
	
	private final float frontSpeed = 7f;
	private final float backSpeed = 5f;
	
	private float fireScale = 1f;
	private boolean fireScaleUp = true;
	
	private boolean lowFPS;
	
	private Bitmap starsImage, nebulaImage, shipImage, fireImage;
	private SimpleMaterial nebulaMaterial, starsMaterial, shipMaterial, fireMaterial;
	
	public Renderer(Context context) {
		super(context);
	}

	public void configUpdate() {
		if(this.preferences.getBoolean("battery_checkbox",false)) {
			this.setFrameRate(24f);
			lowFPS = true;
		}
		else {
			this.setFrameRate(40f);
			lowFPS = false;
		}
	}
	
	public void initScene() {
		//mCamera.setFarPlane(1000);
		
		this.setFrameRate(40f);
		
		this.setBackgroundColor(1f,1f,1f,1f	);
		//this.setBackgroundColor(0f,0f,0f,0f	);
		
		nebulaImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.nebula);
		nebulaMaterial = new SimpleMaterial();
		nebulaMaterial.addTexture(mTextureManager.addTexture(nebulaImage));
		//smaterialS.setInterpolation(0f);
		
		
		starsImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.stars);
		starsMaterial = new SimpleMaterial();
		starsMaterial.addTexture(mTextureManager.addTexture(starsImage));
		
		shipImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.mothership);
		shipMaterial = new SimpleMaterial();
		shipMaterial.addTexture(mTextureManager.addTexture(shipImage));
		
		fireImage = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fire);
		fireMaterial = new SimpleMaterial();
		fireMaterial.addTexture(mTextureManager.addTexture(fireImage));
		
			
	
		float scrollErrorOffset = 0.05f; 
		float initOffset = starsWidthBack * 1.2f;//1.2f
		float starsBackZ = 15f;
		starsBackA = new Plane(starsWidthBack, starsWidthBack + scrollErrorOffset, 1, 1, 1);
		starsBackA.setMaterial(nebulaMaterial);		
		addChild(starsBackA);
		starsScrollBackA = starsWidthBack * 0 + initOffset;
		starsBackA.setPosition(  starsScrollBackA,0,starsBackZ);
		//starsBackA.setTransparent(true);
		starsBackA.rotateAround(new Number3D(0,0,1f), -90f);
		
		starsBackB = new Plane(starsWidthBack, starsWidthBack + scrollErrorOffset, 1, 1, 1);
		starsBackB.setMaterial(nebulaMaterial);		
		addChild(starsBackB);
		starsScrollBackB = starsWidthBack  * -1.0f + initOffset;
		starsBackB.setPosition(starsScrollBackB ,0,starsBackZ);
		//starsBackB.setTransparent(true);		
		starsBackB.rotateAround(new Number3D(0,0,1f), -90f);

		starsBackC = new Plane(starsWidthBack , starsWidthBack + scrollErrorOffset, 1, 1, 1);
		starsBackC.setMaterial(nebulaMaterial);		
		addChild(starsBackC);
		starsScrollBackC = starsWidthBack * -2.0f + initOffset;
		starsBackC.setPosition(starsScrollBackC ,0,starsBackZ);
		//starsBackC.setTransparent(true);		
		starsBackC.rotateAround(new Number3D(0,0,1f), -90f);
		
		
		
		
		float mothershipZ = 2.5f;
		
		
		
		
		
		mothership = new Plane(3.5f, 3.5f, 1, 1, 1);
		mothership.setMaterial(shipMaterial);			
		//
		mothership.setPosition(0,0,mothershipZ);
		mothership.setTransparent(true);
		mothership.rotateAround(new Number3D(0,0,1f), -90f);
		
		
		fire = new Plane(0.23f, 0.23f, 1, 1, 1);
		fire.setMaterial(fireMaterial);		
		addChild(fire);
		fire.setPosition(1.8f, - .11f,mothershipZ + .1f);
		fire.setTransparent(true);
		fire.rotateAround(new Number3D(0,0,1f), -90f);
		//fire.setScaleX(2f);
		fire.setBlendingEnabled(true);
		fire.setBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_COLOR);
		
		addChild(mothership);//needs to layer in front of fire
		
		
		

		float starsFrontZ = 6f;
		starsFrontA = new Plane(starsWidthFront, starsWidthFront, 1, 1, 1);
		starsFrontA.setMaterial(starsMaterial);	
		starsScrollFrontA = 0;
		addChild(starsFrontA);
		starsFrontA.setPosition(0,0,starsFrontZ);
		starsFrontA.setTransparent(true);
		starsFrontA.setBlendingEnabled(true);
		starsFrontA.setBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_COLOR);
		//(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_COLOR); (GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		
		
		starsFrontB = new Plane(starsWidthFront, starsWidthFront, 1, 1, 1);
		starsFrontB.setMaterial(starsMaterial);		
		addChild(starsFrontB);
		starsScrollFrontB = - starsWidthFront;
		starsFrontB.setPosition(starsScrollFrontB ,0,starsFrontZ);//tile slightly apart, overlapping has some alpha issues
		starsFrontB.setTransparent(true);
		starsScrollFrontB = -starsWidthFront;
		starsFrontB.setBlendingEnabled(true);
		starsFrontB.setBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_COLOR);
		
		
		configUpdate();
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
	
		
		configUpdate();
	}
	
	public void onSurfaceDestroyed() {
		super.onSurfaceDestroyed();
		
	
		//	private Bitmap starsImage, nebulaImage, shipImage, fireImage;
		//private SimpleMaterial nebulaMaterial, starsMaterial, shipMaterial, fireMaterial;
		starsImage.recycle();
		nebulaImage.recycle();
		shipImage.recycle();
		fireImage.recycle();
		
		nebulaMaterial.destroy();
		starsMaterial.destroy();
		shipMaterial.destroy();
		fireMaterial.destroy();
		
		//starsTexture.recycle();
	}

	public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
		
		
		mCamera.setX( (   (xOffset - .5f) )   );
		//System.out.println("offset" + xOffset  );
	}
	
	
	public void visibleUpdate() {
		
	}
	

	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		
		float elapsed = 1f/(float)mLastMeasuredFPS;
		//System.out.println("elapsed:   " + elapsed  );
		//System.out.println("fps" + mLastMeasuredFPS  );
		if(lowFPS) {
			if(elapsed > .056f) //low cap 
				elapsed = .056f;
		}
		else {
			if(elapsed > .026f) //low cap 
				elapsed = .026f;
		}
		
		if(fireScaleUp){
			fireScale += elapsed * 45.0f;
			if(fireScale > 4f){
				fireScale = 4f;
				fireScaleUp = false;
			}
		} else {
			fireScale -= elapsed * 42.0f;
			if(fireScale < 1f){
				fireScale = 1f;
				fireScaleUp = true;
			}
		}
		fire.setScaleX(fireScale);
		
		
		starsScrollFrontA += elapsed * frontSpeed;		
		if(starsScrollFrontA > starsWidthFront) {
			starsScrollFrontA -= starsWidthFront * 2f;
		}
		starsFrontA.setX(starsScrollFrontA);		
		starsScrollFrontB += elapsed * frontSpeed;		
		if(starsScrollFrontB > starsWidthFront) {
			starsScrollFrontB -= starsWidthFront* 2f;
		}
		starsFrontB.setX(starsScrollFrontB);
		
		
		starsScrollBackA += elapsed * backSpeed;		
		if(starsScrollBackA > starsWidthBack * 1.5f) {
			starsScrollBackA -= starsWidthBack * 3f;
		}
				
		starsScrollBackB += elapsed * backSpeed;		
		if(starsScrollBackB > starsWidthBack * 1.5f) {
			starsScrollBackB -= starsWidthBack * 3f;
		}
		
		starsScrollBackC += elapsed * backSpeed;		
		if(starsScrollBackC > starsWidthBack * 1.5f) {
			starsScrollBackC -= starsWidthBack * 3f;
		}
		
		starsBackA.setX(starsScrollBackA);
		starsBackB.setX(starsScrollBackB);
		starsBackC.setX(starsScrollBackC);
		
		
		//System.out.println("fps" + mLastMeasuredFPS  );
		//
		
		//mSkybox.setRotX(mSkybox.getRotX() + -.45f);
		//mSkybox.setRotY(mSkybox.getRotY() + .5f);
		
		//mCamera.setRotY(mCamera.getRotY() + .5f);
		//mCamera.setLookAt(0,0,0);
		
	}
	
	
	
	public void onTouchEvent(MotionEvent event) {
		
	}
	
	
}
