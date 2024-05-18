package world.tsmk.customjukebox;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class Pos extends BlockPos {

	public Pos(Vec3i pos) {
		super(pos);
	}

	public Pos(int i, int j, int k) {
		super(i, j, k);
	}

	public Pos(BlockPos pos) {
		super(pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public int hashCode() {
		return (this.getX() + " " + this.getY() + " " + this.getZ()).hashCode();
	}
}
