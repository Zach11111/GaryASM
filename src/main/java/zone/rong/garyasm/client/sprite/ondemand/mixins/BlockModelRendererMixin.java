package zone.rong.garyasm.client.sprite.ondemand.mixins;

import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zone.rong.garyasm.client.sprite.ondemand.IAnimatedSpritePrimer;
import zone.rong.garyasm.client.sprite.ondemand.ICompiledChunkExpander;

import java.util.List;

@Mixin(BlockModelRenderer.class)
public class BlockModelRendererMixin {

    @Redirect(method = "renderQuadsSmooth", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", remap = false))
    private Object sendAnimatedSprites$smooth(List list, int i) {
        BakedQuad bakedquad = (BakedQuad) list.get(i);
        if (bakedquad.getSprite().hasAnimationMetadata()) {
            CompiledChunk chunk = IAnimatedSpritePrimer.CURRENT_COMPILED_CHUNK.get();
            if (chunk != null) {
                ((ICompiledChunkExpander) chunk).resolve(bakedquad.getSprite());
            }
        }
        return bakedquad;
    }

    @SuppressWarnings("all")
    @Redirect(method = "renderQuadsFlat", at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0))
    private Object sendAnimatedSprites$flat(List<BakedQuad> list, int i) {
        BakedQuad bakedquad = list.get(i);
        if (bakedquad.getSprite().hasAnimationMetadata()) {
            CompiledChunk chunk = IAnimatedSpritePrimer.CURRENT_COMPILED_CHUNK.get();
            if (chunk != CompiledChunk.DUMMY) {
                ((ICompiledChunkExpander) chunk).resolve(bakedquad.getSprite());
            }
        }
        return bakedquad;
    }

}
