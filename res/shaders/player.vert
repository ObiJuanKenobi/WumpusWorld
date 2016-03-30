#version 330 core
#extension GL_ARB_explicit_attrib_location : require
#extension GL_ARB_uniform_buffer_object : require

layout (location = 1) in vec4 position;
layout (location = 2) in vec2 tc;

uniform mat4 ml_matrix = mat4(1.0);

out DATA {
	vec2 tc;
} vs_out;

void main() {
	gl_Position = ml_matrix * position;
	vs_out.tc = tc;
}