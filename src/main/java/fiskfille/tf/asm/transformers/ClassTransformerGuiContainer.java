package fiskfille.tf.asm.transformers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.asm.ASMHooksClient;
import fiskfille.tf.asm.TFTranslator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

@SideOnly(Side.CLIENT)
public class ClassTransformerGuiContainer extends ClassTransformerMethodProcess {
	public ClassTransformerGuiContainer() {
		super("net.minecraft.client.gui.inventory.GuiContainer", "a", "func_146977_a", "(Laay;)V", "(Lnet/minecraft/inventory/Slot;)V");
	}

	@Override
	public void processMethod(final MethodNode method) {
		final InsnList list = new InsnList();

		for(int i = 0; i < method.instructions.size(); ++i) {
			final AbstractInsnNode node = method.instructions.get(i);

			if(node.getOpcode() == Opcodes.RETURN) {
				list.add(new VarInsnNode(Opcodes.ALOAD, 0));
				list.add(new VarInsnNode(Opcodes.ALOAD, 1));
				list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ASMHooksClient.class), "renderSlotPost", TFTranslator.getMappedName("(Lbex;Laay;)V", "(Lnet/minecraft/client/gui/inventory/GuiContainer;Lnet/minecraft/inventory/Slot;)V"), false));
			}

			list.add(node);
		}

		method.instructions.clear();
		method.instructions.add(list);
	}
}
