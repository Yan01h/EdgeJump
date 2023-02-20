package net.yan01h.edgejump.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.yan01h.edgejump.EdgeJump;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class EdgeJumpMixin {
	@Inject(method = "tick", at = @At("HEAD"), cancellable = false)
	public void injectTick(CallbackInfo info) {
		EdgeJump.onTick();
	}
}
