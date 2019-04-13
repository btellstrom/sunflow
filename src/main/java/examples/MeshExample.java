package examples;

import org.sunflow.SunflowAPI;
import org.sunflow.core.parameter.ImageParameter;
import org.sunflow.core.parameter.InstanceParameter;
import org.sunflow.core.parameter.PhotonParameter;
import org.sunflow.core.parameter.TraceDepthsParameter;
import org.sunflow.core.parameter.camera.PinholeCameraParameter;
import org.sunflow.core.parameter.geometry.*;
import org.sunflow.core.parameter.gi.InstantGIParameter;
import org.sunflow.core.parameter.light.CornellBoxLightParameter;
import org.sunflow.core.parameter.shader.DiffuseShaderParameter;
import org.sunflow.core.parameter.shader.GlassShaderParameter;
import org.sunflow.core.parameter.shader.MirrorShaderParameter;
import org.sunflow.image.Color;
import org.sunflow.math.Matrix4;
import org.sunflow.math.Point3;
import org.sunflow.math.Vector3;

public class MeshExample {

    public static void main(String[] args) {
        SunflowAPI api = new SunflowAPI();
        api.reset();

        ImageParameter image = new ImageParameter();
        image.setResolutionX(400);
        image.setResolutionY(300);
        image.setAAMin(0);
        image.setAAMax(1);
        image.setFilter(ImageParameter.FILTER_GAUSSIAN);
        image.setup(api);

        TraceDepthsParameter traceDepths = new TraceDepthsParameter();
        traceDepths.setDiffuse(4);
        traceDepths.setReflection(3);
        traceDepths.setRefraction(2);
        traceDepths.setup(api);

        PhotonParameter photons = new PhotonParameter();
        photons.setNumEmit(1000000);
        photons.setCaustics("kd");
        photons.setCausticsGather(100);
        photons.setCausticsRadius(0.5f);
        photons.setup(api);

        InstantGIParameter gi = new InstantGIParameter();
        gi.setSamples(64);
        gi.setSets(1);
        gi.setBias(0.00003f);
        gi.setBiasSamples(0);
        gi.setup(api);

        PinholeCameraParameter camera = new PinholeCameraParameter();

        camera.setName("camera");
        Point3 eye = new Point3(0, -205, 50);
        Point3 target = new Point3(0, 0, 50);
        Vector3 up = new Vector3(0, 0, 1);
        // TODO Move logic to camera
        api.parameter("transform", Matrix4.lookAt(eye, target, up));

        camera.setFov(45f);
        camera.setAspect(1.333333f);
        camera.setup(api);

        // Materials
        MirrorShaderParameter mirror = new MirrorShaderParameter("Mirror");
        mirror.setReflection(new Color(0.7f, 0.7f, 0.7f));
        mirror.setup(api);

        GlassShaderParameter glass = new GlassShaderParameter("Glass");
        glass.setEta(1.6f);
        glass.setAbsorptionColor(new Color(1, 1, 1));
        glass.setup(api);

        // Lights
        CornellBoxLightParameter lightParameter = new CornellBoxLightParameter();
        lightParameter.setName("cornell-box-light");
        lightParameter.setMin(new Point3(-60, -60, 0));
        lightParameter.setMax(new Point3(60, 60, 100));
        lightParameter.setLeft(new Color(0.8f, 0.25f, 0.25f));
        lightParameter.setRight(new Color(0.25f, 0.25f, 0.8f));
        lightParameter.setTop(new Color(0.7f, 0.7f, 0.7f));
        lightParameter.setBottom(new Color(0.7f, 0.7f, 0.7f));
        lightParameter.setBack(new Color(0.7f, 0.7f, 0.7f));
        lightParameter.setRadiance(new Color(15, 15, 15));
        lightParameter.setSamples(32);
        lightParameter.setup(api);

        InstanceParameter mirrorSphereInstance = new InstanceParameter();
        mirrorSphereInstance.shaders("Mirror");

        SphereParameter mirrorSphere = new SphereParameter();
        mirrorSphere.setName("mirror-sphere");
        mirrorSphere.setCenter(new Point3(30, 30, 20));
        mirrorSphere.setInstanceParameter(mirrorSphereInstance);
        mirrorSphere.setRadius(20);
        // Hide Sphere
        mirrorSphere.setup(api);

        SphereParameter otherSphere = new SphereParameter();
        otherSphere.setName("other-sphere");
        otherSphere.setCenter(new Point3(-30, 30, 50));
        otherSphere.setInstanceParameter(mirrorSphereInstance);
        otherSphere.setRadius(20);
        // Hide Sphere
        otherSphere.setup(api);

        DiffuseShaderParameter simpleYellow = new DiffuseShaderParameter("simple_yellow");
        simpleYellow.setDiffuse(new Color(0.70f, 0.70f, 0.15f).toNonLinear());
        simpleYellow.setup(api);

        SphereFlakeParameter sphereFlakeParameter = new SphereFlakeParameter("left");
        sphereFlakeParameter.setInstanceParameter(new InstanceParameter().shaders("simple_yellow"));
        sphereFlakeParameter.setRadius(20);
        sphereFlakeParameter.setLevel(6);
        sphereFlakeParameter.setup(api);

        /*InstanceParameter glassSphereInstance = new InstanceParameter();
        glassSphereInstance.setShaders(new String[]{"Glass"});
        SphereParameter glassSphere = new SphereParameter();
        glassSphere.setName("glass-sphere");
        glassSphere.setCenter(new Point3(28, 2, 20));
        glassSphere.setInstanceParameter(glassSphereInstance);
        glassSphere.setRadius(20);
        glassSphere.setup(api);*/

        float y = 30;

        PlaneParameter plane = new PlaneParameter();
        plane.setName("my_plane");
        plane.setCenter(new Point3(28,30,20));
        plane.setPoint1(new Point3(300,60,50));
        plane.setPoint2(new Point3(0,0,-1));
        plane.setNormal(new Vector3(0, 0, 1));
        //plane.setInstanceParameter(mirrorSphereInstance);
        plane.setup(api);

        GenericMeshParameter mesh = new GenericMeshParameter();
        mesh.setName("triangularMesh");
        mesh.setPoints(new float[]{-30, y, 0, 0, y, 100, 500, y, 0});
        mesh.setUvs(new float[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        mesh.setTriangles(new int[]{0, 1, 2});
        mesh.setNormals(new float[]{0, -1, 0, 0, -1, 0, 0, -1, 0});

        mesh.setInstanceParameter(mirrorSphereInstance);
        mesh.setup(api);

        finalRender(api);
    }

    private static void previewRender(SunflowAPI api) {
        api.parameter("sampler", "ipr");
        api.options(SunflowAPI.DEFAULT_OPTIONS);
        api.render(SunflowAPI.DEFAULT_OPTIONS, null);
    }

    private static void finalRender(SunflowAPI api) {
        api.parameter("sampler", "bucket");
        api.options(SunflowAPI.DEFAULT_OPTIONS);
        api.render(SunflowAPI.DEFAULT_OPTIONS, null);
    }

}
