package world.tsmk.customjukebox.mixins;

import net.minecraft.client.sound.SoundLoader;
import net.minecraft.resource.ResourceFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import world.tsmk.customjukebox.NetResourceFactory;

@Mixin(SoundLoader.class)
public class MixinAtSoundLoader {

	@Mutable
	@Shadow
	@Final
	private ResourceFactory resourceFactory;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(ResourceFactory resourceFactory, CallbackInfo ci) {
		this.resourceFactory = new NetResourceFactory(resourceFactory);
	}
}
