package net.yan01h.edgejump;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import org.lwjgl.glfw.GLFW;

import java.util.stream.StreamSupport;

public class EdgeJump implements ClientModInitializer {
	public static final MinecraftClient CLIENT = MinecraftClient.getInstance();

	private static final KeyBinding EDGEJUMP_KEYBIND = new KeyBinding(
			"key.edgejump.toggle_edge_jump",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_G,
			"key.category.edgejump.keybinds"
	);

	public static boolean edgeJumpEnabled = false;

	@Override
	public void onInitializeClient() {
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (EDGEJUMP_KEYBIND.wasPressed()) {
				edgeJumpEnabled = !edgeJumpEnabled;
				String onOff = edgeJumpEnabled ? "On" : "Off";
				client.player.sendMessage(new LiteralText("EdgeJump: " + onOff), true);
			}
		});

		KeyBindingHelper.registerKeyBinding(EDGEJUMP_KEYBIND);
	}

	public static void onTick() {
		if (CLIENT.player == null && !edgeJumpEnabled)
			return;
		if (!CLIENT.player.isOnGround() || CLIENT.options.jumpKey.isPressed())
			return;
		if (CLIENT.player.isSneaking() || CLIENT.options.sneakKey.isPressed())
			return;

		Box box = CLIENT.player.getBoundingBox();
		Box adjustedBox = box.stretch(0, -0.5, 0).expand(-0.001, 0, -0.001);
		Iterable<VoxelShape> blockCollisions = CLIENT.player.getWorld().getBlockCollisions(CLIENT.player, adjustedBox);
		if (StreamSupport.stream(blockCollisions.spliterator(), false).findAny().isPresent())
			return;
		if (CLIENT.player.forwardSpeed > 0)
			CLIENT.player.jump();
	}
}
