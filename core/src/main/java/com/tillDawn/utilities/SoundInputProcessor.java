package com.tillDawn.utilities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.tillDawn.model.App;

public class SoundInputProcessor extends InputAdapter {
    private Sound clickSound;

    public SoundInputProcessor(Sound clickSound) {
        this.clickSound = clickSound;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && App.isSfxStage()) {
            clickSound.play();
        }
        return false;
    }
}
