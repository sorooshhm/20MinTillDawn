attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform vec2 u_shadowOffset; // Added for shadow effect

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec2 v_shadowCoords; // Added to pass shadow texture coordinates

void main() {
    v_color = a_color;
    v_texCoords = a_texCoord0;
    v_shadowCoords = a_texCoord0 + u_shadowOffset; // Calculate shadow coordinates
    gl_Position = u_projTrans * a_position;
}
