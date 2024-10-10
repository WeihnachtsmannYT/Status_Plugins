#version 150

uniform sampler2D gcolor;// The color buffer
uniform vec2 resolution;// The screen resolution

void main() {
    vec2 uv = gl_FragCoord.xy / resolution;// Normalize the coordinates
    vec2 center = vec2(0.5, 0.5);// Center of the screen
    float radius = 0.3;// Circle radius

    // Calculate distance from the center of the screen
    float dist = distance(uv, center);

    // Draw the red circle
    if (dist < radius) {
        gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);// Red color
    } else {
        gl_FragColor = texture2D(gcolor, uv);// Default color from the screen
    }
}