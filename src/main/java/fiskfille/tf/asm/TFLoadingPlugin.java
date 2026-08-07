package fiskfille.tf.asm;

import cpw.mods.fml.relauncher.FMLLaunchHandler;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.asm.transformers.ClassTransformerEntity;
import fiskfille.tf.asm.transformers.ClassTransformerGuiContainer;
import fiskfille.tf.asm.transformers.ClassTransformerModelBiped;
import fiskfille.tf.asm.transformers.ClassTransformerRenderPlayer;

import java.util.Map;

@SideOnly(Side.CLIENT)
@MCVersion("1.7.10")
@TransformerExclusions("fiskfille.tf.asm")
public class TFLoadingPlugin implements IFMLLoadingPlugin {
	public static boolean loaded;

	@Override
	public String[] getASMTransformerClass() {
		if(FMLLaunchHandler.side().isClient()) {
			return new String[]{ClassTransformerRenderPlayer.class.getName(), ClassTransformerEntity.class.getName(), ClassTransformerModelBiped.class.getName(), ClassTransformerGuiContainer.class.getName()};
		}

		return null;
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(final Map<String, Object> data) {
		TFTranslator.obfuscatedEnv = (Boolean) data.get("runtimeDeobfuscationEnabled");
		loaded = true;
	}
}
