package engine.graphics.vulkan.texture;

import engine.graphics.texture.TextureFormat;
import org.lwjgl.vulkan.VK10;

public enum ColorFormat {
    UNDEFINED(VK10.VK_FORMAT_UNDEFINED, null),
    BGRA_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_B8G8R8A8_UNORM, TextureFormat.BGRA8),
    RGBA_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8G8B8A8_UNORM, TextureFormat.RGBA8),
    BGRA_NORMALIZED(VK10.VK_FORMAT_B8G8R8A8_SNORM, TextureFormat.BGRA8),
    RGBA_NORMALIZED(VK10.VK_FORMAT_R8G8B8A8_SNORM, TextureFormat.RGBA8),
    BGRA_UINT(VK10.VK_FORMAT_B8G8R8A8_UINT, TextureFormat.BGRA8),
    RGBA_UINT(VK10.VK_FORMAT_R8G8B8A8_UINT, TextureFormat.RGBA8),
    BGRA_INT(VK10.VK_FORMAT_B8G8R8A8_SINT, TextureFormat.BGRA8),
    RGBA_INT(VK10.VK_FORMAT_R8G8B8A8_SINT, TextureFormat.RGBA8),
    BGRA_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8A8_USCALED, TextureFormat.BGRA8),
    RGBA_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8A8_USCALED, TextureFormat.BGRA8),
    BGRA_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8A8_SSCALED, TextureFormat.BGRA8),
    RGBA_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8A8_SSCALED, TextureFormat.RGBA8),
    R8_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8_UNORM, TextureFormat.RED8),
    R8_NORMALIZED(VK10.VK_FORMAT_R8_SNORM, TextureFormat.RED8),
    R8_UINT(VK10.VK_FORMAT_R8_UINT, TextureFormat.RED8UI),
    R8_INT(VK10.VK_FORMAT_R8_SINT, TextureFormat.RED8),
    R8_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8_USCALED, TextureFormat.RED8),
    R8_SCALED_FLOAT(VK10.VK_FORMAT_R8_SSCALED, TextureFormat.RED8),
    R8G8_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8G8_UNORM, TextureFormat.RG8),
    R8G8_NORMALIZED(VK10.VK_FORMAT_R8G8_SNORM, TextureFormat.RG8),
    R8G8_UINT(VK10.VK_FORMAT_R8G8_UINT, TextureFormat.RG8),
    R8G8_INT(VK10.VK_FORMAT_R8G8_SINT, TextureFormat.RG8),
    R8G8_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8G8_USCALED, TextureFormat.RG8),
    R8G8_SCALED_FLOAT(VK10.VK_FORMAT_R8G8_SSCALED, TextureFormat.RG8),
    RGB_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_R8G8B8_UNORM, TextureFormat.RGB8),
    RGB_NORMALIZED(VK10.VK_FORMAT_R8G8B8_SNORM, TextureFormat.RGB8),
    RGB_UINT(VK10.VK_FORMAT_R8G8B8_UINT, TextureFormat.RGB8),
    RGB_INT(VK10.VK_FORMAT_R8G8B8_SINT, TextureFormat.RGB8),
    RGB_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8_USCALED, TextureFormat.RGB8),
    RGB_SCALED_FLOAT(VK10.VK_FORMAT_R8G8B8_SSCALED, TextureFormat.RGB8),
    BGR_UNSIGNED_NORMALIZED(VK10.VK_FORMAT_B8G8R8_UNORM, TextureFormat.BGR8),
    BGR_NORMALIZED(VK10.VK_FORMAT_B8G8R8_SNORM, TextureFormat.BGR8),
    BGR_UINT(VK10.VK_FORMAT_B8G8R8_UINT, TextureFormat.BGR8),
    BGR_INT(VK10.VK_FORMAT_B8G8R8_SINT, TextureFormat.BGR8),
    BGR_UNSIGNED_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8_USCALED, TextureFormat.BGR8),
    BGR_SCALED_FLOAT(VK10.VK_FORMAT_B8G8R8_SSCALED, TextureFormat.BGR8),

    DEPTH_32(VK10.VK_FORMAT_D32_SFLOAT, TextureFormat.DEPTH32),
    DEPTH_32_STENCIL_8(VK10.VK_FORMAT_D32_SFLOAT_S8_UINT, null),
    DEPTH_24_STENCIL_8(VK10.VK_FORMAT_D24_UNORM_S8_UINT, TextureFormat.DEPTH24_STENCIL8),
    DEPTH_16_STENCIL_8(VK10.VK_FORMAT_D16_UNORM_S8_UINT, null),
    DEPTH_16(VK10.VK_FORMAT_D16_UNORM, null);
    private final int vk;
    private final TextureFormat peer;

    ColorFormat(int vk, TextureFormat peer){
        this.vk = vk;
        this.peer = peer;
    }

    public int getVk() {
        return vk;
    }

    public TextureFormat getPeer() {
        return peer;
    }

    public static ColorFormat fromVkFormat(int vk){
        for (ColorFormat value : values()) {
            if(value.vk == vk) {
                return value;
            }
        }
        return UNDEFINED;
    }
}
