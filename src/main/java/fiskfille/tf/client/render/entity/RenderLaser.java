package fiskfille.tf.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import fiskfille.tf.client.model.ModelLaser;
import fiskfille.tf.TransformersMod;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderLaser extends Render
{
    public final ModelLaser model = new ModelLaser();
    public final ResourceLocation texture = new ResourceLocation(TransformersMod.modid, "textures/models/weapons/laser.png");

    @Override
    public void doRender(Entity entity, double x, double y, double z, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9 + 180, 0F, 1F, 0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9, 1F, 0F, 0F);
        bindEntityTexture(entity);

        model.render(entity, 0, 0, 0, 0, 0, 0.0625F);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return texture;
    }
}
