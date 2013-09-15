package yg.and.wallpaper.space;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import rajawali.Camera;
import rajawali.Geometry3D;
import rajawali.materials.ParticleMaterial;
import rajawali.math.Number3D;
import rajawali.primitives.Particle;
import rajawali.util.BufferUtil;
import rajawali.util.ObjectColorPicker.ColorPickerInfo;
import android.opengl.GLES20;
import android.util.FloatMath;

public class ExampleParticleSystem2 extends Particle {
	protected Number3D mFriction;
	protected FloatBuffer mVelocityBuffer;
	protected FloatBuffer mAnimOffsetBuffer;
	protected float mTime;
	protected int mCurrentFrame;
	protected int mVelocityBufferHandle;
	
	public ExampleParticleSystem2() {
		super();
	}
	
	protected void init() {
		mMaterial = new ParticleMaterial(true);
		mParticleShader = (ParticleMaterial)mMaterial;
		setDrawingMode(GLES20.GL_POINTS);
		setTransparent(true);
		
		final int numParticles = 200;
		
		float[] vertices = new float[numParticles * 3];
		float[] velocity = new float[numParticles * 3];
		float[] textureCoords = new float[numParticles * 2];
		float[] normals = new float[numParticles * 3];
		float[] colors = new float[numParticles * 4];
		int[] indices = new int[numParticles];
		float[] animOffsets = new float[numParticles];
		
		int index = 0;
		
		for(int i=0; i<numParticles; ++i) {
			index = i * 3;
			vertices[index] = 0;
			vertices[index + 1] = 0;
			vertices[index + 2] = 0;
			
			velocity[index] = -.2f + ((float)Math.random() * .4f);
			velocity[index + 1] = -.2f + ((float)Math.random() * .4f);
			velocity[index + 2] = -.2f + ((float)Math.random() * .4f);
			
			normals[index] = 0;
			normals[index + 1] = 0;
			normals[index + 2] = 1;
			
			index = i * 2;
			textureCoords[i] = 0;
			textureCoords[i + 1] = 0;
			
			index = i * 4;
			colors[i] = 1;
			colors[i + 1] = i;
			colors[i + 2] = i;
			colors[i + 3] = i;
			
			indices[i] = i;
			
			animOffsets[i] = FloatMath.floor((float)Math.random() * 64);
		}
		
		mVelocityBuffer = ByteBuffer
				.allocateDirect(velocity.length * Geometry3D.FLOAT_SIZE_BYTES)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		BufferUtil.copy(velocity, mVelocityBuffer, velocity.length, 0);
		mVelocityBuffer.position(0);
		
		mAnimOffsetBuffer = ByteBuffer
				.allocateDirect(animOffsets.length * Geometry3D.FLOAT_SIZE_BYTES)
				.order(ByteOrder.nativeOrder()).asFloatBuffer();
		BufferUtil.copy(animOffsets, mAnimOffsetBuffer, animOffsets.length, 0);
		mAnimOffsetBuffer.position(0);
		
		mFriction = new Number3D(.95f, .95f, .95f);
		
		int buff[] = new int[1];
		GLES20.glGenBuffers(1, buff, 0);
		mVelocityBufferHandle = buff[0];
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVelocityBufferHandle);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mVelocityBuffer.limit() * Geometry3D.FLOAT_SIZE_BYTES, mVelocityBuffer, GLES20.GL_DYNAMIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
		setData(vertices, normals, textureCoords, colors, indices);
	}
	
	public void reload() {
		super.reload();

		int buff[] = new int[1];
		GLES20.glGenBuffers(1, buff, 0);
		mVelocityBufferHandle = buff[0];
		
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVelocityBufferHandle);
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, mVelocityBuffer.limit() * Geometry3D.FLOAT_SIZE_BYTES, mVelocityBuffer, GLES20.GL_DYNAMIC_DRAW);
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}
	
	public void setTime(float time) {
		mTime = time;
	}
	
	public float getTime() {
		return mTime;
	}
	
	public void setCurrentFrame(int currentFrame) {
		mCurrentFrame = currentFrame;
	}
	
	protected void setShaderParams(Camera camera) {
		super.setShaderParams(camera);
		ParticleMaterial particleShader = (ParticleMaterial) mParticleShader;
		particleShader.setCurrentFrame(mCurrentFrame);
		particleShader.setTileSize(1 / 8f);
		particleShader.setNumTileRows(8);
		particleShader.setAnimOffsets(mAnimOffsetBuffer);
		particleShader.setFriction(mFriction);
		particleShader.setVelocity(mVelocityBufferHandle);
		particleShader.setMultiParticlesEnabled(true);
		particleShader.setTime(mTime);
		particleShader.setCameraPosition(camera.getPosition());
	}
	
	public void render(Camera camera, float[] projMatrix, float[] vMatrix,
			final float[] parentMatrix, ColorPickerInfo pickerInfo) {
		super.render(camera, projMatrix, vMatrix, parentMatrix, pickerInfo);
	}
}
