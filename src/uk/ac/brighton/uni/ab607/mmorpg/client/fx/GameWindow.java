package uk.ac.brighton.uni.ab607.mmorpg.client.fx;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import uk.ac.brighton.uni.ab607.mmorpg.client.ui.animation.Animation;
import uk.ac.brighton.uni.ab607.mmorpg.common.GameCharacterClass;
import uk.ac.brighton.uni.ab607.mmorpg.common.Player;
import uk.ac.brighton.uni.ab607.mmorpg.common.item.Chest;
import uk.ac.brighton.uni.ab607.mmorpg.common.object.Enemy;
import uk.ac.brighton.uni.ab607.mmorpg.common.request.ActionRequest;
import uk.ac.brighton.uni.ab607.mmorpg.common.request.ActionRequest.Action;
import uk.ac.brighton.uni.ab607.mmorpg.common.request.QueryRequest;
import uk.ac.brighton.uni.ab607.mmorpg.common.request.QueryRequest.Query;
import uk.ac.brighton.uni.ab607.mmorpg.common.request.ServerResponse;

import com.almasb.common.net.DataPacket;
import com.almasb.common.net.ServerPacketParser;
import com.almasb.common.net.UDPClient;
import com.almasb.common.util.Out;
import com.almasb.java.io.ResourceManager;
import com.almasb.java.ui.FXWindow;

public class GameWindow extends FXWindow {

    private String name;
    private UDPClient client = null;

    private Scene scene;

    private Group gameRoot = new Group(), uiRoot = new Group();

    private Font font;

    private Player player;

    private Group players = new Group();

    private String ip;

    private ArrayList<Player> playersList = new ArrayList<Player>();

    private int selX = 1000, selY = 600;

    private SimpleIntegerProperty money = new SimpleIntegerProperty();
    Button inventory = new Button("Inventory");

    public GameWindow(String ip, String playerName) {
        this.ip = ip;
        player = new Player(playerName, GameCharacterClass.NOVICE, 0, 0, "", 0);
        name = playerName;
    }

    @Override
    protected void createContent(Pane root) {
        try {
            ImageView background = new ImageView(ResourceManager.loadFXImage("map1.png"));
            gameRoot.getChildren().add(background);
        }
        catch (Exception e) {
            Out.e(e);
        }

        try {
            InputStream is = ResourceManager.loadResourceAsStream("Vecna.otf").get();
            font = Font.loadFont(is, 28);
            is.close();
        }
        catch (IOException e) {
            Out.e(e);
        }


        playersList.add(player);
        players.getChildren().add(player.sprite);

        gameRoot.getChildren().add(players);

        try {
            ImageView img = new ImageView(ResourceManager.loadFXImage("ui_hotbar.png"));
            img.setTranslateX(300);
            img.setTranslateY(580);
            uiRoot.getChildren().add(img);
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        UIMenuWindow menuWindow = new UIMenuWindow();
        UIStatsWindow statsWindow = new UIStatsWindow(player);


        // UI elements

        Button btnOptions2 = new Button("Menu");
        btnOptions2.setTranslateX(950);
        btnOptions2.setTranslateY(640);
        btnOptions2.setFont(font);
        btnOptions2.setOnAction(event -> {
            if (menuWindow.isShowing())
                menuWindow.minimize();
            else
                menuWindow.restore();
        });

        uiRoot.getChildren().add(btnOptions2);



        ProgressBar xpBar = new ProgressBar(0);
        xpBar.setPrefWidth(600);
        xpBar.setTranslateX(350);
        xpBar.setStyle("-fx-accent: rgb(255, 215, 0)");
        xpBar.progressProperty().bind(player.baseXPProperty);

        uiRoot.getChildren().add(xpBar);

        ProgressBar hpBar = new ProgressBar(0);
        hpBar.setTranslateX(240);
        hpBar.setTranslateY(635);
        hpBar.setRotate(-90);
        hpBar.progressProperty().bind(player.hpProperty.divide(
                player.statProperties[Player.MAX_HP].add(player.bonusStatProperties[Player.MAX_HP]).multiply(1.0f)));
        hpBar.progressProperty().addListener((obs, old, newValue) -> {
            int r = 255 - (int) (147*newValue.doubleValue());
            int g = (int)(200 * newValue.doubleValue());
            int b = 10;
            hpBar.setStyle(String.format("-fx-accent: rgb(%d, %d, %d)", r, g, b));
        });

        uiRoot.getChildren().add(hpBar);

        ProgressBar spBar = new ProgressBar(0);
        spBar.setTranslateX(850);
        spBar.setTranslateY(635);
        spBar.setRotate(-90);
        spBar.progressProperty().bind(player.spProperty.divide(
                player.statProperties[Player.MAX_SP].add(player.bonusStatProperties[Player.MAX_SP]).multiply(1.0f)));
        spBar.progressProperty().addListener((obs, old, newValue) -> {
            int r = 173 - (int)(42*newValue.doubleValue());
            int g = 223 - (int)(154*newValue.doubleValue());
            int b = 255;
            spBar.setStyle(String.format("-fx-accent: rgb(%d, %d, %d)", r, g, b));
        });

        uiRoot.getChildren().add(spBar);


        Button btnStats = new Button("Stats");
        btnStats.setTranslateX(1070);
        btnStats.setTranslateY(640);
        btnStats.setFont(font);
        btnStats.setOnAction(event -> {
            if (statsWindow.isShowing()) {
                statsWindow.minimize();
            }
            else {
                statsWindow.restore();
            }
        });

        uiRoot.getChildren().add(btnStats);


        inventory.textProperty().bind(money.asString().concat("G"));
        inventory.setTranslateX(1180);
        inventory.setTranslateY(640);
        inventory.setFont(font);
        inventory.setOnAction(event -> {

        });

        uiRoot.getChildren().add(inventory);




        root.getChildren().addAll(gameRoot, uiRoot);
    }

    private Random rand = new Random();

    private void moneyTest() {
        Platform.runLater(() -> {
            final int val = rand.nextInt(1000);
            Text text = new Text(val + "G");
            text.setFont(font);
            text.setFill(Color.GOLD);
            TranslateTransition tt = new TranslateTransition(Duration.seconds(1.66), text);
            tt.setFromX(player.getX() + rand.nextInt(1280) - 640);
            tt.setFromY(player.getY() + rand.nextInt(720) - 360);

            tt.setToX(player.getX() + 500);
            tt.setToY(player.getY() + 320);
            tt.setOnFinished(event -> {
                gameRoot.getChildren().remove(text);
                money.set(money.get() + val);
                ScaleTransition st = new ScaleTransition(Duration.seconds(0.66), inventory);
                st.setFromY(1);
                st.setToY(1.3);
                st.setAutoReverse(true);
                st.setCycleCount(2);
                st.play();
            });

            gameRoot.getChildren().add(text);

            tt.play();
        });
    }

    @Override
    protected void initScene(Scene scene) {
        this.scene = scene;

        gameRoot.layoutXProperty().bind(player.xProperty.subtract(640).negate());
        gameRoot.layoutYProperty().bind(player.yProperty.subtract(360).negate());


        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {

            }
            if (event.getCode() == KeyCode.LEFT) {

            }
            if (event.getCode() == KeyCode.UP) {

            }
            if (event.getCode() == KeyCode.DOWN) {

            }
        });

        scene.setOnMouseClicked(event -> {
            selX = (int)(event.getX() - gameRoot.getLayoutX()) / 40 * 40;
            selY = (int)(event.getY() - gameRoot.getLayoutY()) / 40 * 40;
        });
    }

    //boolean test = true;

    @Override
    protected void initStage(Stage primaryStage) {
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.setTitle("Orion MMORPG");
        primaryStage.setResizable(false);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        primaryStage.show();

        try {
            client = new UDPClient(ip, 55555, new ServerResponseParser());
            client.send(new DataPacket(new QueryRequest(Query.LOGIN, name)));

            //Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::showTraffic, 0, 10, TimeUnit.SECONDS);
        }
        catch (IOException e) {
            Out.e(e);
        }


        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::moneyTest, 0, 2, TimeUnit.SECONDS);
    }

    private void showTraffic() {
        // 10 seconds
        float kbs = client.resetAC() / 10240.0f;
        Out.d("showTraffic", kbs + " KB/s (IN). Required bandwidth: " + kbs * 10 + " kbit/s");

        kbs = client.resetACSent() / 10240.0f;
        Out.d("showTraffic", kbs + " KB/s (OUT). Required bandwidth: " + kbs * 10 + " kbit/s");
    }

    // TODO: add a single message sending from all UIs
    private ArrayList<ActionRequest> requests = new ArrayList<ActionRequest>();

    public void addActionRequest(ActionRequest action) {
        requests.add(action);
    }

    public ActionRequest[] clearPendingActionRequests() {
        ActionRequest[] res = new ActionRequest[requests.size()];
        requests.toArray(res);
        requests.clear();
        return res;
    }

    /**
     * Parses and updates the game client, including all windows
     * with the information taken from server packets
     *
     * @author Almas Baimagambetov (ab607@uni.brighton.ac.uk)
     * @version 1.0
     *
     */
    class ServerResponseParser extends ServerPacketParser {
        @Override
        public void parseServerPacket(DataPacket packet) {
            if (packet.byteData != null && packet.byteData.length > 0 && packet.byteData[0] == -127) {

                for (Player p : playersList)
                    p.sprite.setValid(false);


                // raw data of players containing drawing data
                ByteArrayInputStream in = new ByteArrayInputStream(packet.byteData);

                // number of players
                int size = packet.byteData.length / 32;
                for (int i = 0; i < size; i++) {
                    byte[] data = new byte[16];
                    byte[] name = new byte[16];

                    try {
                        in.read(data);
                        in.read(name);

                        String playerName = new String(name).replace(new String(new byte[] {0}), "");

                        // search list of players for name
                        // if found update their data
                        // else create new player with data

                        boolean newPlayer = true;

                        for (Player p : playersList) {
                            if (p.name.equals(playerName)) {
                                newPlayer = false;
                                p.loadFromByteArray(data);
                                p.sprite.setValid(true);
                                break;
                            }
                        }

                        if (newPlayer) {

                            Out.d("player", "new");

                            Player p = new Player(playerName, GameCharacterClass.NOVICE, 0, 0, "", 0);
                            p.loadFromByteArray(data);
                            playersList.add(p);
                            players.getChildren().add(p.sprite);
                        }
                    }
                    catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                // END FOR


                // TODO: test
                players.getChildren().removeIf(node -> {
                    Sprite s = (Sprite)node;
                    return !s.isValid();
                });

                playersList.removeIf(player -> !player.sprite.isValid());

                updateGameClient();
            }

            if (packet.objectData instanceof ServerResponse) {
                ServerResponse res = (ServerResponse) packet.objectData;

                //                            map = ObjectManager.getMapByName(res.data);
                //
                //                            selX = res.value1;
                //                            selY = res.value2;
            }

            if (packet.objectData instanceof Player) {
                Player p = (Player) packet.objectData;

                // update client's player
                player.update(p);

            }

            if (packet.multipleObjectData instanceof Player[]) {
                update((Player[]) packet.multipleObjectData);
            }

            if (packet.multipleObjectData instanceof Chest[]) {
                update((Chest[]) packet.multipleObjectData);
            }

            if (packet.multipleObjectData instanceof Enemy[]) {
                update((Enemy[]) packet.multipleObjectData);
            }

            if (packet.multipleObjectData instanceof Animation[]) {
                update((Animation[]) packet.multipleObjectData);
            }
        }

        private void updateGameClient() {
            if (player.getX() != selX || player.getY() != selY)
                addActionRequest(new ActionRequest(Action.MOVE, name, "map1.txt", selX, selY));

            try {
                ActionRequest[] thisGUI = clearPendingActionRequests();

                if (thisGUI.length > 0)
                    client.send(new DataPacket(thisGUI));
            }
            catch (IOException e) {
                Out.e("updateGameClient", "Failed to send a packet", this, e);
            }
        }

        /**
         * Update info about players
         *
         * @param sPlayers
         *                 players from server
         */
        private void update(Player[] sPlayers) {
            Player player = sPlayers[0];
        }

        /**
         * Updates info about chests
         *
         * @param sChests
         *                  chests from server
         */
        private void update(Chest[] sChests) {
            //gameObjects.set(INDEX_CHESTS, sChests);
        }

        /**
         * Updates info about enemies
         *
         * @param sChests
         *                  enemies from server
         */
        private void update(Enemy[] sEnemies) {
            //                    enemies.clear();
            //                    for (Enemy e : sEnemies) {
            //                        enemies.add(e);
            //                    }
            //
            //                    gameObjects.set(INDEX_ENEMIES, sEnemies);
            //
            //                    tmpEnemies = new ArrayList<Enemy>(enemies);
        }

        private void update(Animation[] sAnimations) {
            //gameObjects.set(INDEX_ANIMATIONS, sAnimations);
        }
    }
}
