package com.tillDawn.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;

public class Selector {
    private final Table container;
    private ScrollPane scrollPane;
    private final Table avatarTable;
    private String selectedAvatarPath;
    private ArrayList<String> avatarPaths;
    private Skin skin;

    public Selector(Skin skin, ArrayList<String> avatarPaths) {
        container = new Table();
        avatarTable = new Table();
        avatarTable.defaults().pad(10);
        this.skin = skin;

        this.avatarPaths = avatarPaths;

        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.DARK_GRAY);
        pixmap.fillCircle(50, 50, 50);

        NinePatch roundedBg = new NinePatch(new Texture(pixmap), 20, 20, 20, 20);
        pixmap.dispose();

        ScrollPane.ScrollPaneStyle style = new ScrollPane.ScrollPaneStyle();
        style.background = new NinePatchDrawable(roundedBg);

        scrollPane = new ScrollPane(avatarTable, style);
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setVisible(false);

        for (String path : avatarPaths) {
            Texture texture = new Texture(Gdx.files.internal(path));
            ImageButton avatarBtn = new ImageButton(new TextureRegionDrawable(new TextureRegion(texture)));
            avatarBtn.setUserObject(path);
            avatarBtn.setWidth(80f);
            avatarBtn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectedAvatarPath = (String) avatarBtn.getUserObject();
                    updateSelectionVisual();
                }
            });
            avatarTable.add(avatarBtn);
        }
        container.add(scrollPane).width(500).height(250).colspan(2);
    }

    private void updateSelectionVisual() {
    }

    public void toggleVisibility() {
        scrollPane.setVisible(!scrollPane.isVisible());
    }

    public Table getContainer() {
        return container;
    }

    public String getSelectedAvatarPath() {
        return selectedAvatarPath;
    }
}
