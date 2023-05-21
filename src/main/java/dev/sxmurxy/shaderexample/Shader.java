package dev.sxmurxy.shaderexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;


public class Shader {
	
	private static final MainWindow WINDOW = Minecraft.getInstance().getWindow();
	private static final String VERTEX_SORUCE = getShaderSource("vertex.vsh");
	private final int programId;
	private final long initTime;
	
	public Shader(String fragmentShaderName) {
		this.programId = GlStateManager.glCreateProgram();
		this.initTime = System.currentTimeMillis();
		
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
	}
	
	public void draw(double mouseX, double mouseY) {
		GlStateManager._glUseProgram(programId);
		
		GL30.glUniform2f(GL30.glGetUniformLocation(programId, "resolution"), WINDOW.getWidth(), WINDOW.getHeight());
		GL30.glUniform2f(GL30.glGetUniformLocation(programId, "mouse"), (float) mouseX, (float) mouseY);
		GL30.glUniform1f(GL30.glGetUniformLocation(programId, "time"), (System.currentTimeMillis() - initTime) / 1000f);
		
		GL30.glBegin(GL30.GL_QUADS);
		GL30.glVertex2d(0, 0);
		GL30.glVertex2d(0, WINDOW.getGuiScaledHeight());
		GL30.glVertex2d(WINDOW.getGuiScaledWidth(), WINDOW.getGuiScaledHeight());
		GL30.glVertex2d(WINDOW.getGuiScaledWidth(), 0);
		GL30.glEnd();
		
		GlStateManager._glUseProgram(0);
	}

	public static String getShaderSource(String fileName) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(ShaderExample.class
				.getResourceAsStream("/assets/" + ShaderExample.MOD_ID + "/shaders/" + fileName)))) {
			return reader.lines().filter(str -> !str.isEmpty()).map(str -> str = str.replace("\t", "")).collect(Collectors.joining("\n"));
		} catch (IOException ex) {
			return "";
		}
	}

}
