package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class GameSceneScreen implements Screen {

    private Game game;
    private OrthographicCamera camera;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Body player;

    GameSceneScreen(Game game) {
        //init all shit
        this.game = game;
        camera = new OrthographicCamera();
        debugRenderer = new Box2DDebugRenderer();
        camera.setToOrtho(false, 40, 40);
        world = new World(new Vector2(0, -10), true);

        initGameWalls();
        initPlayer();

    }

    private void initGameWalls() {
        BodyDef groundBodyDef = new BodyDef();
        // Set its world position
        groundBodyDef.position.set(new Vector2(0, 1));
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        // Create a body from the defintion and add it to the world
        Body groundBody = world.createBody(groundBodyDef);

        // Create a polygon shape

        PolygonShape groundBox = new PolygonShape();
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(camera.viewportWidth, 1.0f);
        // Create a fixture from our polygon shape and add it to our ground body
        Fixture fixture = groundBody.createFixture(groundBox, 0.0f);
        // Clean up after ourselve

        //left wall
        groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(new Vector2(0, 0));
        groundBody = world.createBody(groundBodyDef);
        groundBox = new PolygonShape();
        groundBox.setAsBox(1, camera.viewportHeight);
        fixture = groundBody.createFixture(groundBox, 0.0f);
        fixture.setRestitution(1);
        //right wall
        groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(new Vector2(camera.viewportWidth, 0));
        groundBody = world.createBody(groundBodyDef);
        groundBox = new PolygonShape();
        groundBox.setAsBox(1, camera.viewportHeight);
        fixture = groundBody.createFixture(groundBox, 0.0f);
        fixture.setRestitution(1);
        //upper wall
        groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(new Vector2(camera.viewportWidth, camera.viewportHeight));
        groundBody = world.createBody(groundBodyDef);
        groundBox = new PolygonShape();
        groundBox.setAsBox(camera.viewportWidth, 1);
        fixture = groundBody.createFixture(groundBox, 0.0f);
        fixture.setRestitution(1);
        //middle wall
        groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(new Vector2(camera.viewportWidth / 2, 0));
        groundBody = world.createBody(groundBodyDef);
        groundBox = new PolygonShape();
        groundBox.setAsBox(1, camera.viewportHeight / 2);
        fixture = groundBody.createFixture(groundBox, 0.0f);
        fixture.setRestitution(1);
        groundBox.dispose();
    }

    private void initPlayer() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(2, 3);
        // add it to the world
        player = world.createBody(bodyDef);
        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(2, 2);

        // set the properties of the object ( shape, weight, restitution(bouncyness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 5f;
        fixtureDef.restitution = 1;
        // create the physical object in our body)
        // without this our body would just be data in the world
        player.createFixture(shape, 0);

        // we no longer use the shape object here so dispose of it.
        shape.dispose();
    }

    private void initBall() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(10, 30);
        // add it to the world
        Body circle = world.createBody(bodyDef);
        // set the shape (here we use a box 50 meters wide, 1 meter tall )
        CircleShape shape = new CircleShape();
        shape.setRadius(2);

        // set the properties of the object ( shape, weight, restitution(bouncyness)
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.restitution = 1;
        fixtureDef.friction = 0;

        // create the physical object in our body)
        // without this our body would just be data in the world
        circle.createFixture(shape, 1);

        // we no longer use the shape object here so dispose of it.
        shape.dispose();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        world.step(1 / 60f, 6, 2);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        debugRenderer.render(world, camera.combined);
        float yVel = player.getLinearVelocity().y;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.setLinearVelocity(20, yVel);
//            player.applyLinearImpulse(new Vector2(10, 0), player.getPosition(), true);
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                player.setLinearVelocity(-20, yVel);
//                player.applyLinearImpulse(new Vector2(-10, 0), player.getPosition(), true);
            } else {
                player.setLinearVelocity(new Vector2(0, player.getLinearVelocity().y));
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.applyForce(new Vector2(0, 500), player.getPosition(), true);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            initBall();
        }


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
