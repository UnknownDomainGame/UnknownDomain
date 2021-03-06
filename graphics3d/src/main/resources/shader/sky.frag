#version 420 core

uniform sampler2D u_Texture;

layout (location = 0) in vec4 v_Color;
layout (location = 1) in vec2 v_TexCoord;

out vec4 fragColor;

void main() {
    fragColor = v_Color * texture(u_Texture, v_TexCoord);
}