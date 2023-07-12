package ru.dingo3.streamingmusicbmbf.core;

import lombok.Getter;
import lombok.Setter;
import ru.dingo3.streamingmusicbmbf.providers.AbstractProvider;

import java.util.ArrayList;

public class ProviderManager {
    private boolean syncState = false;
    @Setter
    @Getter
    private Integer syncDelay = 1000;

    Thread syncThread;
    private ArrayList<AbstractProvider> providers;

    public ProviderManager() {
        providers = new ArrayList<>();
    }

    public void addProvider(AbstractProvider provider) {
        providers.add(provider);
    }

    public boolean getSyncState() {
        return syncState;
    }

    public void setSyncState(boolean syncState) {
        this.syncState = syncState;
    }

    public ArrayList<AbstractProvider> getProviders() {
        return providers;
    }

    public void startSync() {
        syncThread = new Thread(() -> {
            while (syncState) {
                for (AbstractProvider provider : providers) {
                    if (provider.getSync()) {
                        provider.sync();
                    }
                }
                try {
                    Thread.sleep(syncDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        syncThread.start();
    }

    public void stopSync() {
        syncState = false;
    }
}
