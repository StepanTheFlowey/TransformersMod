package fiskfille.tf.asm.transformers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.asm.ASMHooksClient;
import fiskfille.tf.asm.TFTranslator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ClassTransformerRenderPlayer extends ClassTransformerBase {
	public ClassTransformerRenderPlayer() {
		super("net.minecraft.client.renderer.entity.RenderPlayer");
	}

	@Override
	public boolean processMethods(final List<MethodNode> methods) {
		boolean flag = false;

		for(final MethodNode method : methods) {
			if(method.name.equals(TFTranslator.getMappedName("a", "renderLivingAt")) && method.desc.equals(TFTranslator.getMappedName("(Lsv;DDD)V", "(Lnet/minecraft/entity/EntityLivingBase;DDD)V"))) {
				final InsnList list = new InsnList();

				for(int i = 0; i < method.instructions.size(); ++i) {
					final AbstractInsnNode node = method.instructions.get(i);

					if(node instanceof MethodInsnNode) {
						final MethodInsnNode methodNode = (MethodInsnNode) node;

						if(methodNode.getOpcode() == Opcodes.INVOKEVIRTUAL && methodNode.desc.equals(TFTranslator.getMappedName("(Lblg;DDD)V", "(Lnet/minecraft/client/entity/AbstractClientPlayer;DDD)V"))) {
							list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ASMHooksClient.class), "applyPlayerRenderTranslation", TFTranslator.getMappedName("(Lbop;Lblg;DDD)V", "(Lnet/minecraft/client/renderer/entity/RenderPlayer;Lnet/minecraft/client/entity/AbstractClientPlayer;DDD)V"), false));
							continue;
						}
					}

					list.add(node);
				}

				method.instructions.clear();
				method.instructions.add(list);
				flag = true;
			}
		}

		return flag;
	}

	@Override
	public boolean processFields(final List<FieldNode> fields) {
		return true;
	}

	@Override
	public void setupMappings() {}
}
