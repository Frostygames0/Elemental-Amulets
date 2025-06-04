package frostygames0.elementalamulets.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public record BlockFace(BlockPos pos, Direction face) {
    public boolean isEquivalent(BlockFace other) {
        if (equals(other)) {
            return true;
        }
        return getConnectedPos().equals(other.pos()) && pos().equals(other.getConnectedPos());
    }

    public Direction getOppositeFace() {
        return face().getOpposite();
    }

    public BlockFace getOpposite() {
        return new BlockFace(getConnectedPos(), getOppositeFace());
    }

    public BlockFace withDifferentFace(Direction face) {
        return new BlockFace(pos(), face);
    }

    public BlockPos getConnectedPos() {
        return pos().relative(face());
    }
}
