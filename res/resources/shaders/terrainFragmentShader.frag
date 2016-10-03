#version 330 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;

out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;
uniform sampler2DShadow shadowMap;

uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;
uniform float mapSize;

const int pcfCount = 2;
const float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);

void main(void) {

	//PCF shadow mapping
	float texelSize = 1 / mapSize;
	float total = 0.0;
	
	for(int x=-pcfCount ; x<=pcfCount; x++) {
		for(int y=-pcfCount ; y<=pcfCount; y++) {
			float objectNearestLight = 1.0 - texture(shadowMap, shadowCoords.xyz + vec3(0.0, 0.0, -0.003) + vec3(x, y, 0.0) * texelSize);
			total += objectNearestLight;
		}
	}
	
	total /= totalTexels;
	
	//light factor for shadow mapping
	float lightFactor = 1.0 - (total * shadowCoords.w);

	//blend map texturing
	vec4 blendMapColour = texture(blendMap, pass_textureCoords);
	
	float backTextureAmount = 1 - (blendMapColour.r, blendMapColour.g, blendMapColour.b);
	vec2 tiledCoords = pass_textureCoords * 40.0;
	vec4 backgroundTextureColour = texture(backgroundTexture, tiledCoords) * backTextureAmount;
	vec4 rTextureColour = texture(rTexture, tiledCoords) * blendMapColour.r;
	vec4 gTextureColour = texture(gTexture, tiledCoords) * blendMapColour.g;
	vec4 bTextureColour = texture(bTexture, tiledCoords) * blendMapColour.b;
	
	vec4 totalColour = backgroundTextureColour + rTextureColour + gTextureColour + bTextureColour;

	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	float brightness = dot(unitNormal, unitLightVector);
	
	vec3 diffuse = max(brightness * lightColour * lightFactor, 0.2);

	vec3 unitToCameraVector = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDir = reflect(lightDirection, unitNormal);
	
	float specularFactor = dot(reflectedLightDir, unitToCameraVector);
	specularFactor = max(0.0, specularFactor);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColour;

	out_Color = vec4(diffuse, 1.0) * totalColour + vec4(finalSpecular, 1.0);
	out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
	
}