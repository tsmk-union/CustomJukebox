package world.tsmk.customjukebox.mixins;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import world.tsmk.customjukebox.CustomJukebox;
import world.tsmk.customjukebox.Pos;

import java.lang.reflect.Field;

@Mixin(SoundSystem.class)
public class MixinAtSoundSystem {
	private static final Logger log = LoggerFactory.getLogger(MixinAtSoundSystem.class);

	@Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At(value = "HEAD"))
	private void play(SoundInstance soundInstance, CallbackInfo ci) {
		if (soundInstance.getId().getNamespace().equals("customjukebox") && soundInstance.getId().getPath().equals("music_disc_netmusic")) {
			int x = (int) soundInstance.getX();
			int y = (int) soundInstance.getY();
			int z = (int) soundInstance.getZ();
			log.info("{} {} {}", x, y, z);
			String musicid = CustomJukebox.getSoundEvent(new Pos(x, y, z));
			try {
				Field id = soundInstance.getClass().getField("id");
				id.setAccessible(true);
				id.set(soundInstance, new Identifier("customjukebox", "music_disc_netmusic" + musicid));
			} catch (NoSuchFieldException | IllegalAccessException ignored) {
			}
		}

	}


}
