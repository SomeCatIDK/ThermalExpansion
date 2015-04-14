package cofh.thermalexpansion.block.plate;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import cofh.core.network.PacketCoFHBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

public class TilePlatePoweredBase extends TilePlateBase implements IEnergyReceiver {

	protected EnergyStorage storage;

	protected TilePlatePoweredBase(BlockPlate.Types type, int storage) {

		super(type);
		this.storage = new EnergyStorage(storage);
	}

	@Override
	public boolean setFacing(int facing) {

		return false;
	}

	@Override
	void setAlignment(int side, float hitX, float hitY, float hitZ) {

		alignment = (byte) side;
		direction = 7;
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);
		storage.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);
		storage.writeToNBT(nbt);
	}

	@Override
	public PacketCoFHBase getGuiPacket() {

		PacketCoFHBase payload = super.getGuiPacket();

		payload.addInt(storage.getEnergyStored());

		return payload;
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void handleGuiPacket(PacketCoFHBase payload) {

		super.handleGuiPacket(payload);

		storage.setEnergyStored(payload.getInt());
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {

		return from.ordinal() == alignment;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {

		return storage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {

		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {

		return storage.getMaxEnergyStored();
	}

}
