package net.mehvahdjukaar.supplementaries.mixins;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.mehvahdjukaar.supplementaries.api.IAntiqueTextProvider;
import net.mehvahdjukaar.supplementaries.common.capabilities.SupplementariesCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public abstract class TileEntityRendererDispatcherMixin{


    @Shadow public FontRenderer font;

    @Shadow @Final public static TileEntityRendererDispatcher instance;

    private static boolean antiqueFontActive;
    private static final IAntiqueTextProvider FONT = (IAntiqueTextProvider)(Minecraft.getInstance().font);

    @Inject(method = "setupAndRender", at = @At("HEAD"))
    private static <T extends TileEntity> void setupAndRenderPre(TileEntityRenderer<T> renderer, T tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, CallbackInfo ci) {
        tile.getCapability(SupplementariesCapabilities.ANTIQUE_TEXT_CAP).ifPresent(c->{
            if(c.hasAntiqueInk()){
                FONT.setAntiqueInk(true);
                antiqueFontActive = true;
            }
        });
        FONT.setAntiqueInk(true);
    }


    @Inject(method = "setupAndRender", at = @At("TAIL"))
    private static <T extends TileEntity> void setupAndRenderPost(TileEntityRenderer<T> renderer, T tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, CallbackInfo ci) {
        if(antiqueFontActive){
            FONT.setAntiqueInk(false);
            antiqueFontActive = false;
        }
    }
}