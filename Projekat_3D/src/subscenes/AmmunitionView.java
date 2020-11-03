package subscenes;

import constants.Constants;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public class AmmunitionView extends SubScene {
    private static final double BULLET_RADIUS = 2.5;
    private static final double BULLET_BODY_HEIGHT = BULLET_RADIUS * 5;
    private static final double BULLET_HEAD_HEIGHT = BULLET_RADIUS * 2;

    private Group myRoot;
    private List<Group> ammunition = new ArrayList<>();

    public AmmunitionView(Group myRoot, double v, double v1, boolean b, SceneAntialiasing sceneAntialiasing, int ammunition, double dx, double dy) {
        super(myRoot, v, v1, b, sceneAntialiasing);
        super.setCursor ( Cursor.NONE );
        this.myRoot = myRoot;

        //BULLETS
        for(int i = 0; i < ammunition; ++i)
        {
            double start_distance = Constants.AMMUNITION_VIEW_SUB_SCENE_WIDTH - BULLET_RADIUS * 2 * 3 ;
            Group newBullet = createBullet();
            newBullet.getTransforms().addAll(
                    new Translate(start_distance - BULLET_RADIUS * 2 * 3 * i,0,0)
            );
            myRoot.getChildren().addAll(newBullet);
            this.ammunition.add(newBullet);
        }

        //LIGHTS
        AmbientLight light = new AmbientLight();
        this.myRoot.getChildren().addAll(light);

        //CAMERA
        Camera camera = new ParallelCamera();
        camera.setNearClip(Constants.CAMERA_NEAR_CLIP);
        camera.setFarClip(Constants.CAMERA_FAR_CLIP);
        super.setCamera(camera);

        //POSITION
        this.getTransforms().addAll(
                new Translate(dx, dy,0)
        );
    }

    public void addBullets(int numberOfBullets)
    {
        double startDistance = Constants.AMMUNITION_VIEW_SUB_SCENE_WIDTH - BULLET_RADIUS * 2 * 3 - this.ammunition.size() * BULLET_RADIUS * 2 * 3;
        for(int i = 0; i < numberOfBullets; ++i)
        {
            Group newBullet = createBullet();
            newBullet.getTransforms().addAll(
                    new Translate(startDistance - BULLET_RADIUS * 2 * 3 * i,0,0)
            );
            myRoot.getChildren().addAll(newBullet);
            this.ammunition.add(newBullet);
        }
    }

    public void removeBullets(int numberOfBullets)
    {
        for( int i = 0; i < numberOfBullets; ++i)
        {
            if(this.ammunition.size() == 0) return;
            int indexOfLastBullet = this.ammunition.size() - 1;
            Group bullet = this.ammunition.get(indexOfLastBullet);
            this.myRoot.getChildren().remove(bullet);
            this.ammunition.remove(bullet);
        }
    }

    private Group createBullet()
    {
        Group ret = new Group();

        Cylinder body = new Cylinder(BULLET_RADIUS, BULLET_BODY_HEIGHT);
        body.setMaterial(new PhongMaterial(Color.YELLOW));
        body.getTransforms().add(
                new Translate(0, BULLET_HEAD_HEIGHT+BULLET_BODY_HEIGHT/2,0)
        );
        ret.getChildren().addAll(body);

        float[] points = {
                            0,           0, 0,
                (float)BULLET_RADIUS,    (float)(BULLET_HEAD_HEIGHT), 0,
                            0,           (float)(BULLET_HEAD_HEIGHT), -(float)(BULLET_RADIUS),
                -(float)(BULLET_RADIUS), (float)(BULLET_HEAD_HEIGHT), 0,
                            0,           (float)(BULLET_HEAD_HEIGHT), (float)BULLET_RADIUS

        };
        float[] texCoords = {
                0.5f, 0.5f,
                1f, 0.5f,
                0.5f, 0f,
                0f, 0.5f,
                0.5f, 1f
        };
        int[] faces = {
                0,0,1,1,2,2,
                0,0,2,2,1,1,
                0,0,2,2,3,3,
                0,0,3,3,2,2,
                0,0,3,3,4,4,
                0,0,4,4,3,3,
                0,0,4,4,1,1,
                0,0,1,1,4,4,
                1,1,2,2,4,4,
                1,1,4,4,2,2,
                2,2,3,3,4,4,
                2,2,4,4,3,3,
        };
        TriangleMesh triangleMesh = new TriangleMesh();
        triangleMesh.getPoints().addAll(points);
        triangleMesh.getTexCoords().addAll(texCoords);
        triangleMesh.getFaces().addAll(faces);

        MeshView bulletHead = new MeshView();
        bulletHead.setMesh(triangleMesh);
        bulletHead.setMaterial(new PhongMaterial(Color.YELLOW));
        bulletHead.getTransforms().add(
                new Rotate(45,Rotate.Y_AXIS)
        );
        ret.getChildren().addAll(bulletHead);

        return ret;
    }

}
