// light_fragment.glsl
#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;
uniform sampler2D u_texture;

uniform vec2 u_lightPos;  // In screen/world coords (adjust as needed)
uniform float u_radius;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoord);

    float dist = distance(gl_FragCoord.xy, u_lightPos);
    float factor = clamp(1.0 - (dist / u_radius), 0.0, 1.0);

    gl_FragColor = texColor * (0.8 + 0.7 * factor); // Outside: dark (30%), Inside: full brightness
}
