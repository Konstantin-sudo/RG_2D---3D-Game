package objects;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

public class DistanceMap extends Group {
    //private static final double CIRCLE_HEIGHT = 0;
    private static final float CIRCLE_THICKNESS = 2;
    private static final int NU = 50;
    private static final int NP = 25;
    private static final Color CIRCLE_COLOR = Color.RED;
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    PixelWriter pixelWriter;

    public DistanceMap( double[] circleRadius , double ocean_height, Color circleColor)
    {
        for(int i = 0; i < circleRadius.length; ++i)
        {
            MeshView circle = createTorus( NU, NP, (float)circleRadius[i], CIRCLE_THICKNESS/2 );
            circle.setMaterial( new PhongMaterial(circleColor) );
            circle.getTransforms().addAll(
                    new Translate(0, -(ocean_height - CIRCLE_THICKNESS/2),0),
                    new Rotate(-90, Rotate.X_AXIS)
            );
            super.getChildren().addAll(circle);
        }
    }

    private MeshView createTorus(int nU, int nP, float R, float r)
    {
        float[] points = new float[nP * nU * 3];
        float[] texCoords = {
                0.1f, 0.5f,
                0.3f, 0.5f,
                0.5f, 0.5f,
                0.7f, 0.5f,
                0.9f, 0.5f
        };
        int[] faces = new int[nP * nU * 12];
        int iV = 0;
        int iT = 0;
        int iS = 0;
        int p0,p1,p2,p3;
        int p;
        int t0 = 0;
        int t1 = 0;
        int t2 = 0;
        int t3 = 0;

        //points
        for(int iP = 0; iP < nP; ++iP)
        {
            float fi = iP * 2.0f * (float)Math.PI / nP;
            for(int iU = 0; iU < nU; ++iU)
            {
                float teta = iU * 2.0f * (float)Math.PI/nU;
                points[iV++] = (float)( (R + r*Math.cos(fi)) * Math.cos(teta) );
                points[iV++] = (float)( (R + r*Math.cos(fi)) * Math.sin(teta) );
                points[iV++] = (float)( r * Math.sin(fi) );
            }
        }

        //faces
        for(int i = 0; i < nP; ++i)
        {
            for(int j = 0; j < nU; ++j)
            {
                p0 = i * nU + j;

                p = p0 + 1;
                p1 = p % nU != 0 ? p : p-nU;

                p = p0 + nU;
                p2 = p < nP * nU ? p : j;

                p = p2 + 1;
                p3 = p % nU != 0 ? p : p - nU;

                t0 = t1 = t2 = t3 = iT;

                faces[iS++] = p0;
                faces[iS++] = t0;
                faces[iS++] = p1;
                faces[iS++] = t1;
                faces[iS++] = p2;
                faces[iS++] = t2;

                faces[iS++] = p3;
                faces[iS++] = t3;
                faces[iS++] = p2;
                faces[iS++] = t2;
                faces[iS++] = p1;
                faces[iS++] = t1;
            }
            iT = (iT + 1) % (texCoords.length / 2);
        }

        TriangleMesh triangleMesh = new TriangleMesh();
        triangleMesh.getPoints().addAll(points);
        triangleMesh.getTexCoords().addAll(texCoords);
        triangleMesh.getFaces().addAll(faces);

        MeshView meshView = new MeshView();
        meshView.setMesh(triangleMesh);
        return meshView;
    }

}
