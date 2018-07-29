image {
	resolution 512 512
	aa 0 2
	filter gaussian
}

trace-depths {
	diff 1
	refl 0
	refr 0
}

% |persp|perspShape
camera {
	type   pinhole
	eye    -5 0 0
	target 0 0 0
	up     0 1 0
	fov    58
	aspect 1
}

% background { color { "sRGB nonlinear" 0.5 0.5 0.5 } }

gi { type path samples 16 }

shader {
  name simple1
  type diffuse
  diff { "sRGB nonlinear" 0.5 0.5 0.5 }
}

light {
  type spherical
  color { "sRGB nonlinear" 1 1 .6 }
  radiance 60
  center -5 7 5
  radius 2
  samples 8
}

light {
  type spherical
  color { "sRGB nonlinear"  .6 .6 1 }
  radiance 20
  center -15 -17 -15
  radius 5
  samples 8
}

object {
	shader simple1
	type generic-mesh
	name "Cube"
	points 4
		0.0 0.0 0.0
		1.0 0.0 0.0
		1.0 1.0 0.0
		0.0 1.0 0.0
	faces 1
		0 1 2 3
        0 1 2 3
	normals vertex
		0.0 0.0 1.0
		0.0 0.0 1.0
		0.0 0.0 1.0
		0.0 0.0 1.0
	uvs none
}
