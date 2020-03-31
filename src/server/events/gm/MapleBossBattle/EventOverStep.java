
package server.events.gm.MapleBossBattle;

import java.util.List;
import java.util.ArrayList;

import constants.ExpTable;

import net.server.Server;
import client.MapleCharacter;
import server.maps.MapleMap;
import server.events.gm.core.EventStep;
import tools.MaplePacketCreator;

public class EventOverStep extends EventStep {
    public static final int MAP_EFFECT = 5120009;
    public static final int EVENT_OVER_MAP = 109050000;
    public static final String[] DESC = {
            "Thank you for participating!",
            "We hope to see you again!!"
    };

    MapleMap map;

    public EventOverStep(MapleMap map) {
        this.map = map;
    }

    // Impl abstract method
    protected void executeStep()  throws InterruptedException {
        for(String desc : DESC) {
            map.startMapEffect(desc, MAP_EFFECT, 7 * 1000);
            Thread.sleep(8 * 1000);
        }

        List<MapleCharacter> chars = new ArrayList<>(map.getCharacters());
        for(MapleCharacter chr : chars) {
            if(chr != null) {
                giveExpToParticipant(chr);
                MapleMap target = chr.getClient().getChannelServer().getMapFactory().getMap(EVENT_OVER_MAP);
                chr.changeMap(target, target.getRandomPlayerSpawnpoint());
            }
        }
    }

    private void giveExpToParticipant(MapleCharacter chr) {
        int requiredExp = ExpTable.getExpNeededForLevel(chr.getLevel());
        // Modified Sigmoid Exp % curve
        long exp = Math.round(
                requiredExp * (2 / (1 + Math.exp(
                        2.5 * ((double)chr.getLevel())/ExpTable.MAX_LEVEL
                )))
        );
        chr.gainExpNoModifiers(exp, true, true, true);
    }
}