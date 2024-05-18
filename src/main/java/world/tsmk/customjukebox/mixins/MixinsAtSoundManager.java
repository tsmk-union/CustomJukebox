package world.tsmk.customjukebox.mixins;

import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.WeightedSoundSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundManager.class)
public class MixinsAtSoundManager {
	@Inject(method = "get", at = @At("RETURN"), cancellable = true)
	private void get(Identifier id, CallbackInfoReturnable<WeightedSoundSet> cir) {
		if (id.getNamespace().equals("customjukebox") && id.getPath().startsWith("music_disc_netmusic") && !id.getPath().equals("music_disc_netmusic")) {
			WeightedSoundSet soundSet = new WeightedSoundSet(id, null);
			soundSet.add(new Sound(
					id.toString(),
					ConstantFloatProvider.create(0.05f),
					ConstantFloatProvider.create(1.0f),
					1,
					Sound.RegistrationType.FILE,
					true,
					false,
					16));
			cir.setReturnValue(soundSet);
		}
	}
}
