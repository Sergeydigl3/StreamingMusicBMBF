package ru.dingo3.streamingmusicbmbf.converters;

import ru.dingo3.streamingmusicbmbf.providers.models.BasePlaylist;
import ru.dingo3.streamingmusicbmbf.providers.models.BaseTrack;

public interface AbstractConverter {
    void convertTrack(BaseTrack track);
}
