package com.mygdx.game.EDITOR;

import static com.mygdx.game.helpers.Constants.KINEMATIC_FLAG;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.mygdx.game.helpers.GameObject;

public class Commands extends VisWindow{
    
	TextField normalTextField;
	VisTable textFieldTable;
	final LevelEditor levelEditor;
	Skin skin;

    public Commands(LevelEditor editor){
        super("Window Debug");
        TableUtils.setSpacingDefaults(this);
        addNormalWidgets();
        pack();
        setPosition(80, 100);
		levelEditor = editor;
    }

    private void addNormalWidgets () {
		skin = VisUI.getSkin();

		normalTextField = new TextField("/", skin);
		normalTextField.setCursorPosition(1);

		final TextField finalTextField = normalTextField;
		//* On ENTER get the command if it is valid and disable the text field until it is clicked */
		normalTextField.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.ENTER) {
                    String command = finalTextField.getText();
                    commandAction(command);
					resetTextDisplay();
                    return true;
                }
                return false;
            }
        });

		textFieldTable = new VisTable(true);
		textFieldTable.defaults().width(400);
		textFieldTable.add(normalTextField).growX();
		textFieldTable.clearActions();
		add(textFieldTable).row();
	}

	private void commandAction(String command){
		ModelBuilder mb = new ModelBuilder();
		GameObject gameObject = null;
		switch (command) {
			case "/box":
					mb.begin();
					mb.node().id = "box";
					mb.part("box", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.GRAY)))
						.box(1, 1f, 1f);
					Model model = mb.end();

					gameObject = new GameObject(model, 
						"box", 
						new btBoxShape(new Vector3(1,1,1)), 
						0, 
						new Vector3(0,0,0),
						KINEMATIC_FLAG
					);
					levelEditor.SetInstances(gameObject); //Add obj to instances.
				break;
			case "/cylinder":
					mb.begin();
					mb.node().id = "cylinder";
					mb.part("cylinder", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
						new Material(ColorAttribute.createDiffuse(Color.MAGENTA))).cylinder(1f, 2f, 1f, 10);
					model = mb.end();

					gameObject = new GameObject(model, 
						"cylinder", 
						new btCylinderShape(new Vector3(1,2,1)), 
						0, 
						new Vector3(0,0,0),
						KINEMATIC_FLAG
					);
					levelEditor.SetInstances(gameObject); //Add obj to instances.
				break;
			default: Gdx.app.log("CLI", "No command found.");
				break;
		}
	}

	private void resetTextDisplay(){
		normalTextField.setText("/");
		normalTextField.setCursorPosition(1);
		getStage().setKeyboardFocus(null);
	}
}
