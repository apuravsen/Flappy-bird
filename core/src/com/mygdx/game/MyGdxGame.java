package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import javax.management.StringValueExp;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture background;
    Texture gameover;
    Texture[] birds;
    int flapstate = 0;
    float birdY = 0;
    float velocity = 0;
    int gamestate = 0;
    float gravity = 2;
    Texture toptube;
    Texture bottomtube;
    float gap = 400;
    float maxtubeoffset;
    Random randomgenerator;
    Circle birdcircle;
    ShapeRenderer shapeRenderer;
    BitmapFont font;


    float tubevelocity = 4;
    int nooftubes = 4;
    float[] tubeX = new float[nooftubes];
    float[] tubeoffset = new float[nooftubes];
    float distancebetweentubes;
    Rectangle[] toptuberectangles;
    Rectangle[] bottomtuberectangles;
    int score = 0;
    int scoringtube = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        birdcircle = new Circle();
        shapeRenderer = new ShapeRenderer();
        background = new Texture("bg.png");
        gameover = new Texture("gameover.png");
        toptube = new Texture("toptube.png");
        bottomtube = new Texture("bottomtube.png");
        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        randomgenerator = new Random();
        distancebetweentubes = Gdx.graphics.getWidth() * 3 / 4;
        toptuberectangles = new Rectangle[nooftubes];
        bottomtuberectangles = new Rectangle[nooftubes];
        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getData().setScale(10);
        startgame();

    }

    public void startgame() {
        birdY = (Gdx.graphics.getHeight() / 2) - (birds[0].getHeight() / 2);
        for (int i = 0; i < nooftubes; i++) {
            tubeoffset[i] = (randomgenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distancebetweentubes;
            toptuberectangles[i] = new Rectangle();
            bottomtuberectangles[i] = new Rectangle();

        }
    }

    @Override
    public void render() {
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gamestate == 1) {
            if (tubeX[scoringtube] < Gdx.graphics.getWidth() / 2) {
                score++;
                if (scoringtube < nooftubes - 1) {
                    scoringtube++;
                } else {
                    scoringtube = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                velocity = -30;
            }
            for (int i = 0; i < nooftubes; i++) {
                if (tubeX[i] < -toptube.getWidth()) {
                    tubeX[i] += nooftubes * distancebetweentubes;
                    tubeoffset[i] = (randomgenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                } else {
                    tubeX[i] = tubeX[i] - tubevelocity;
                }

                batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
                batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i]);
                toptuberectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i], toptube.getWidth(), toptube.getHeight());
                bottomtuberectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i], bottomtube.getWidth(), bottomtube.getHeight());

            }
            if (birdY > 0) {
                velocity = velocity + gravity;
                birdY -= velocity;
            } else gamestate = 2;
        } else if (gamestate == 0) {
            if (Gdx.input.justTouched()) {
                gamestate = 1;


            }

        } else if (gamestate == 2) {
            batch.draw(gameover, Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 2);
            if (Gdx.input.justTouched()) {
                gamestate = 1;
                startgame();
                score = 0;
                scoringtube = 0;
                velocity = 0;
            }
        }
        if (flapstate == 0) {
            flapstate = 1;
        } else {
            flapstate = 0;
        }

        batch.draw(birds[flapstate], (Gdx.graphics.getWidth() / 2) - (birds[flapstate].getWidth() / 2), birdY);
        font.draw(batch, String.valueOf(score), 10, 1700);


        //  shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // shapeRenderer.setColor(Color.RED);

        birdcircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapstate].getHeight() / 2, birds[flapstate].getWidth() / 2);
        //  shapeRenderer.circle(birdcircle.x,birdcircle.y,birdcircle.radius);


        for (int i = 0; i < nooftubes; i++) {
            // shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2+gap/2+tubeoffset[i],toptube.getWidth(),toptube.getHeight());
            //shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2-gap/2-bottomtube.getHeight()+tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());
            if (Intersector.overlaps(birdcircle, toptuberectangles[i]) || Intersector.overlaps(birdcircle, bottomtuberectangles[i])) {
                gamestate = 2;

            }


            //shapeRenderer.end();
        }
        batch.end();
    }
}

