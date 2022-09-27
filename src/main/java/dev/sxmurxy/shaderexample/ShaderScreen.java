package dev.sxmurxy.shaderexample;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

public class ShaderScreen extends Screen {
	
	private Shader shader;
	private Screen lastScreen;
	
	public ShaderScreen(Screen lastScreen) {
		super(new StringTextComponent("Shader example"));
		this.lastScreen = lastScreen;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
		shader.draw(mouseX / 300f, (Minecraft.getInstance().getWindow().getGuiScaledHeight() - mouseY) / 300f);
		super.render(matrices, mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void init() {
		shader = new Shader("glsandbox.fsh");
		MainWindow window = Minecraft.getInstance().getWindow();
		this.addButton(new Button(window.getGuiScaledWidth() - 80, window.getGuiScaledHeight() - 30, 70, 20, new StringTextComponent("Close"),
					(button) -> { 
						Minecraft.getInstance().setScreen(lastScreen);
							}));
	}
	
}
