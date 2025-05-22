#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_shadowCoords; // Received from vertex shader
uniform sampler2D u_texture;
uniform vec4 u_shadowColor; // RGBA shadow color (1,0,0,0.7 for red shadow)

void main() {
    // Sample the shadow first (using offset coordinates)
    vec4 shadow = texture2D(u_texture, v_shadowCoords) * u_shadowColor;

    // Then sample the main sprite
    vec4 sprite = texture2D(u_texture, v_texCoords) * v_color;

    // Combine with shadow behind (using alpha blending)
    gl_FragColor = mix(shadow, sprite, sprite.a);
}
