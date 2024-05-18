package world.tsmk.customjukebox;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceFactory;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class NetResourceFactory implements ResourceFactory {

	private static final Logger log = LoggerFactory.getLogger(NetResourceFactory.class);
	public final ResourceFactory superResourceFactory;

	public NetResourceFactory(ResourceFactory resourceFactory) {
		superResourceFactory = resourceFactory;
	}

	@Override
	public Optional<Resource> getResource(Identifier var1) {
		if (var1.toString().equals("customjukebox:music_disc_netmusic")) {
			log.info("aaaaaaaaaaaaa");
		}
		return superResourceFactory.getResource(var1);
	}

	public Resource getResourceOrThrow(Identifier id) throws FileNotFoundException {
		if (id.toString().equals("customjukebox:music_disc_netmusic")) {
			log.info("aaaaaaaaaaaaa");
		}
		return superResourceFactory.getResourceOrThrow(id);
	}

	@Override

	public InputStream open(Identifier id) throws IOException {
		String idStr = id.toString();
		try {
			if (!idStr.equals("customjukebox:sounds/music_disc_netmusic") && idStr.startsWith("customjukebox:sounds/music_disc_netmusic") && idStr.endsWith(".ogg")) {
				String musicId = id.getPath().substring(26, id.getPath().length() - 4);
				return Tool.toOgg(Tool.getNetMusic(musicId).openStream());
			}
		} catch (Exception ignored) {
		}
		return superResourceFactory.open(id);
	}

	@Override
	public BufferedReader openAsReader(Identifier id) throws IOException {
		return superResourceFactory.openAsReader(id);
	}

}
