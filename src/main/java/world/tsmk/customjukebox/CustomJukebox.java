package world.tsmk.customjukebox;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jetbrains.annotations.Nullable;

import java.security.Security;
import java.util.Hashtable;

public class CustomJukebox implements ModInitializer {
	public static final Identifier suid = new Identifier("customjukebox", "music_disc_netmusic");
	public static final SoundEvent event = SoundEvent.of(suid);

	public static final Item MUSIC_DISC_NETMUSIC = new NetRecord(15,
			event,
			new Item.Settings().maxCount(1).rarity(Rarity.RARE),
			71);
	private static final Logger log = LogManager.getLogger(CustomJukebox.class);
	private static final Hashtable<Pos, String> register = new Hashtable<>();

	protected static void registerSoundEvent(Pos key, String v) {
		register.put(key, v);
	}

	@Nullable
	public static String getSoundEvent(BlockPos pos) {
		return register.remove(pos);
	}

	@Override
	public void onInitialize() {
		Registry.register(Registries.ITEM, new Identifier("customjukebox", "music_disc_netmusic"), MUSIC_DISC_NETMUSIC);
		Security.addProvider(new BouncyCastleProvider());
		//Registry.register(Registries.ITEM, new Identifier("customjukebox", "music_disc_netmusic"), MUSIC_DISC_NETMUSIC);
		Registry.register(Registries.SOUND_EVENT, suid, event);
	}
}
