package GameClient.UI;

import GameClient.GameClientView;
import GameClient.Model.ClientPlayer;
import Shared.Logger.GameLog;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PlayerPanel extends JPanel {
    private GameClientView view;
    private JLabel player;
    private HashMap<Integer, PlayerComponent> playerComponentHashMap;

    public PlayerPanel(GameClientView view) {
        this.view = view;
        this.player = new JLabel("Player");
        player.setFont(FontManager.getFont().deriveFont(40f)); // NOI18N
        player.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        this.add(player);

        playerComponentHashMap = new HashMap<Integer, PlayerComponent>();

        this.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        this.setPreferredSize(new java.awt.Dimension(320, 60));
        this.setLayout(new java.awt.GridLayout(12, 1));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

//        this.removeAll();

//        GameLog.log("repainting....");

//        this.add(player);

        ClientPlayer.getPlayers().forEach((player) -> {

            if(playerComponentHashMap.get(player.getPlayerProfile().getId()) != null) {
                playerComponentHashMap.get(player.getPlayerProfile().getId()).setPlayerScore(player.getPlayerProfile().getScore());
                playerComponentHashMap.get(player.getPlayerProfile().getId()).update();
            }
            else {
                PlayerComponent newComponent = new PlayerComponent(player.getPlayerProfile().getName(), player.getPlayerProfile().getScore(), player.getPlayerProfile().getId());
                playerComponentHashMap.put(player.getPlayerProfile().getId(), newComponent);
                this.add(newComponent.getPlayerPanel());
            }

        });

        Set<Map.Entry<Integer, PlayerComponent>> componentsEntries = playerComponentHashMap.entrySet();

        Iterator<Map.Entry<Integer, PlayerComponent>> iterator = componentsEntries.iterator();

        while(iterator.hasNext()) {
            Map.Entry<Integer, PlayerComponent> playerComponentEntry = iterator.next();

            boolean hasValue = false;
            for(ClientPlayer player: ClientPlayer.getPlayers()) {
                if(playerComponentEntry.getValue().getId() == player.getPlayerProfile().getId()) {
                    hasValue = true;
                }
            }
            if(!hasValue) {
                this.remove(playerComponentEntry.getValue().getPlayerPanel());
                iterator.remove();
            }
        }

    }
}
