#version 150

in vec2 texCoord;

uniform sampler2D DiffuseSampler;
uniform sampler2D BloomSampler;
uniform vec2 InSize;
uniform float BloomIntensity;

out vec4 fragColor;

void main() {
    vec4 colorGame = texture(DiffuseSampler, texCoord);
    vec3 color = colorGame.rgb;
    vec4 bloom4 = texture(BloomSampler, texCoord);
    vec3 bloom = bloom4.rgb * BloomIntensity;
    vec3 finalColor = color + (bloom * pow(0.08, length(color) * 0.8));
    fragColor = vec4(finalColor, colorGame.a + bloom4.a);
}
