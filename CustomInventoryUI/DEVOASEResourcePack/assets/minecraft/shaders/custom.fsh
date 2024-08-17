#version 120

uniform sampler2D texture;// The texture you're animating
uniform float time;// The elapsed time, passed by the game

void main() {
    vec2 uv = gl_TexCoord[0].xy;

    // Define the size of each frame (for example, 1/5th of the texture height)
    float frameHeight = 1.0 / 5.0;

    // Calculate the current frame based on time
    float currentFrame = mod(time * 5.0, 5.0);// 5 frames in the texture

    // Offset the UV coordinates to select the correct frame
    uv.y = uv.y / 5.0 + frameHeight * currentFrame;

    // Output the final color
    gl_FragColor = texture2D(texture, uv);
}
