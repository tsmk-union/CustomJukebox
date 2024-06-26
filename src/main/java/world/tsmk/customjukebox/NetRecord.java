package world.tsmk.customjukebox;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class NetRecord extends MusicDiscItem {
	public NetRecord(int comparatorOutput, SoundEvent sound, Settings settings, int lengthInSeconds) {
		super(comparatorOutput, sound, settings, lengthInSeconds);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockPos blockPos;
		World world = context.getWorld();
		BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());
		if (!blockState.isOf(Blocks.JUKEBOX) || blockState.get(JukeboxBlock.HAS_RECORD).booleanValue()) {
			return ActionResult.PASS;
		}
		ItemStack itemStack = context.getStack();
		NbtCompound nbt = itemStack.getNbt();
		if (nbt != null && nbt.contains("musicid")) {
			CustomJukebox.registerSoundEvent(new Pos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ()), nbt.getString("musicid"));
		}
		if (!world.isClient) {
			PlayerEntity playerEntity = context.getPlayer();
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof JukeboxBlockEntity) {
				JukeboxBlockEntity jukeboxBlockEntity = (JukeboxBlockEntity) blockEntity;
				jukeboxBlockEntity.setStack(itemStack.copy());
				world.emitGameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Emitter.of(playerEntity, blockState));
			}
			itemStack.decrement(1);
			if (playerEntity != null) {
				playerEntity.incrementStat(Stats.PLAY_RECORD);
			}
		}
		return ActionResult.success(world.isClient);
	}
}
