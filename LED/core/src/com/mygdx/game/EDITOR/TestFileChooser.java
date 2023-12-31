package com.mygdx.game.EDITOR;

import static com.mygdx.game.helpers.Constants.KINEMATIC_FLAG;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.TableUtils;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.StreamingFileChooserListener;
import com.mygdx.game.helpers.GameObject;

public class TestFileChooser extends VisWindow {

	public TestFileChooser (LevelEditor editor) {
		super("Imported Models");
		FileChooser.setDefaultPrefsName("com.mygdx.game.EDITOR.TestFileChoose.filechooser;");
		FileChooser.setSaveLastDirectory(true);
		final FileChooser chooser = new FileChooser(Mode.OPEN);
		chooser.setSelectionMode(FileChooser.SelectionMode.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(true);
		chooser.setFavoriteFolderButtonVisible(true);

		final LevelEditor levelEditor = editor;
		chooser.setListener(new StreamingFileChooserListener() {
			@Override
			public void selected (FileHandle file) { //Choose what happens after selecting file.
				ModelLoader<?> loader = new ObjLoader();
				String fileName = file.name().toString();
		        GameObject object = new GameObject(loader.loadModel(file), 
								fileName.substring(0, fileName.lastIndexOf('.')), 
								null , 0f, new Vector3(0,0,0), KINEMATIC_FLAG);
				levelEditor.SetInstances(object);
			}
		});
		
        VisLabel pathLabel = new VisLabel("MODEL PATH: ");
		VisTextButton open = new VisTextButton(" Open ");
		TableUtils.setSpacingDefaults(this);

		open.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				chooser.setMode(Mode.OPEN);
				getStage().addActor(chooser.fadeIn());
			}
		});

        add(pathLabel);
		add(open);
		pack();
		setPosition(134, 320);
	}

}