package com.chesy.unstriplog.config;

import com.chesy.unstriplog.UnstripLog;
import com.chesy.unstriplog.item.ModItems;
import com.chesy.unstriplog.network.ConfigSyncManager;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import java.io.IOException;
import java.nio.file.*;

public class UnstripLogConfig {

    private static FileConfig config;
    private static Item bark;

    // default values
    private static final String DEFAULT_ITEM = "unstriplog:bark";
    private static final boolean DEFAULT_ALLOW_UNKNOWN = true;
    private static final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("unstriplog-common.toml");

    public static void init() {
        config = FileConfig.builder(configPath, TomlFormat.instance())
                .autosave()
                .sync()
                .build();

        config.load();
        writeDefaults();
        updateCache();
        startWatcher();
    }

    private static void writeDefaults() {
        if (!config.contains("item")) {
            config.set("item", DEFAULT_ITEM);
        }
        if (!config.contains("allowUnknownLog")) {
            config.set("allowUnknownLog", DEFAULT_ALLOW_UNKNOWN);
        }
        config.save();
    }

    private static void startWatcher() {
        Thread watcherThread = new Thread(() -> {
            try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
                configPath.getParent().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

                while (!Thread.currentThread().isInterrupted()) {
                    WatchKey key = watcher.take();

                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path changed = (Path) event.context();
                        if (changed.endsWith(configPath.getFileName())) {
                            UnstripLog.LOGGER.info("Config changed, reloading...");
                            Thread.sleep(100);
                            reload();
                            if (UnstripLog.SERVER != null) {
                                UnstripLog.SERVER.getPlayerList().getPlayers().forEach(ConfigSyncManager::sendTo);
                            }
                        }
                    }

                    key.reset();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                UnstripLog.LOGGER.error("Config watcher failed", e);
            }
        }, "unstriplog-config-watcher");

        watcherThread.setDaemon(true);
        watcherThread.start();
    }

    private static void updateCache() {
        String itemId = config.getOrElse("item", DEFAULT_ITEM);

        // validate
        Identifier id = Identifier.tryParse(itemId);
        if (id != null && BuiltInRegistries.ITEM.containsKey(id)) {
            bark = BuiltInRegistries.ITEM.getValue(id);
        } else {
            UnstripLog.LOGGER.warn("Invalid item id in config: {}, falling back to default", itemId);
            bark = ModItems.BARK;
        }
    }

    public static void reload() {
        config.load();
        updateCache();
    }

    public static Item getBark() {
        return bark;
    }

    public static String getConfiguredItemId() {
        return config.getOrElse("item", DEFAULT_ITEM);
    }

    public static boolean allowUnknownLog() {
        return config.getOrElse("allowUnknownLog", DEFAULT_ALLOW_UNKNOWN);
    }
}