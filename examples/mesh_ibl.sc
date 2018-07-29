image {
  resolution 640 480
  aa 0 1
  filter mitchell
}

camera {
  type pinhole
  eye    -10.5945 -30.0581 10.967
  target 0.0554193 0.00521195 5.38209
  up     0 0 1
  fov    60
  aspect 1.333333
}

light {
  type ibl
  image sky_small.hdr
  center 0 -1 0
  up 0 0 1
  lock true
  samples 200
}

shader {
  name default-shader
  type diffuse
  diff 0.25 0.25 0.25
}

object {
	shader default-shader
	type generic-mesh
	name cube
	points 4
		0.0 0.0 0.0
		100.0 0.0 0.0
		100.0 0.0 100.0
		0.0 0.0 100.0
	faces 1
		0 1 2 3
	normals vertex
		0.0 0.0 1.0
		0.0 0.0 1.0
		0.0 0.0 1.0
		0.0 0.0 1.0
	uvs none
}

object {
  shader default-shader
  type plane
  p 0 0 0
  n 0 0 1
}

shader {
  name Glass
  type glass
  eta 1.6
  color 1 1 1
}

shader {
  name Mirror
  type mirror
  refl 0.7 0.7 0.7
}

object {
  shader Glass
  type sphere
  c 12 0 5
  r 3
}


object {
  shader Mirror
  type sphere
  c -13 0 5
  r 3
}
