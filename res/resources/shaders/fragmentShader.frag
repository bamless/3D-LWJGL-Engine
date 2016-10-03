#version 330 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform sampler2DShadow shadowMap;

uniform vec3 lightColour;
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColour;
uniform float mapSize;

const float minLum = 0.1456;
const int pcfCount = 2;
const float totalTexels = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);

//Computes the pixel diffuse lighting
//@returns the diffuse pixel color
vec3 compute_diffuse(vec3 unitNormal, vec3 unitLightVector) {
	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, 0.1386);
	return max(brightness * lightColour, minLum);
}

//Computes the pixel specular lighting
//@returns the specular light color
vec3 compute_specular(vec3 unitNormal, vec3 unitLightVector, vec3 toCameraVector) {
	vec3 unitToCameraVector = normalize(toCameraVector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDir = reflect(lightDirection, unitNormal);
	
	float specularFactor = dot(reflectedLightDir, unitToCameraVector);
	specularFactor = max(0.0, specularFactor);
	float dampedFactor = pow(specularFactor, shineDamper);
	return dampedFactor * reflectivity * lightColour;
}

//Performs a lookup into the shadowmap depth buffer using HW PCF and a kernel of 4.
//@arg bias - the bias to be added in the lookup in order to prevent shadow acne (too much will result in peter panning)
//@arg distance - used to make shadow disappear smoothly at the esge of the shadow distance
//@returns a value beetwen 0 and 1 indicating how much the pixel is in shade.
float compute_shadow_mapping(float bias, float distance) {
	float texelSize = 1 / mapSize;
	float total = 0.0;
	
	for(int x=-pcfCount ; x<=pcfCount; x++) {
		for(int y=-pcfCount ; y<=pcfCount; y++) {
			float occludedPercentage = 1.0 - texture(shadowMap, shadowCoords.xyz + vec3(0.0, 0.0, -bias) + vec3(x, y, 0.0) * texelSize);
			total += occludedPercentage;
		}
	}
	
	total /= totalTexels;
	return 1.0 - (total * distance);
}

void main(void) {
	
	//normalizes the surface normal and the pixel-to-light vector
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitLightVector = normalize(toLightVector);
	
	//computing diffuse lighting
	vec3 diffuse = compute_diffuse(unitNormal, unitLightVector);

	//copmputing specular lighting
	vec3 specular = compute_specular(unitNormal, unitLightVector, toCameraVector);
	
	//computing shadowmap light factor
	float lightFactor = compute_shadow_mapping(0.003, shadowCoords.w);
	
	//scaling the diffuse luminescence according to the shadpw mapping light factor
	diffuse = max(diffuse * lightFactor, minLum);
	
	//scaling the specular luminescence according to the shadw mapping light factor
	specular *= lightFactor;
	
	//sampling the texure
	vec4 textureColour = texture(textureSampler, pass_textureCoords);
	//if the pixel is transparent discard
	if(textureColour.a < 0.5) {
		discard;
	}

	//mixing all the lighting
	out_Color = vec4(diffuse, 1.0) * textureColour + vec4(specular, 1.0);
	//creating fog effect for object in the distance
	out_Color = mix(vec4(skyColour, 1.0), out_Color, visibility);
	
}