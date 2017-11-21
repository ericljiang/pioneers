package me.ericjiang.settlers.library;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ericjiang.settlers.library.data.GameDao;

@Getter
@AllArgsConstructor
public abstract class Game extends MultiplayerModule {

    private final String name;

    private final GameDao gameDao;

    public abstract GameSummary summarize();

    protected void save() {
        gameDao.save(this);
    }

}
