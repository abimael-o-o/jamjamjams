package com.mygdx.game.EDITOR;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.spinner.FloatSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;
import com.mygdx.game.helpers.GameObject;

public class ObjectSetState extends VisWindow{

    public ObjectSetState(final GameObject instance){
        super("Instance " + instance.model.nodes.first().id);
        setPosition(800, 500);

        //*Set position for instance*/
        VisTable positionTable = new VisTable(true);
        //MODELS
        final FloatSpinnerModel xModel = new FloatSpinnerModel("0", "-999", "999");
        final FloatSpinnerModel yModel = new FloatSpinnerModel("0", "-999", "999");
        final FloatSpinnerModel zModel = new FloatSpinnerModel("0", "-999", "999");
        //TABLE
        positionTable.add(new Spinner("X", xModel));
        positionTable.add(new Spinner("Y", yModel));
        positionTable.add(new Spinner("Z", zModel));
        positionTable.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor){
                Vector3 pos = new Vector3(xModel.getValue().floatValue(), yModel.getValue().floatValue(), zModel.getValue().floatValue());
                Quaternion rot = new Quaternion();
                instance.transform.getRotation(rot);
                instance.transform.set(pos, rot);
            }
        });

        //*Set rotation for instance*/
        VisTable rotationTable = new VisTable(true);
        //MODELS
        final FloatSpinnerModel xRotModel = new FloatSpinnerModel("0", "-999", "999");
        final FloatSpinnerModel yRotModel = new FloatSpinnerModel("0", "-999", "999");
        final FloatSpinnerModel zRotModel = new FloatSpinnerModel("0", "-999", "999");
        //TABLE
        rotationTable.add(new Spinner("X", xRotModel));
        rotationTable.add(new Spinner("Y", yRotModel));
        rotationTable.add(new Spinner("Z", zRotModel));
        rotationTable.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor){
                //*Fix issue instance keeps rotating in same direction after changing value */
                Quaternion rotation = new Quaternion().setEulerAngles(xRotModel.getValue().floatValue(), yRotModel.getValue().floatValue(), zRotModel.getValue().floatValue());
                instance.transform.rotate(rotation);
            }
        });

        add(positionTable).row();
        add(rotationTable).row();
        pack();
    }
}
