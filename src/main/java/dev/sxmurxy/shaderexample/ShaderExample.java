package dev.sxmurxy.shaderexample;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(ShaderExample.MOD_ID)
public class ShaderExample {
	
	public static final String MOD_ID = "shaderexample";

	public ShaderExample() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onInit(GuiScreenEvent.InitGuiEvent e) {
		if(e.getGui() instanceof MainMenuScreen) {
			Minecraft mc = Minecraft.getInstance();
			e.addWidget(new Button(mc.getWindow().getGuiScaledWidth() / 2 + 110, mc.getWindow().getGuiScaledHeight() / 4 + 48, 98, 20, new StringTextComponent("Shader example"),
					(button) -> { 
						Minecraft.getInstance().setScreen(new ShaderScreen(mc.screen));
							}));
		}
	}
	
}
