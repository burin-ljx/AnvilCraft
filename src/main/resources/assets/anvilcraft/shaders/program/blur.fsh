#version 150

uniform sampler2D DiffuseSampler;

uniform vec2 InSize;
uniform vec2 BlurDir;
uniform float weight[6] = float[] (0.227027, 0.1945946, 0.1216216, 0.054054, 0.016216, 0.0111);

in vec2 texCoord;
in vec2 texelSize;

out vec4 fragColor;

void main() {
    vec2 texOffset = 1.0 / textureSize(DiffuseSampler, 0);

    vec3 result = texture(DiffuseSampler, texCoord).rgb * weight[0];
    bool horizontal = BlurDir.x == 1.0;
    if (horizontal) {
        for (int i = 1; i < 6; ++i) {
            result += texture(DiffuseSampler, texCoord + vec2(texOffset.x * i, 0.0)).rgb * weight[i];
            result += texture(DiffuseSampler, texCoord - vec2(texOffset.x * i, 0.0)).rgb * weight[i];
        }
    } else {
        for (int i = 1; i < 6; ++i) {
            result += texture(DiffuseSampler, texCoord + vec2(0.0, texOffset.y * i)).rgb * weight[i];
            result += texture(DiffuseSampler, texCoord - vec2(0.0, texOffset.y * i)).rgb * weight[i];
        }
    }
    fragColor = vec4(result.rgb * 1.105, 1.0);
}