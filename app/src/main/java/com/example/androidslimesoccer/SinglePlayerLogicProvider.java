package com.example.androidslimesoccer;


//TODO Will Be Completed By ALL

public class SinglePlayerLogicProvider {
    SlimeSprite slimeSprite1, slimeSprite2;
    BallSprite ballSprite;
    int slime1Goals = 0, slime2Goals = 0;

    public SinglePlayerLogicProvider(SlimeSprite slimeSprite1, SlimeSprite slimeSprite2, BallSprite ballSprite) {
        this.slimeSprite1 = slimeSprite1;
        this.slimeSprite2 = slimeSprite2;
        this.ballSprite = ballSprite;
    }


    public void update() {
        aiUpdateAction();
        slimeSprite1.update();
        slimeSprite2.update();
        ballSprite.update();
        slimeAndBallCollisionChecker(slimeSprite1);
        slimeAndBallCollisionChecker(slimeSprite2);
        if (slimeSprite1.specialIsActive)
            doSpecial(slimeSprite1, slimeSprite2);
        if (slimeSprite2.specialIsActive)
            doSpecial(slimeSprite2, slimeSprite1);
        goalChecker();
    }


    public void goalChecker() {

    }

    public void slimeAndBallCollisionChecker(SlimeSprite slimeSprite) {

    }

    public void aiUpdateAction() {

    }

    public void doSpecial(SlimeSprite performerSlimeSprite, SlimeSprite performedSlimeSprite) {

    }

    public int distance(SlimeSprite slimeSprite, BallSprite ballSprite) {
        return (int)(Math.sqrt(Math.pow(((ballSprite.x + Utils.ballRatio) - (slimeSprite.x + Utils.slimeRatio)), 2) +
                Math.pow(((ballSprite.y + Utils.ballRatio) - (slimeSprite.y - slimeSprite.slimeImage.getHeight())), 2)));
    }
}
