package dev.sxmurxy.shaderexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;


public class Shader {
	
	private static final Minecraft MC = Minecraft.getInstance();
	private static final MainWindow WINDOW = MC.getWindow();
	public static final String VERTEX_SORUCE = getShaderSource("vertex.vsh");
	private int programId;
	private long initTime;
	
	public Shader(String fragmentShaderName) {
		int programId = GlStateManager.glCreateProgram();
		try {
			int vertexShader = GlStateManager.glCreateShader(GL30.GL_VERTEX_SHADER);
			GlStateManager.glShaderSource(vertexShader, VERTEX_SORUCE);
			GlStateManager.glCompileShader(vertexShader);
			int isVertexCompiled = GlStateManager.glGetShaderi(vertexShader, GL30.GL_COMPILE_STATUS);
			
			if(isVertexCompiled == 0) {
				GlStateManager.glDeleteShader(vertexShader);
				System.err.println("Vertex shader couldn't compile. It has been deleted.");
			}
			
			int fragmentShader = GlStateManager.glCreateShader(GL30.GL_FRAGMENT_SHADER);
			GlStateManager.glShaderSource(fragmentShader, getShaderSource(fragmentShaderName));
			GlStateManager.glCompileShader(fragmentShader);
			
			int isFragmentCompiled = GlStateManager.glGetShaderi(fragmentShader, GL30.GL_COMPILE_STATUS);
			if(isFragmentCompiled == 0) {
				GlStateManager.glDeleteShader(fragmentShader);
				System.err.println("Fragment shader couldn't compile. It has been deleted.");
			}
			
			GlStateManager.glAttachShader(programId, vertexShader);
			GlStateManager.glAttachShader(programId, fragmentShader);
			GlStateManager.glLinkProgram(programId);
		} catch(Exception e) {
			e.printStackTrace();
		}
		this.programId = programId;
		this.initTime = System.currentTimeMillis();
	}
	
	public void draw(float mouseX, float mouseY) {
		GlStateManager._glUseProgram(programId);
		GL30.glUniform2f(GL30.glGetUniformLocation(programId, "resolution"), WINDOW.getWidth(), WINDOW.getHeight());
		GL30.glUniform2f(GL30.glGetUniformLocation(programId, "mouse"), mouseX, mouseY);
		GL30.glUniform1f(GL30.glGetUniformLocation(programId, "time"), (System.currentTimeMillis() - initTime) / 1000f);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(0, WINDOW.getGuiScaledHeight());
		GL11.glVertex2d(WINDOW.getGuiScaledWidth(), WINDOW.getGuiScaledHeight());
		GL11.glVertex2d(WINDOW.getGuiScaledWidth(), 0);
		GL11.glEnd();
		GlStateManager._glUseProgram(0);
	}
	
	public static String getShaderSource(String fileName) {
		String source = "";
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ShaderExample.class
				.getResourceAsStream("/assets/" + ShaderExample.MOD_ID + "/shaders/" + fileName)));
		source = bufferedReader.lines().filter(str -> !str.isEmpty()).map(str -> str = str.replace("\t", "")).collect(Collectors.joining("\n"));
		try {
			bufferedReader.close();
		} catch (IOException ignored) {
			
		}
		return source;
	}
	
}
